package compilador.src.errores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LexicalError extends Exception {
    private static BufferedWriter out = null;
    private final String PATH = "output\\lexicalError.txt";

    public LexicalError(String error) {
        super(error);
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
