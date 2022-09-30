
/**
 * 
 * @author Nana Kofi Djan
 *
 * The Airport class to store information about an airport
 */
public class Airport {
	public Airport(int id, String name, String city, String country, String iATACode, String iCAOCode, double latitude,
				   double longitude, double altitude, double timezone, String dST, String tzDBTimezone, String type,
				   String dataSource) {
		this.id = id;
		this.city = city;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private int id;
	private String city;
	private String country;
	private double latitude;
	private double longitude;

	/*
	 * Getters for necessary columns
	 */
	public int getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
}
