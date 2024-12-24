/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.src.symbols;

/**
 *
 * @author soyjo
 */
public class SymbolBoolOpValue extends SymbolBase{

    private Boolean value;
    private boolean isConstant;
    private String var_id;

    public SymbolBoolOpValue(String var_id) {
        super("Symbol Boolean Operation Value", 0);
        this.isConstant = false;
        this.var_id = var_id;
    }
    
    public SymbolBoolOpValue(String var_id, Boolean value) {
        super("Symbol Boolean Operation Value", 0);
        this.value = value;
        this.isConstant = true;
        this.var_id = var_id;
    }    

    public Boolean getValue() {
        return this.value;
    }

    public boolean getIsConst(){
        return this.isConstant;
    }

    public String getVarId() {
        return this.var_id;
    }
    
}
