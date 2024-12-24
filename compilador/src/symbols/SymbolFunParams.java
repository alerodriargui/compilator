package compilador.src.symbols;


import compilador.src.sintactico.Param;

import java.util.ArrayList;

public class SymbolFunParams extends SymbolBase {

    private ArrayList<Param> params = new ArrayList<Param>();

    public SymbolFunParams() {
        super("Symbol Function Params", 0);
    }

    public SymbolFunParams(ArrayList<Param> params) {
        super("Symbol Function Params", 0);
        this.params = params;
    }

    public ArrayList<Param> getParams() {
        return this.params;
    }
}
