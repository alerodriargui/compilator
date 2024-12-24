package compilador.src.c3a;

import compilador.src.backend.Backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneratorC3A {

    // txt folder where we'll find the data
    private static final String PATH = "output\\c3_code.txt";
    private static final String PATH_OPTIMIZED = "output\\c3_code_optimized.txt";

    // other variables used
    private int variableNumber;
    private ArrayList<InstructionC3A> instructions;

    // private Optimizer optimizer; NOT IMPLEMENTED

    // Writer to save information
    private BufferedWriter writer;

    public GeneratorC3A(Backend backend) {
        variableNumber = 0;
        this.instructions = new ArrayList<InstructionC3A>();
    }

    /* NOT IMPLEMENTED
      public void optimize() {
        // optimizer = new Optimizer(instructions);
        // this.instructions = optimizer.optimize();
    } */

    // Add a new instruction
    public void generateC3aInstr(InstructionC3A.Code opCode, String op1, String op2, String dest) {

        InstructionC3A inst = new InstructionC3A(opCode, op1, op2, dest);
        instructions.add(inst);
    }

    // public void generateC3aInstr(int index, Code opCode, String op1, String op2, String dest) {
    //     instructions.add(index, new Instruction(opCode, op1, op2, dest));
    // }

    public void savec3aInFile(boolean optimized) {
        String result = "-----------------------------------------------\n"
                + "---------------- C3@ code list"
                + (optimized ? "optimized" : "")
                + " ----------------\n"
                + "-----------------------------------------------\n";
        for (int i = 0; i < instructions.size(); i++) {
            result += instructions.get(i) + "\n\n";
        }
        try {
            // File Writter
            File file;
            if (optimized) {
                file = new File(PATH_OPTIMIZED);
            } else {
                file = new File(PATH);
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(result);
            writer.close();
        } catch (IOException ex) {
            System.out.println("ERROR WRITING C3@");
            Logger.getLogger(GeneratorC3A.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getVariableNumber() {
        return variableNumber;
    }

    public void setVariableNumber(int variableNumber) {
        this.variableNumber = variableNumber;
    }

    public ArrayList<InstructionC3A> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<InstructionC3A> instructions) {
        this.instructions = instructions;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }
}