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
public class SymbolCallFunction extends SymbolBase{

    private String functionId;
    private String functionBackId;
    
    public SymbolCallFunction(String functionId, String functionBackId) {
        super("Symbol Call Function", 0);
        this.functionId = functionId;
        this.functionBackId = functionBackId;
    }
    
    public String getFunctionId() {
        return this.functionId;
    }

    public String getBackendId() {
        return this.functionBackId;
    }
}
