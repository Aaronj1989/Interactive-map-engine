package application.model;

public class USState {

	private String name; 
	private String abbr;
	private String flag;
	
	
	
	public USState(String name, String abbr, String flag) {
		this.name = name;
		this.abbr = abbr;
		this.flag = flag;
	}

	public USState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAbbr() {
		return abbr;
	}


	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
}
