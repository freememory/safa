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
    CLASS("class"),
    ELSE("else"),
    FALSE("false"),
    FUNC("func"),
    FOR("for"),
    IF("if"),
    IN("in"),
    NULL("null"),
    RETURN("print"),
    MEMBERS("members"),
    GET("get"),
    SET("set"),
    THIS("this"),
    TRUE("true"),
    WHILE("while"),

    EOF(null);

    private String text;
    TokenType(String repr)
    {
        this.text = repr;
    }

    public String getText() { return text; }
}