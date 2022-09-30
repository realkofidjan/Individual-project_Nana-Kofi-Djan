/**
 *
 * @author Nana Kofi Djan
 * The Main class
 * AP is airport
 * RT is route
 * AIR is airline
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {



	// initializing the required files to string variables.
	public static final String airportDataset = "airports.csv";
	public static final String airlineDataset = "airlines.csv";
	public static final String routeDataset = "routes.csv";

	public static void main(String[] args) {
		// Getting the start time of the execution
		float startTime = System.nanoTime();

		// String variable to hold the name of the input file.
		String inputFileName = "input.txt";

		// Creating an Array list for each file read
		ArrayList<Airport> airports = airportCSV();
		airlinesCSV();
		ArrayList<Route> routes = routesCSV();

		// try to read city names from the file
		try {
			Scanner scanner = new Scanner(new File(inputFileName));
			// get output file name
			String outputFileName = getOutputFileName(inputFileName);
			PrintWriter printWriter = new PrintWriter(outputFileName);

			// read start location and destination location
			String startLocation = scanner.nextLine();
			String destinationLocation = scanner.nextLine();
			scanner.close();

			// Code to find an airport
			Airport sourceAirport = airportLookup(startLocation, airports);
			Airport destinationAirport = airportLookup(destinationLocation, airports);

			// if the start location is null it would print the following text
			if( sourceAirport == null ) {
				printWriter.println("There is not an airport in " + startLocation);
			}
			// if the destination location is null it would print the following text
			else if( destinationAirport == null ) {
				printWriter.println("Cannot find you destination Airport: " + destinationLocation);
			}
			else {
				// if both the start and the destination are not null, the route taken would be printed to the output file
				Map Map = new Map(airports, routes);
				// find path
				ArrayList<Route> path = Map.routeTaken(sourceAirport, destinationAirport);
				// print the route of the flights.
				int numAdditionalStops = 0;
				int i=0;
				int totalDistance = 0;
				for( Route route : path ) {
					printWriter.println("\t" + (++i) + ". " + route.getAirlineCode() + " from " + route.getSourceAirportCode() + " to " + route.getDestinationAirportCode() + " " + route.getStops() + " stops.");
					numAdditionalStops += route.getStops();
					totalDistance += Map.haversineMethod( Map.findAirportById(Integer.parseInt(route.getSourceAirportID())),
							Map.findAirportById(Integer.parseInt(route.getDestinationAirportID())));
				}

				/*
				 Calculation of program's execution time
				 The calculation was aided by JavaVisited
				 https://javarevisited.blogspot.com/2012/04/how-to-measure-elapsed-execution-time.html#axzz7gO0L4DJI
				*/
				float elapsedTime = System.nanoTime() - startTime;
				elapsedTime = elapsedTime / 1000000;


				// Extra credit option
				printWriter.println("Total flights: " + i);
				printWriter.println("Total additional stops: " + numAdditionalStops);
				printWriter.println("Total distance: " + totalDistance + "km.");
				printWriter.println("Execution time: " + elapsedTime + "ms.");
			}

			printWriter.close();

			System.out.print("Your output file has been written.\n Please check the file with the name: " + outputFileName);

		}
		catch(FileNotFoundException fnfe) {
			System.out.println("This file is not found.");
		}
	}

	// Method to help read airport data from file
	// The methods; airlineCSV and routeCSV follow the same procedure, so some comments available in airportCSV will not be available in other methods.
	private static ArrayList<Airport> airportCSV() {
		
		try {
			ArrayList<Airport> airports = new ArrayList<>();
			Scanner scanner = new Scanner(new File(airportDataset));
			while( scanner.hasNextLine() ) {
				// reading each line in the airport file
				String line = scanner.nextLine();
				// extracting the data from each line and placing them in the string AP
				String[] AP = line.split(",");

				// This code check the length of each line.
				// If the length of the line is not equal to 14, then there is some invalid data
				// If there is invalid data, skip past it because the reason for that data will be unknown and probably a typo.
				if( AP.length != 14 ) {
					continue;
				}
				// if any of the AP lines has this character: "\N", skip this row
				boolean invalidData = false;
				for (String s : AP) {
					if (s.equals("\\N")) {
						invalidData = true;
						break;
					}
				}
				if( invalidData ) {
					continue;
				}
				// Appending the list of airports with every airport that has only valid data
				Airport airport = new Airport(Integer.parseInt(AP[0]), AP[1], AP[2], AP[3], AP[4], AP[5],
						Double.parseDouble(AP[6]), Double.parseDouble(AP[7]), Double.parseDouble(AP[8]), Double.parseDouble(AP[9]),
						AP[10], AP[11], AP[12], AP[13]);
				airports.add(airport);
			}
			//System.out.println(airports);
			scanner.close();
			return airports;
		}
		catch(FileNotFoundException fnfe) {
			System.out.println("Cannot open input file " + airportDataset);
			return null;
		}
	}
	
	// the helper method to read airline data from file
	private static void airlinesCSV() {
		
		try {
			ArrayList<Airline> airlines = new ArrayList<>();
			Scanner scanner = new Scanner(new File(airlineDataset));
			while( scanner.hasNextLine() ) {
				String line = scanner.nextLine();
				String[] AIR = line.split(",");

				// This code check the length of each line.
				// If the length of the line is not equal to 8, then there is some invalid data
				// If there is invalid data, skip past it because the reason for that data will be unknown and probably a typo.
				if( AIR.length != 8 ) {
					continue;
				}
				// if any of the AIR lines has this character: "\N", skip this row
				boolean invalidData = false;
				for (String s : AIR) {
					if (s.equals("\\N")) {
						invalidData = true;
						break;
					}
				}
				if( invalidData ) {
					continue;
				}
				// creating a new airline with the specific columns needed
				Airline airline = new Airline(Integer.parseInt(AIR[0]), AIR[1], AIR[2], AIR[3], AIR[4], AIR[5],
						AIR[6], AIR[7]);
				airlines.add(airline);
			}
			scanner.close();
		}
		catch(FileNotFoundException fnfe) {
			System.out.println("Cannot open input file " + airlineDataset);
		}
	}
	
	// the helper method to read route data from file
	private static ArrayList<Route> routesCSV() {
		
		try {
			ArrayList<Route> routes = new ArrayList<>();
			Scanner scanner = new Scanner(new File(routeDataset));
			while( scanner.hasNextLine() ) {
				String line = scanner.nextLine();
				String[] RT = line.split(",");

				// This code check the length of each line.
				// If the length of the line is not equal to 9, then there is some invalid data
				// If there is invalid data, skip past it because the reason for that data will be unknown and probably a typo.
				if( RT.length != 9 ) {
					continue;
				}

				// if any of the RT lines has this character: "\N", skip this row
				boolean invalidData = false;
				for (String s : RT) {
					if (s.equals("\\N")) {
						invalidData = true;
						break;
					}
				}
				if( invalidData ) {
					continue;
				}
				// creating a new route with the specific needed columns
				Route route = new Route(RT[0], RT[1], RT[2], RT[3], RT[4], RT[5],
						RT[6], Integer.parseInt(RT[7]), RT[8]);
				routes.add(route);
			}
			scanner.close();
			return routes;
		}
		catch(FileNotFoundException fnfe) {
			System.out.println("Cannot open input file " + routeDataset);
			return null;
		}
	}
	
	// method to use the input file to get the name of the output file.
	private static String getOutputFileName(String inputFileName) {
		String name = inputFileName.substring(0, inputFileName.indexOf('.'));
		return name + "_output.txt";
	}
	
	// extracting the value of the city and country of a specific airport.
	private static Airport airportLookup(String s, ArrayList<Airport> airports) {
		String city = s.substring(0, s.indexOf(','));
		String country = s.substring(s.indexOf(',') + 1).trim();

		// code come comparing the read city and country with the input file city and country.
		// using just the equals() method will not ignore cases so the equalsIgnoreCase() method was implemented.
		for( Airport airport : airports )
			if (airport.getCity().equalsIgnoreCase(city) && airport.getCountry().equalsIgnoreCase(country)) {
				return airport;
			}
		return null;
	}

}
