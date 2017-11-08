package io.baschel.safa;

/**
 * Created by macobas on 06/11/17.
 */
enum TokenType {
    // Single-character tokens.
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    COMMA(","),
    SEMICOLON(";"),

    // One or two character tokens.
    BANG("!"),
    BANGEQ("!="),
    EQ("="),
    EQEQ("=="),
    MINUS("-"),
    MINUSEQ("-="),
    MINUSMINUS("--"),
    PLUS("+"),
    PLUSEQ("+="),
    PLUSPLUS("++"),
    STAR("*"),
    STAREQ("*="),
    SLASH("/"),
    SLASHEQ("/="),
    PERCENT("%"),
    PERCENTEQ("%="),
    GT(">"),
    DOT("."),
    DOTDOTDOT("..."),
    GTEQ(">="),
    LT("<"),
    LTEQ("<="),
    AMP("&"),
    AMPAMP("&&"),
    AMPEQ("&="),
    PIPE("|"),
    PIPEPIPE("||"),
    PIPEEQ("|="),
    HAT("^"),
    HATEQ("^="),
    TILDE("~"),
    BLING("$$$"),

    // Literals.
    IDENTIFIER(null),
    STRING(null),
    NUMBER(null),

    // Keywords.
    CLASS("class", true),
    ELSE("else", true),
    FALSE("false", true),
    FUNC("func", true),
    FOR("for", true),
    IF("if", true),
    IN("in", true),
    NULL("null", true),
    RETURN("return", true),
    BREAK("break", true),
    CONTINUE("continue", true),
    GET("get", true),
    SET("set", true),
    TRUE("true", true),
    WHILE("while", true),

    EOF(null);

    private String text;
    private boolean keyword;
    TokenType(String repr)
    {
        this(repr, false);
    }

    TokenType(String repr, boolean kw)
    {
        this.text = repr;
        this.keyword = kw;
    }

    public String getText() { return text; }

    public boolean isKeyword() { return keyword; }
}