package compilador.src.generatorAssembler;

import compilador.src.backend.Backend;
import compilador.src.backend.Procedure;
import compilador.src.backend.StrVariable;
import compilador.src.backend.Variable;
import compilador.src.c3a.GeneratorC3A;
import compilador.src.c3a.InstructionC3A;
import compilador.src.c3a.InstructionC3A.Code;
import compilador.src.errores.SymbolsTableError;
import compilador.src.symbolsTable.SymbolsTable;
import compilador.src.symbolsTable.Type;
import compilador.src.symbolsTable.Type.Tipo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// IMPORTANT: The assembler chosen is GAS
public class GeneratorAssembler {

    // Write the resulting code
    private BufferedWriter writer;
    private final String PATH = "output\\AssemblerCode_NOT_Optimized.s";
    private final String PATH_OPTIMIZED = "output\\AssemblerCode_Optimized.s";
    // Symbols Table
    private SymbolsTable symbolsTable;
    // TS + TV
    private Backend backend;

    private GeneratorC3A c3a_g;
    // List of instructions
    private ArrayList<String> assemblyInstructions;

    public GeneratorAssembler(SymbolsTable symbolTable, Backend backend, GeneratorC3A c3a_g) {
        //this.writer = writer;
        this.symbolsTable = symbolTable;
        this.backend = backend;
        this.c3a_g = c3a_g;
        assemblyInstructions = new ArrayList<String>();
    }

    public void generateAssembler(boolean optimized) {
        try {
            File file;
            if (optimized) {
                file = new File(PATH_OPTIMIZED);
            } else {
                file = new File(PATH);
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writeHead();
            ArrayList<InstructionC3A> instructions = c3a_g.getInstructions();
            for (int i = 0; i < instructions.size(); i++) {
                InstructionC3A ins = instructions.get(i);
                if (i < instructions.size() - 1) {
                    InstructionC3A next = instructions.get(i + 1);
                    toAssembly(ins, next);
                } else {
                    toAssembly(ins, null);
                }
            }
            writeBottom();
            for (String inst : assemblyInstructions) {
                writer.write(inst);
            }
            writer.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneratorAssembler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: CANNOT WRITE IN FILE");
        } catch (IOException e) {
            System.out.println("ERROR: CANNOT CREATE ASSEMBLY FILE");
        }
    }

    private void writeLine(String input) {
        assemblyInstructions.add(input + "\n");
    }

    public void writeC3A_Comment(InstructionC3A instruction) {
        assemblyInstructions.add("# " + instruction.toString().replace("\n", "\n# ") + "\n");
    }

    // Generates the header of program
    private void writeHead() {
        writeLine(".global main");
        /* C functions declaration */
        writeLine(".extern printf, scanf");
        writeLine(".data");
        declareStringVariables();
        writeLine(".text");
    }

    private void declareStringVariables() {
        // Only declare as global string values
        for (Variable var : backend.getVariables()) {
            if (var instanceof StrVariable) {
                writeLine(var.getName() + ": .asciz " + "\"" + ((StrVariable) var).getValue() + "\"");
            }
        }

        //Print formats for int and boolean.
        writeLine("format_int: .asciz \"%d\"");
        writeLine("true_label : .asciz \"true\"");
        writeLine("false_label : .asciz \"false\"");
    }

    private void writePrintBoolFunction() {
        writeLine("print_bool :");
        writeLine("cmpw $0,%di");
        writeLine("je print_false");
        writeLine("mov $true_label, %rdi");
        writeLine("jmp print_bool_val");
        writeLine("print_false : mov $false_label, %rdi");
        writeLine("print_bool_val : xor %rax, %rax");
        writeLine("call printf");
        writeLine("ret");
    }

    private void writeBottom() {
        writeLine("# exit");
        writeLine("\n# auxiliar functions");
        writeCMPFunctions();
        writePrintBoolFunction();
    }

    public void toAssembly(InstructionC3A instruction, InstructionC3A nextInstruction) {
        writeC3A_Comment(instruction);
        switch (instruction.getOpCode()) {
            case skip:
                skipInstruction(instruction);
                break;
            case rtn:
                returnInstruction(instruction);
                break;
            // "jump X place"
            case go_to:
                writeLine("jmp " + instruction.getDest());
                break;
            // Arithmetical expressions
            // Sume
            case add:
                calculateSumRes(instruction, "add");
                break;
            // Substract
            case sub:
                calculateSumRes(instruction, "sub");
                break;
            // Modul
            case mod:
                calculateDivision(instruction, "idiv", 2);
                break;
            // Product
            case prod:
                calculateMulu(instruction, "imul");
                break;
            // Division
            case div:
                calculateDivision(instruction, "idiv", 1);
                break;
            // Funtion related expressions
            case call:
                callInstruction(instruction);
                break;
            case param:
                paramInstruction(instruction);
                break;
            // Preamble expression
            case pmb:
                pmbInstruction(instruction);
                break;
            // Copy expression
            case copy:
                copyInstruction(instruction);
                break;

            // In order to obtain which branch is we are using an auxiliar method
            // called substract Jump
            case jump_cond:
                jumpCondInstruction(instruction);
                break;
            // Lower than
            case LT:
                // Lower/equals
            case LE:
                // Equals
            case EQ:
                // Negative
            case NE:
                // Greater/equals
            case GE:
                // Greater
            case GT:
                substractCMP(instruction, instruction.getOpCode());
                break;
            case and:
            case or:
                logicalInstruction(instruction);
                break;
            case not:
            case neg:
                unaryInstruction(instruction);
                break;
            case input:
                inputInstruction(instruction);
                break;
            case output:
                outputInstruction(instruction);
                break;
            default:
                break;
        }
        writeLine("");
    }

    private void jumpCondInstruction(InstructionC3A instruction) {
        writeLine("cmpw $1," + getVarAssembler(instruction.getOp1()));
        writeLine("je " + instruction.getDest());
    }

    private void inputInstruction(InstructionC3A instruction) {
        writeLine("push %rbp");
        writeLine("xor %rax, %rax");
        writeLine("mov $format_int, %rdi");
        writeLine("leaq " + getVarAssembler(instruction.getDest()) + ", %rsi");
        writeLine("call scanf");
        writeLine("pop %rbp");
    }

    private void unaryInstruction(InstructionC3A instruction) {
        Variable variable = backend.getVariable(instruction.getOp1());
        String suffix = "l";
        String register = "%edi";
        if (variable.getType() == Type.TipoSubyacente.TS_BOOLEAN) {
            suffix = "w";
            register = "%di";
        }
        writeLine("mov" + suffix + " " + variable.getAssemblerDir() + ", " + register);
        writeLine(instruction.getOpCode() + suffix + " " + register);
        writeLine("mov" + suffix + " " + register + ", " + getVarAssembler(instruction.getDest()));
    }

    private void logicalInstruction(InstructionC3A instruction) {
        // For sure that are boolean values
        // AND or OR
        writeLine("movw " + getVarAssembler(instruction.getOp1()) + ", %di");
        writeLine("movw " + getVarAssembler(instruction.getOp2()) + ", %ax");
        writeLine(instruction.getOpCode() + "w" + " %ax, %di");
        writeLine("movw %di, " + getVarAssembler(instruction.getDest()));
    }

    private void outputInstruction(InstructionC3A instruction) {
        /* when we call output, op1 stores type of dest in string format */
        if (instruction.getOp1().equals(Type.TipoSubyacente.TS_NUMBER.toString())) {
            writeLine("mov $format_int, %rdi");
            writeLine("xor %rsi, %rsi");
            writeLine("movl " + getVarAssembler(instruction.getDest()) + ", %esi");
            writeLine("xor %rax, %rax");
            writeLine("call printf");
        }
        if (instruction.getOp1().equals(Type.TipoSubyacente.TS_STRING.toString())) {
            writeLine("mov $" + instruction.getDest() + ", %rdi");
            writeLine("xor %rax, %rax");
            writeLine("call printf");
        }
        if (instruction.getOp1().equals(Type.TipoSubyacente.TS_BOOLEAN.toString())) {
            writeLine("movw " + getVarAssembler(instruction.getDest()) + ", %di");
            writeLine("call print_bool");
        }
    }

    // Auxiliar method for the skip Instruction
    private void skipInstruction(InstructionC3A instruction) {
        writeLine(instruction.getDest() + ":");
    }

    // Auxiliar method for return Instruction
    private void returnInstruction(InstructionC3A instruction) {
        // is function with return value, op1 register that stores return value
        if (instruction.getOp1() != null) {
            Variable variable = backend.getVariable(instruction.getOp1());
            writeLine("# Moving function result into %eax or %ax");
            String suffix = "l";
            String register = "%eax";
            if (variable.getType() == Type.TipoSubyacente.TS_BOOLEAN) {
                suffix = "w";
                register = "%ax";
            }
            writeLine("mov" + suffix + " " + variable.getAssemblerDir() + ", " + register);
        }

        // delete all reservated space
        Procedure proc = backend.getProcedure(instruction.getDest());
        writeLine("# Delete all reserved space");
        writeLine("addq $" + proc.getSize() + ", %rsp");
        writeLine("leave");
        writeLine("ret");
    }

    // Auxiliar method which will be helping with the arithmetical calculations (sum and rest)
    private void calculateSumRes(InstructionC3A instruction, String type) {
        boolean op1Lit = InstructionC3A.opIsInt(instruction.getOp1());
        boolean op2Lit = InstructionC3A.opIsInt(instruction.getOp1());
        String op1 = op1Lit ? "$" + instruction.getOp1() : getVarAssembler(instruction.getOp1());
        String op2 = op2Lit ? "$" + instruction.getOp2() : getVarAssembler(instruction.getOp2());
        // For sure that are
        writeLine("movl " + op1 + ", %edi");
        writeLine("movl " + op2 + ", %eax");
        writeLine(type + "l" + " %eax, %edi");
        writeLine("movl %edi, " + getVarAssembler(instruction.getDest()));
    }

    // Auxiliar method which will help with the / and % operations
    private void calculateDivision(InstructionC3A instruction, String type, int code) {
        boolean op1Lit = InstructionC3A.opIsInt(instruction.getOp1());
        boolean op2Lit = InstructionC3A.opIsInt(instruction.getOp1());
        String op1 = op1Lit ? "$" + instruction.getOp1() : getVarAssembler(instruction.getOp1());
        String op2 = op2Lit ? "$" + instruction.getOp2() : getVarAssembler(instruction.getOp2());

        writeLine("movl " + op1 + ", %eax");
        writeLine("cdq");
        writeLine("movl " + op2 + ", %edi");
        writeLine(type + "l" + " %edi");
        checkDivisionStatus(instruction, code);
    }

    // Auxiliar method to caculate if the instruction is a division or a modulus
    private void checkDivisionStatus(InstructionC3A instruction, int code) {
        if (code == 1) {
            //Division
            writeLine("movl %eax, " + getVarAssembler(instruction.getDest()));
        } else if (code == 2) {
            //modulus
            writeLine("movl %edx, " + getVarAssembler(instruction.getDest()));
        }
    }

    // Mulu calculation
    private void calculateMulu(InstructionC3A instruction, String type) {
        boolean op1Lit = InstructionC3A.opIsInt(instruction.getOp1());
        boolean op2Lit = InstructionC3A.opIsInt(instruction.getOp1());
        String op1 = op1Lit ? "$" + instruction.getOp1() : getVarAssembler(instruction.getOp1());
        String op2 = op2Lit ? "$" + instruction.getOp2() : getVarAssembler(instruction.getOp2());
        writeLine("movl " + op1 + ", " + "%edi");
        writeLine("movl " + op2 + ", " + "%eax");
        writeLine(type + "l" + " %eax" + ", %edi");
        writeLine("movl %edi, " + getVarAssembler(instruction.getDest()));
    }

    // Call Instruction
    private void callInstruction(InstructionC3A instruction) {
        writeLine("xor %rax, %rax   # clean return register");
        writeLine("call " + instruction.getDest());
        writeLine("# pop all params");
        int numParams = Integer.parseInt(instruction.getOp1());
        for (int i = 0; i < numParams; i++) {
            writeLine("pop %rdx");
        }
    }

    private void paramInstruction(InstructionC3A instruction) {
        Variable variable = backend.getVariable(instruction.getOp1());
        String code = "movslq";
        if (variable.getType() == Type.TipoSubyacente.TS_BOOLEAN) {
            code = "movswq";
        }
        writeLine(code + " " + variable.getAssemblerDir() + ", %rdx");
        writeLine("push %rdx");
    }

    public void writeSpecificLine(int lineNumber, String codeToWrite) {
        assemblyInstructions.add(lineNumber, codeToWrite);
    }

    // Auxiliar method which indicates what kind of jump are we analyzing
    private void substractCMP(InstructionC3A instruction, Code type) {
        boolean isNumCmp;
        if (InstructionC3A.opIsInt(instruction.getOp1()) || InstructionC3A.opIsInt(instruction.getOp2())) {
            isNumCmp = true;
        } else {
            Variable var = backend.getVariable(instruction.getOp1());
            if (var == null) {
                var = backend.getVariable(instruction.getOp2());
            }
            isNumCmp = var.getType() == Type.TipoSubyacente.TS_NUMBER;
        }
        String suffix = "l";
        String register1 = "%edi";
        String register2 = "%esi";
        if (!isNumCmp) {
            suffix = "w";
            register1 = "%di";
            register2 = "%si";
        }
        writeLine("mov" + suffix + " " + checkLiteral(instruction.getOp2()) + ", " + register1);
        writeLine("mov" + suffix + " " + checkLiteral(instruction.getOp1()) + ", " + register2);
        writeLine("xor %rax, %rax # clean return value register");
        String functionLabel = getCMPFunctionLabel(type, isNumCmp);
        writeLine("call " + functionLabel);
        writeLine("movw %ax," + getVarAssembler(instruction.getDest()) + " # get return value");
    }

    private String checkLiteral(String operand) {
        boolean opInt = InstructionC3A.opIsInt(operand);
        boolean opBool = InstructionC3A.opIsBoolean(operand);
        //literall number
        if (opInt) {
            return "$" + operand;
        }
        // literall boolean
        if (opBool) {
            // true
            if (operand.equals("true")) {
                return "$1";
            }
            // false
            return "$0";
        }
        return getVarAssembler(operand);
    }

    // Auxiliar method that generates the copy Instruction
    private void copyInstruction(InstructionC3A instruction) {
        if (instruction.getOp1().equals("return")) {
            Procedure procedure = backend.getProcedure(instruction.getOp2());
            String suffix = "l";
            String register = "%eax";
            if (procedure.getType() == Type.TipoSubyacente.TS_BOOLEAN) {
                suffix = "w";
                register = "%ax";
            }
            writeLine("mov" + suffix + " " + register + ", " + getVarAssembler(instruction.getDest()));
        } else {
            Variable variable = backend.getVariable(instruction.getDest());
            boolean isNum = variable.getType() == Type.TipoSubyacente.TS_NUMBER;
            String suffix = "l";
            String register = "%edi";
            if (!isNum) {
                suffix = "w";
                register = "%di";
            }
            writeLine("mov" + suffix + " " + checkLiteral(instruction.getOp1()) + ", " + register);
            writeLine("mov" + suffix + " " + register + ", " + getVarAssembler(instruction.getDest()));
        }
    }

    private void pmbInstruction(InstructionC3A instruction) {
        try {
            writeLine("push %rbp        # Guardem el registre que utilitzarem com a apuntador de la pila.");
            writeLine("mov %rsp, %rbp");
            //Declarar parametros del procedimiento como variables.
            String backFunId = instruction.getDest();
            String funId = backFunId.replace("PROC_", "");
            Type type = symbolsTable.get(funId);

            if (type == null || type.getTipo() != Tipo.dfun) {
                throw new Error("Invalid function");
            }

            //save space for local variables
            Procedure proc = backend.getProcedure(backFunId);
            writeLine("sub $" + proc.getSize() + ", %rsp");
        } catch (SymbolsTableError e) {
            //AQUI MAU S'HAURIA D'ARRIBAR JA QUE SINTÀTIC JA S'ENCARREGA DE COMPROVAR
        }
    }

    private String getVarAssembler(String varName) {
        return backend.getVarAssembler(varName);
    }

    private String getCMPFunctionLabel(Code code, boolean numCmp) {
        switch (code) {
            case EQ:
                if (numCmp) {
                    return "CMP_EQ_NUM";
                }
                return "CMP_EQ";
            case GE:
                return "CMP_GE";
            case GT:
                return "CMP_GT";
            case LE:
                return "CMP_LE";
            case LT:
                return "CMP_LT";
            case NE:
                if (numCmp) {
                    return "CMP_NE_NUM";
                }
                return "CMP_NE";
            default:
                return "";
        }
    }

    private void writeCMPFunctions() {
        writeEQ();
        writeNE();
        writeGT();
        writeGE();
        writeLE();
        writeLT();
        writeNE_num();
        writeEQ_num();
    }

    private void writeLT() {
        // comparation between two integers
        writeLine("# boolean value assignation LT");
        writeLine("CMP_LT :");
        writeLine("\tcmp %edi, %esi");
        writeLine("\tjge CMP_LT_GE");
        writeLine("\tmov $1, %ax");
        writeLine("\tret");
        writeLine("CMP_LT_GE :");
        writeLine("\tmov $0, %ax");
        writeLine("\tret\n");
    }

    private void writeLE() {
        writeLine("# boolean value assignation LE");
        writeLine("CMP_LE :");
        writeLine("\tcmp %edi, %esi");
        writeLine("\tjg CMP_LE_G");
        writeLine("\tmov $1, %ax");
        writeLine("\tret");
        writeLine("CMP_LE_G :");
        writeLine("\tmov $0, %ax");
        writeLine("\tret\n");
    }

    private void writeGE() {
        writeLine("# boolean value assignation GE");
        writeLine("CMP_GE :");
        writeLine("\tcmp %edi, %esi");
        writeLine("\tjl CMP_GE_L");
        writeLine("\tmov $1, %ax");
        writeLine("\tret");
        writeLine("CMP_GE_L :");
        writeLine("\tmov $0, %ax");
        writeLine("\tret\n");
    }

    private void writeGT() {
        writeLine("# boolean value assignation GT");
        writeLine("CMP_GT :");
        writeLine("\tcmp %edi, %esi");
        writeLine("\tjle CMP_GT_LE");
        writeLine("\tmov $1, %ax");
        writeLine("\tret");
        writeLine("CMP_GT_LE :");
        writeLine("\tmov $0, %ax");
        writeLine("\tret\n");
    }

    private void writeNE() {
        writeLine("# boolean value assignation NE");
        writeLine("CMP_NE :");
        writeLine("\tcmp %di, %si");
        writeLine("\tje CMP_NE_E");
        writeLine("\tmov $1, %rax");
        writeLine("\tret");
        writeLine("CMP_NE_E :");
        writeLine("\tmov $0, %rax");
        writeLine("\tret\n");
    }

    private void writeEQ() {
        writeLine("# boolean value assignation EQ");
        writeLine("CMP_EQ :");
        writeLine("\tcmp %di, %si");
        writeLine("\tjne CMP_EQ_NE");
        writeLine("\tmov $1, %rax");
        writeLine("\tret");
        writeLine("CMP_EQ_NE :");
        writeLine("\tmov $0, %rax");
        writeLine("\tret\n");
    }

    private void writeNE_num() {
        writeLine("# boolean value assignation NE num");
        writeLine("CMP_NE_NUM :");
        writeLine("\tcmp %edi, %esi");
        writeLine("\tje CMP_NE_E_NUM");
        writeLine("\tmov $1, %ax");
        writeLine("\tret");
        writeLine("CMP_NE_E_NUM :");
        writeLine("\tmov $0, %ax");
        writeLine("\tret\n");
    }

    private void writeEQ_num() {
        writeLine("# boolean value assignation EQ num");
        writeLine("CMP_EQ_NUM :");
        writeLine("\tcmp %edi, %esi");
        writeLine("\tjne CMP_EQ_NE_NUM");
        writeLine("\tmov $1, %ax");
        writeLine("\tret");
        writeLine("CMP_EQ_NE_NUM :");
        writeLine("\tmov $0, %ax");
        writeLine("\tret\n");
    }
}
