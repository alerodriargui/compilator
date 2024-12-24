package compilador.src;

import compilador.src.lexico.Lexico;
import compilador.src.sintactico.Parser;
import java_cup.internal_error;
import java_cup.runtime.ComplexSymbolFactory;
import jflex.exceptions.SilentExit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String WORK_DIR = USER_DIR + "\\compilador\\src\\";

    private static final String LEX_DIR = WORK_DIR + "lexico\\";
    private static final String JFLEX_FILE = LEX_DIR + "lexico.flex";

    private static final String CUP_DIR = WORK_DIR + "sintactico\\";
    private static final String CUP_FILE = CUP_DIR + "sintactico.cup";

    private static final String OUTPUT_DIR = "output\\";

    private static final boolean COMPILE = false;

    public static void main(String[] args) {
//        generateJavaFiles();
        String file = WORK_DIR + "examples\\example1.txt";
        if(args.length != 0){
            file = args[0];
        }
        executeCompiler(file);
    }

    private static void generateJavaFiles() {
        try {
            generateFlexFile();
        } catch (Exception e) {
            System.out.println("ERROR GENERANDO EL ARCHIVO: " + e.getMessage());
        }
        try {
            generateCupFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateFlexFile() throws SilentExit {
        jflex.Main.generate(new String[]{JFLEX_FILE});
    }

    private static void generateCupFile() throws internal_error, IOException, Exception {
        String[] commands = {/* "-dump_grammar", */ "-locations", "-parser", "Parser", CUP_FILE};
        java_cup.Main.main(commands);
        // generates on WorkDir folder Parser.java and ParserSym.java

        // MOVE FILES
        Path parser_o = Paths.get("Parser.java");
        Path parser_d = Paths.get(CUP_DIR + "Parser.java");

        // move parser
        Files.deleteIfExists(parser_d);
        Files.move(parser_o, parser_d);

        Path sym_o = Paths.get("ParserSym.java");
        Path sym_d = Paths.get(CUP_DIR + "ParserSym.java");

        // move parser symbols
        Files.deleteIfExists(sym_d);
        Files.move(sym_o, sym_d);
    }

    private static void executeCompiler(String file) {
        try {
            //Clean all output files
            cleanOutputFiles();

            // Here we are executing the files specified
            // We read the input.txt
            Reader reader = new BufferedReader(new FileReader(file));
            // generate intermediate code
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            Lexico scanner = new Lexico(reader, sf);
            Parser parser = new Parser(scanner, sf);
            // rezamos 3 ave marias, 5 padre nuestros y le hacemos una estatua a Andreu  para que todo funcione
            parser.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cleanOutputFiles() {
        File output_dir = new File(OUTPUT_DIR);
        if (output_dir.isDirectory()) {
            for (File file : output_dir.listFiles()) {
                file.delete();
            }
        }
    }
}
