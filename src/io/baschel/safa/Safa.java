package io.baschel.safa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by macobas on 06/11/17.
 */
public class Safa
{
    private static boolean hadError = false;

    public static void main(String[] args) throws Exception
    {
        if (args.length > 1) 
        {
            System.out.println("Usage: safa [script]");
        } 
        else if (args.length == 1) 
        {
            runFile(args[0]);
        } 
        else 
        {
            runREPL();
        }
    }

    private static void runREPL() throws IOException
    {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while(true)
        {
            System.out.print("> ");
            run(reader.readLine());
            hadError = false;
        }
    }

    private static void runFile(String path) throws IOException
    {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
    }

    private static void run(String source)
    {
        Lexer lex = new Lexer(source);
        List<Token> tokens = lex.lex();

        // For now, just print the tokens.
        if (hadError) return;
    }

    public static void error(int line, String message)
    {
        report(line, "", message);
    }

    static private void report(int line, String where, String message)
    {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }
}
