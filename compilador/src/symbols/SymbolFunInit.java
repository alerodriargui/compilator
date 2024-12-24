package compilador.src.symbols;

import compilador.src.symbolsTable.Type.TipoSubyacente;

public class SymbolFunInit extends SymbolBase {

    private String fun_id;
    private TipoSubyacente tipoSubyacente;

    public SymbolFunInit(String fun_id, TipoSubyacente tipoSubyacente) {
        super("Symbol Function Initalization", 0);
        this.tipoSubyacente = tipoSubyacente;
        this.fun_id = fun_id;
    }

    public TipoSubyacente getTipoSubyacente() {
        return this.tipoSubyacente;
    }

    public String getFunId() {
        return this.fun_id;
    }
}
