package compilador.src.symbolsTable;

public class ParamExpansion extends Expansion {

	private int next;
	private String func_id;
	private String param_id; // no codified param_id

	public ParamExpansion(Type type,  String func_id, String id, String param_id, int scope, int next) {
		super(type, id, scope);
		this.next = next;
		this.func_id = func_id;
		this.param_id = param_id;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getNext() {
		return this.next;
	}

	public String getFuncId() {
		return this.func_id;
	}

	public String getParamId() {
		return this.param_id;
	}

	@Override
	public String toString() {
		return  "ID : " + this.getId() + " TIPO: " + this.getType().getTipo() + " TIPO SUBYACENTE: " + this.getType().getTipoSubyacente()
			+ " SCOPE : " + this.getScope() + " FUNCTION : "+this.func_id+" NEXT : "+this.next;
	}
}
