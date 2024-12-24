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
public class SymbolCallBody extends SymbolBase{

    private String fun_id;
    private String fun_back_id;
    private int num_params;
    
    public SymbolCallBody(String fun_id, String fun_back_id, int num_params) {
        super("Symbol Call Body", 0);
        this.fun_id = fun_id;
        this.fun_back_id = fun_back_id;
        this.num_params = num_params;
    }
    
    public int getNumParams() {
        return this.num_params;
    }

    public String getFunId(){
        return this.fun_id;
    }

    public String getFunBackId(){
        return this.fun_back_id;
    }
}
