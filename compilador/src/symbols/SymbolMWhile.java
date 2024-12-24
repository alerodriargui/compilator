package compilador.src.symbols;

public class SymbolMWhile extends SymbolBase {

	private String label;

	public SymbolMWhile(String label) {
		super("Symbol M_While", 0);
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}

}