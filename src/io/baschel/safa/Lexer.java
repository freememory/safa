package io.baschel.safa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.baschel.safa.TokenType.*;

/**
 * Created by macobas on 06/11/17.
 */
public class Lexer
{
    private final String source;
    private final List<Token>            tokens     = new ArrayList<>();
    private       int                    start      = 0;
    private       int                    current    = 0;
    private       int                    line       = 1;
    private       Map<String, TokenType> keywordMap = new HashMap<>();

    Lexer(String source)
    {
        this.source = source;
        for(TokenType tt : TokenType.values())
            if(tt.isKeyword())
                keywordMap.put(tt.getText(), tt);
    }

    public List<Token> lex()
    {
        while (!isAtEnd())
        {
            start = current;
            scan();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scan()
    {
        char c = advance();
        switch (c) {
            case '(': addToken(LPAREN); break;
            case ')': addToken(RPAREN); break;
            case '{': addToken(LBRACE); break;
            case '}': addToken(RBRACE); break;
            case '[': addToken(LBRACKET); break;
            case ']': addToken(RBRACKET); break;
            case ',': addToken(COMMA); break;
            case ';': addToken(SEMICOLON); break;
            case '~': addToken(TILDE); break;

            // unambiguous n char tokens
            case '*':
                addToken(twoCharToken('=', STAREQ, STAR));
                break;
            case '%':
                addToken(twoCharToken('=', PERCENTEQ, PERCENT));
                break;
            case '!':
                addToken(twoCharToken('=', BANGEQ, BANG));
                break;
            case '^':
                addToken(twoCharToken('=', HATEQ, HAT));
                break;
            case '=':
                addToken(twoCharToken('=', EQEQ, EQ));
                break;
            case '>':
                addToken(twoCharToken('=', GTEQ, GT));
                break;
            case '<':
                addToken(twoCharToken('=', LTEQ, LT));
                break;

            // somewhat ambiguous n char tokens
            case '+':
                addToken(twoCharToken('+', PLUSPLUS, '=', PLUSEQ, PLUS));
                break;
            case '-':
                addToken(twoCharToken('-', MINUSMINUS, '=', MINUSEQ, MINUS));
                break;
            case '&':
                addToken(twoCharToken('&', AMPAMP, '=', AMPEQ, AMP));
                break;
            case '|':
                addToken(twoCharToken('|', PIPEPIPE, '=', PIPEEQ, PIPE));
                break;
            case '/':
                if (match('/'))
                {
                    // comments
                    while (peek() != '\n' && !isAtEnd()) advance();
                }
                else
                    addToken(twoCharToken('=', SLASHEQ, SLASH));
                break;
            case '.':
                if(match(".."))
                    addToken(DOTDOTDOT);
                else if(isDigit(peek()))
                    number();
                else
                    addToken(DOT);
                break;
            case '$':
                if (match("$$"))
                {
                    consumeMultilineString();
                    break;
                }
            case '"':
                string();
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            default:
                if(isDigit(c))
                        number();
                else if(isAlpha(c))
                    identifier();
                else
                    Safa.error(line, "Unexpected character.");

                break;
        }
    }

    private boolean isAlpha(char c)
    {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c)
    {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    private void identifier()
    {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        addToken(keywordMap.getOrDefault(text, IDENTIFIER));
    }

    private void number()
    {
        boolean possiblyOctal = false;
        try
        {
            if (source.charAt(start) == '0')
            {
                if ('x' == peek())
                {
                    advance();
                    while (isDigit(peek()) || "ABCDEFabcdef".indexOf(peek()) != -1)
                        advance();

                    addToken(NUMBER, new Double(Integer.valueOf(source.substring(start + 2, current), 16)));
                    return;
                }
                else if ('b' == peek())
                {
                    advance();
                    while (isDigit(peek()))
                        advance();
                    addToken(NUMBER, new Double(Integer.valueOf(source.substring(start + 2, current), 2)));
                    return;
                }
                else
                    possiblyOctal = true;
            }

            while (isDigit(peek()) || '.' == peek() || 'e' == peek() || '+' == peek() || '-' == peek())
            {
                char p = peek();
                if (!isDigit(p)) possiblyOctal = false;
                advance();
            }

            if (possiblyOctal)
                addToken(NUMBER, new Double(Integer.valueOf(source.substring(start, current), 8)));
            else
                addToken(NUMBER, new Double(source.substring(start, current)));
        }
        catch(Exception ex)
        {
            Safa.error(line, "Bad number.");
        }
    }

    private void string()
    {
        while (peek() != '"' && !isAtEnd())
        {
            if (peek() == '\n')
            {
                Safa.error(line, "Unterminated string.");
                return;
            }

            advance();
        }

        // Unterminated string.
        if (isAtEnd()) {
            Safa.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private void consumeMultilineString()
    {
        int beginline = line;
        while(!isAtEnd())
        {
            if(peek() == '\n')
            {
                line++;
                advance();
            }
            else if(peek() != '$')
                advance();
            else if(match("$$$"))
            {
                addToken(STRING, source.substring(start + 3, current - 3));
                return;
            }
            else
                advance();
        }

        Safa.error(beginline, "Unterminated multi-line string.");
    }

    private char peek()
    {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext()
    {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAtEnd()
    {
        return current >= source.length();
    }

    private boolean wouldEnd(int endIdx)
    {
        return endIdx > source.length();
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private boolean match(String expected)
    {
        int end = current + expected.length();
        if (isAtEnd() || wouldEnd(end)) return false;
        if (source.substring(current, current + expected.length()).equals(expected))
        {
            current += expected.length();
            return true;
        }

        return false;
    }

    private TokenType twoCharToken(char c, TokenType two, TokenType one)
    {
        return match(c) ? two : one;
    }

    private TokenType twoCharToken(char c, TokenType t1, char c2, TokenType t2, TokenType one)
    {
        if(match(c))
            return t1;
        return twoCharToken(c2, t2, one);
    }
}
