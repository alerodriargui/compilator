package compilador.src.backend;


import compilador.src.symbolsTable.SymbolsTable;
import compilador.src.symbolsTable.Type.TipoSubyacente;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Backend {

    private SymbolsTable symbolsTable;

    private final String PATH = "output\\Backend_Tables.txt";
    // TV
    private HashMap<String, Variable> varTable;
    // TP
    private ArrayList<Procedure> procTable;
    // TE
    private ArrayList<Label> labelTable;

    private static int tmp_n = 0;

    public Backend(SymbolsTable symbolsTable) {
        this.symbolsTable = symbolsTable;
        this.varTable = new HashMap<>();
        this.procTable = new ArrayList<>();
        this.labelTable = new ArrayList<>();
    }

    // Adding a new VARIABLE into the table
    //    private String name;    // variable name
    //    private int code;       // variable code id
    //    private int idParent;   // parent id
    //    private int offset;     // offset
    //    private int size;       // space occupation
    //    private Type type;      // type
    public String addVar(String varname, int size, TipoSubyacente type, boolean isParam) {
        int scope = symbolsTable.getActualScope();
        int idParent = getLastProcedureId();
        String name = varname + "_" + idParent + "_" + scope;
        Procedure proc = procTable.get(idParent);
        int offset = (proc.getSize() + size);
        if(isParam) {
            offset = proc.getOffset() + 16;
            // We update its parent size
            proc.setOffset(offset);
        }
        if(!isParam){
            // We update its parent size
            proc.setSize(proc.getSize() + size);
        }
        // We add the variable into the table
        varTable.put(name,new Variable(name, idParent, offset, size, type, isParam));

        return name;
    }

    // STRING VARIABLE
    public String addStrVar(String varname, int size, String value) {
        int idParent = getLastProcedureId();
        int scope = symbolsTable.getActualScope();
        String name = varname + "_" + idParent + "_" + scope;
        // We add the variable into the table
        varTable.put(name,new StrVariable(name, idParent, size, value));

        return name;
    }

    // TMP STRING VARIABLE
    public String addTempStrVar(int size, String value) {
        String name = "T"+tmp_n;
        tmp_n++;
        int idParent = getLastProcedureId();
        // We add the variable into the table
        varTable.put(name,new StrVariable(name, idParent, size, value));

        return name;
    }

    public String addTempVar(int size, TipoSubyacente type) {
        String name = "T"+tmp_n;
        tmp_n++;
        int idParent = getLastProcedureId();
        // We add the variable into the table
        Procedure proc = procTable.get(idParent);
        int offset = (proc.getSize() + size);
        proc.setSize(offset);
        // We add the variable into the table
        varTable.put(name, new Variable(name, idParent, offset, size, type, false));
        return name;
    }

    // Adding a new PROCEDURE into the table
    //    private String name;    // its name
    //    private int nv;         // variable number
    //    private int depth;      // subprogram from comes
    //    private int size;       // memory used
    //  private int offset;     // offset
    //  private Type type;      // type
    public String addProc(String procName, int params,int size, int offset, TipoSubyacente type) {
        String name = "PROC_" + procName;
        // We add the new procedure
        procTable.add(new Procedure(name, params, size, type));
        return name;
    }

    public String addMain(){
        String name = "PROC_main";
        this.procTable.add(new Procedure(name, 0, 0, TipoSubyacente.TS_NULL));
        return name;
    }

    public String addLabel(){
        String label = "LABEL_" + labelTable.size();

        labelTable.add(new Label(label));

        return label;
    }

    public void storeTables() {
        String result = "---------------------------------------------\n"
                + "---------------- TABLES INFO ----------------\n"
                + "---------------------------------------------\n";

        result += "Variables:\n";
        for (int i = 0; i < varTable.size(); i++) {
            result += varTable.get(i) + "\n";
        }

        result += "\nProcedures:\n";
        for (int i = 0; i < procTable.size(); i++) {
            result += procTable.get(i) + "\n";
        }

        result += "\nLabels:\n";
        for (int i = 0; i < labelTable.size(); i++) {
            result += labelTable.get(i) + "\n";
        }

        try {
            // File Writter
            File file = new File(PATH);
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(result);
            writer.close();
        } catch (IOException ex) {
            System.out.println(" ERROR WRITING BACKEND TABLES");
            Logger.getLogger(Backend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVarAssembler(String name) {
        return this.varTable.get(name).getAssemblerDir();
    }

    public Variable getVariable(String name) {
        return this.varTable.get(name);
    }

    public Collection<Variable> getVariables(){
        return this.varTable.values();
    }

    public Procedure getProcedure(String proc){
        for (Procedure procedure : this.procTable) {
            if (procedure.getName().equals(proc)){
                return procedure;
            }
        }
        return null;
    }

    private int getLastProcedureId(){
        return procTable.get(procTable.size() - 1).getNv();
    }
}
