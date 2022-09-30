
/**
 * 
 * @author Nana Kofi Djan
 *
 * The Route class to store information about an airline's route
 */
public class Route {
	public Route(String airlineCode, String airlineID, String sourceAirportCode, String sourceAirportID,
				 String destinationAirportCode, String destinationAirportID, String codeShare, int stops, String equipment) {
		this.airlineCode = airlineCode;
		this.sourceAirportCode = sourceAirportCode;
		this.sourceAirportID = sourceAirportID;
		this.destinationAirportCode = destinationAirportCode;
		this.destinationAirportID = destinationAirportID;
		this.stops = stops;
	}

	private String airlineCode;
	private String sourceAirportCode;
	private String sourceAirportID;
	private String destinationAirportCode;
	private String destinationAirportID;
	private int stops;

	public String getAirlineCode() {
		return airlineCode;
	}

	public String getSourceAirportCode() {
		return sourceAirportCode;
	}

	public String getSourceAirportID() {
		return sourceAirportID;
	}

	public String getDestinationAirportCode() {
		return destinationAirportCode;
	}

	public String getDestinationAirportID() {
		return destinationAirportID;
	}

	public int getStops() {
		return stops;
	}
}
