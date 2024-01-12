package c209_L13;

public class Participant {

	private String nric;
	private String name;
	private int mobileNum;
	
	public Participant(String nric, String name, int mobileNum) {
		this.nric = nric;
		this.name = name;
		this.mobileNum = mobileNum;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(int mobileNum) {
		this.mobileNum = mobileNum;
	}

	
	
	
	
}
