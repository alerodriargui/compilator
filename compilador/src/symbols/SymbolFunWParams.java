package compilador.src.symbols;

import compilador.src.sintactico.Param;

import java.util.ArrayList;


public class SymbolFunWParams extends SymbolBase {

    private ArrayList<Param> params;

    public SymbolFunWParams(Param param) {
        super("Symbol Function With Params", 0);
        this.params = new ArrayList<Param>();
        this.params.add(param);
    }

    public SymbolFunWParams(Param param, SymbolFunWParams prev) {
        super("Symbol Function With Params", 0);
        this.params = new ArrayList<Param>(prev.params);
        this.params.add(param);
    }

    public ArrayList<Param> getParams() {
        return this.params;
    }
}