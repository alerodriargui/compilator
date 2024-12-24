package compilador.src.symbols;
/**
 *
 * @author soyjo
 */
public class SymbolArithValue extends SymbolBase{
    
    private String var_id;
    private Integer value;
    private boolean isConst;

    public SymbolArithValue(String var_id) {
        super("Symbol Arithmetical Value", 0);
        this.var_id = var_id;
        this.isConst = false;
    }

    public SymbolArithValue(String var_id, Integer value) {
        super("Symbol Arithmetical Value", 0);
        this.var_id = var_id;
        this.value = value;
        this.isConst = true;
    }

    public String getVarId(){
        return this.var_id;
    }

    public boolean getIsConst(){
        return this.isConst;
    }

    public Object getValue(){
        return this.value;
    } 
}
