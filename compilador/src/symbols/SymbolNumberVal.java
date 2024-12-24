package compilador.src.symbols;

public class SymbolNumberVal extends SymbolBase{

	private Integer value;

	public SymbolNumberVal(int value) {
		super("Symbol Number Value", 0);
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
}
