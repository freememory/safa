package io.baschel.safa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static io.baschel.safa.TokenType.*;

/**
 * Created by macobas on 06/11/17.
 */
public class Lexer
{
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;


    Lexer(String source)
    {
        this.source = source;
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
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;


            default:
                Safa.error(line, "Unexpected character.");
                break;
        }
    }

    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    private void number()
    {
        if('0' == peek())
        {
            if('x' == peekNext())
            {
                while (isDigit(peek()) || "ABCDEF".indexOf(peek()) != -1)
                    advance();

                addToken(NUMBER, new BigDecimal(new BigInteger(source.substring(start + 2, current), 16)));
                return;
            }
            else if('b' == peekNext())
            {
                while (isDigit(peek()))
                    addToken(NUMBER, new BigDecimal(new BigInteger(source.substring(start + 2, current), 2)));
                return;
            }
            else if(isDigit(peekNext()))
            {
                while (isDigit(peek()))
                    addToken(NUMBER, new BigDecimal(new BigInteger(source.substring(start + 1, current), 8)));
                return;
            }
        }

        while(isDigit(peek()) || '.' == peek() || 'e' == peek() || '+' == peek() || '-' == peek())
            advance();
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
                line++;
            if(peek() != '$')
                advance();
            if(match("$$$"))
            {
                addToken(STRING, source.substring(start + 3, current - 3));
                return;
            }
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
        return endIdx >= source.length();
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
