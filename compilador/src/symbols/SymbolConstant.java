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
public class SymbolConstant extends SymbolBase{

    private boolean isConst;
    
    public SymbolConstant(boolean isConst) {
        super("Symbol Constant Value", 0);
        this.isConst = isConst;
    }

    public boolean getIsConst() {
        return this.isConst;
    }
}
