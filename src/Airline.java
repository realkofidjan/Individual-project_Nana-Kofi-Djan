
/**
 * 
 * @author 
 *
 * The Airline class to store information about an airline
 */
public class Airline {
	public Airline(int id, String name, String alias, String iATACode, String iCAOCode, String callSign, String country,
				   String active) {
		this.id = id;
		this.country = country;
	}
	
	private int id;
	private String country;

	public int getId() {
		return id;
	}

	public String getCountry() {
		return country;
	}
}
