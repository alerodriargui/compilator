package compilador.src.symbolsTable;

import compilador.src.errores.SymbolsTableError;
import compilador.src.symbolsTable.Type.Tipo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SymbolsTable {

    /*
    TODO: SE PUEDE REFACTORIZAR LAS TABLAS PARA QUE QUEDE DISTINTO
     */
    private int scope;
    private ArrayList<Integer> scopeTable;
    private HashMap<String, Description> descriptionTable;
    private ArrayList<Expansion> expansionTable;
    private static BufferedWriter out;
    private final String SYMBOLS_TABLE_PATH = "output\\SymbolsTableData.txt";

    public SymbolsTable() {
        reset();
        try {
            out = new BufferedWriter(new FileWriter(SYMBOLS_TABLE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Before deleting the symbols table we do write one last time
        saveTableInFile(null);
    }

    public void add(String id, Type type) throws SymbolsTableError {
        Description oldDescription = descriptionTable.get(id);

        // if oldDes.scope > scope can override
        if (oldDescription != null && oldDescription.getScope() <= scope) {
            if (oldDescription.getScope() == scope) {
                throw new SymbolsTableError(id + "cannot be added because it already exists in actual scope");
            }
            if (oldDescription.getType().getTipo() == Tipo.dfun) {
                throw new SymbolsTableError(id + "cannot be added because it already exists and is a function");
            }
            if (oldDescription.getType().getTipo() == Tipo.dtype) {
                throw new SymbolsTableError(id + "cannot be added because it is a reserved word");
            }
            // move oldDescription to expansionTable
            int expIndex = scopeTable.get(scope);
            scopeTable.set(scope, expIndex + 1);
            Expansion exp = new Expansion(oldDescription);
            expansionTable.add(expIndex, exp);
        }
        // write into description table
        Description newDesc = new Description(id, type, scope);
        descriptionTable.put(id, newDesc);
        // We've just put data inside the table, so we are writing it
        saveTableInFile("ADD : " + id);
    }

    public void addParam(String idFun, String idParamBack, String idParam, Type type) throws SymbolsTableError {
        Description funDes = descriptionTable.get(idFun);
        // CHECK TYPE
        if (funDes == null) {
            throw new SymbolsTableError(idFun + "does not exist.");

        }
        if (funDes.getType().getTipo() != Tipo.dfun) {
            throw new SymbolsTableError(idFun + "is not a function");
        }

        int idxe = funDes.getFirst();
        int idxep = -1;

        while (idxe != -1 && expansionTable.get(idxe).getId() != idParam) {
            idxep = idxe;
            idxe = ((ParamExpansion) expansionTable.get(idxe)).getNext();
        }

        if (idxe != -1) {
            throw new SymbolsTableError(idParam + "already exists as function param");
        }

        idxe = scopeTable.get(scope);
        scopeTable.set(scope, idxe + 1);
        ParamExpansion exp = new ParamExpansion(type, idFun, idParamBack, idParam, -1, -1);
        expansionTable.add(idxe, exp);
        if (idxep == -1) {
            funDes.setFirst(idxe);
            descriptionTable.put(idFun, funDes);
        } else {
            ParamExpansion expP = (ParamExpansion) expansionTable.get(idxep);
            expP.setNext(idxe);
            expansionTable.set(idxep, expP);
        }
        // We've just put data inside the table, so we are writing it
        saveTableInFile("ADD PARAM: " + idParamBack + " function : " + idFun);
    }

    public Type get(String id) throws SymbolsTableError {
        if (!descriptionTable.containsKey(id)) {
            //first check if is param inside expansion
            for (Expansion expansion : expansionTable) {
                if (expansion instanceof ParamExpansion) {
                    ParamExpansion param = (ParamExpansion) expansion;
                    if (param.getParamId().equals(id)) {
                        return param.getType();
                    }
                }
            }
            throw new SymbolsTableError("Unknown id: " + id);
        }
        return descriptionTable.get(id).getType();
    }

    public int getNumParams(String idFun) throws SymbolsTableError {
        Description funDes = descriptionTable.get(idFun);
        int count = 0;
        // CHECK TYPE
        if (funDes == null) {
            throw new SymbolsTableError(idFun + "function not found");

        }
        if (funDes.getType().getTipo() != Tipo.dfun) {
            throw new SymbolsTableError(idFun + "is not a function");
        }

        int idxe = funDes.getFirst();

        while (idxe != -1) {
            count++;
            idxe = ((ParamExpansion) expansionTable.get(idxe)).getNext();
        }

        return count;
    }

    public Type getParam(String idFun, int index) throws SymbolsTableError {
        Description funDes = descriptionTable.get(idFun);
        // CHECK TYPE
        if (funDes == null) {
            throw new SymbolsTableError(idFun + "function not found");

        }
        if (funDes.getType().getTipo() != Tipo.dfun) {
            throw new SymbolsTableError(idFun + "is not a function");
        }

        int idxe = funDes.getFirst();
        int pos = index;

        while (idxe != -1 && pos > 0) {
            idxe = ((ParamExpansion) expansionTable.get(idxe)).getNext();
            pos--;
        }

        if (idxe == -1) {
            throw new SymbolsTableError(idFun + "has" + " param at index" + index + " does not exist");
        }

        return expansionTable.get(idxe).getType();
    }

    public ArrayList<Expansion> getParams(String idFun) throws SymbolsTableError {
        Description funDes = descriptionTable.get(idFun);
        // CHECK TYPE
        if (funDes == null) {
            throw new SymbolsTableError(idFun + "function not found");

        }
        if (funDes.getType().getTipo() != Tipo.dfun) {
            throw new SymbolsTableError(idFun + "is not a function");
        }

        ArrayList<Expansion> params = new ArrayList<Expansion>();
        int idxe = funDes.getFirst();

        while (idxe != -1) {
            params.add(expansionTable.get(idxe));
            idxe = ((ParamExpansion) expansionTable.get(idxe)).getNext();
        }

        return params;
    }

    public void enterBlock() {
        scopeTable.add(scope + 1, scopeTable.get(scope));
        scope += 1;
        // We are increasing the scope lvl
        saveTableInFile("ENTER BLOCK : increase scope");
    }

    public void leaveBlock() throws SymbolsTableError {
        if (scope == 1) {
            throw new SymbolsTableError("Compiler error : out of scope 1");
        }
        if (scope != 1) {
            this.scope--;
            // remove out of scope variables, or 
            // iterate over hashmap
            ArrayList<String> keys = new ArrayList<String>(descriptionTable.keySet());
            for (String key : keys) {
                if (descriptionTable.get(key).getScope() > this.scope) {
                    descriptionTable.remove(key);
                }
            }
            // move from expanstion to description
            int first = scopeTable.get(scope + 1) - 1;
            int last = scopeTable.get(scope);

            for (int i = first; i >= last; i--) {
                // not move coplex types like params
                if (expansionTable.get(i).getScope() != -1) {
                    Description des = new Description(expansionTable.remove(i));
                    descriptionTable.put(des.getId(), des);
                }
            }

            this.scopeTable.remove(this.scopeTable.size() - 1);
            // We are decreasing the block's level and deleting data
            saveTableInFile("LEAVE BLOCK : decrease scope");
        }
    }

    public void reset() {
        scopeTable = new ArrayList<Integer>();
        descriptionTable = new HashMap<String, Description>();
        expansionTable = new ArrayList<Expansion>();
        scopeTable.add(0);
        scopeTable.add(0);
        scope = 1;
    }

    public void saveTableInFile(String action) {
        // We ensure that the .txt file is deleted every time we launch the whole compiler (?)
        // This method is called everytime something ocurres to the Symbols Table

        // Header
        String result = "";
        if (action != null) {
            result = "-----------------------------------------------\n"
                    + "----------------- ACTION DONE -----------------\n"
                    + "\t" + action + "\n"
                    + "-----------------------------------------------\n\n";
        }
        String header_bottom =
                "-----------------------------------------------\n"
                        + "------------- SYMBOLS TABLE DATA -------------- \n"
                        + "-----------------------------------------------\n\n";

        result += header_bottom;
        // Scope table data
        result += "-----------------------------------------------\n";
        result += "--------------- SCOPE INFO : " + this.scope + " ----------------\n";

        for (int i = 0; i < this.scopeTable.size(); i++) {
            result += "scope:" + i + ", pointing at: " + scopeTable.get(i) + " value\n";
        }
        result += "-----------------------------------------------\n\n";

        // Description table data
        result += "-----------------------------------------------\n";
        result += "-------------- DESCRIPTION TABLE --------------\n\n";
        for (String key : this.descriptionTable.keySet()) {
            Description desc = this.descriptionTable.get(key);
            result += desc.toString() + "\n";
        }
        result += "\n-----------------------------------------------\n\n";

        // Expansion table data
        result += "-----------------------------------------------\n";
        result += "------------- EXPANSION TABLE -----------------\n\n";
        for (int i = 0; i < this.expansionTable.size(); i++) {
            result += this.expansionTable.get(i).toString() + "\n";
        }
        result += "\n-----------------------------------------------\n\n";
        result += "-------------- END SYMBOLS TABLE --------------\n\n";

        // Write in a file
        try {
            out.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getActualScope() {
        return this.scope;
    }

    public void closeSymbolsTableFiles() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


