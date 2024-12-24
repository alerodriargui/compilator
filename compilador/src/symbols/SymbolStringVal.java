package compilador.src.symbols;

public class SymbolStringVal extends SymbolBase {

	private String string;

	public SymbolStringVal(String string) {
		super("Symbol String Value", 0);
		this.string = string;
	}
	
	public String getString(){
		return this.string;
	}
}
