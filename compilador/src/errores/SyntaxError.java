package compilador.src.errores;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SyntaxError extends Exception {
    private static BufferedWriter out = null;
    private final String PATH = "output\\syntaxError.txt";

    public SyntaxError(ComplexSymbol s, ArrayList<String> expectedName, boolean recovered) {
        String from = s.xleft.getLine() + ":" + s.xleft.getColumn();
        String to = s.xright.getLine() + ":" + s.xright.getColumn();
        String error = "Syntax Error: " + (s.value != null ? s.value : "") + "spanning from " + from + " to " + to;
        if (expectedName != null && expectedName.size() > 0) {
            error += "\t\n Token/s Expected: ";
            for (String token : expectedName) {
                error += token + " ";
            }
        }
        error += "\n";
        System.out.print(error);

        if (out == null) {
            try {
                out = new BufferedWriter(new FileWriter(PATH));
            } catch (Exception e) {
            }
        }
        try {
            out.write(error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
