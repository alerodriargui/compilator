package compilador.src.errores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SemanticError extends Exception {
    private static BufferedWriter out = null;
    private final String PATH = "output\\semanticError.txt";

    public SemanticError(String error) {
        super(error + "\n");
        if (out == null) {
            try {
                out = new BufferedWriter(new FileWriter(PATH));
            } catch (Exception e) {
            }
        }
        try {
            out.write(error + "\n");
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
