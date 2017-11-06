package io.baschel.thlang;

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
public class Thlang
{
    private static boolean hadError = false;

    public static void main(String[] args) throws Exception
    {
        if (args.length > 1) 
        {
            System.out.println("Usage: thlang [script]");
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
        tokens.forEach(System.out::println);
        for (Token token : tokens)
        {
            System.out.println(token);
        }
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

}
