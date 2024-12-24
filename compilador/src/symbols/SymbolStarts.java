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
public class SymbolStarts extends SymbolBase {
    
    public Boolean hasFinished;
    
    public SymbolStarts(boolean hasFinished) {
        super("Start!", 0);
        this.hasFinished = hasFinished;
    }
    
    @Override
    public String toString() {
        return "Arrived to root!!";
    }
}