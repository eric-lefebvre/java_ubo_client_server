
public class DataToSend {
	private TypeMessage type;
	private String data;
	
	//temporaire : à voir comment ajouter les paramètres
	public DataToSend(TypeMessage type, String data) {
		this.type = type;
		this.data = data;
	}
	public DataToSend() {
		
	}

	public DataToSend(TypeMessage clear) {
		this.type = clear;
	}
	public TypeMessage getType() {
		return type;
	}

	public void setType(TypeMessage type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


}
