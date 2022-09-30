/**
 *
 * @author Nana Kofi Djan
 *
 * The Map class to do the major calculations
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Map {
	private final ArrayList<Airport> listofAirports;
	private final ArrayList<Route> listofroutes;
	
	// the adjacency list
	private HashMap<Airport, ArrayList<Airport>> adjacency;
	
	public Map(ArrayList<Airport> airports, ArrayList<Route> routes) {
		this.listofAirports = airports;
		this.listofroutes = routes;
		createAdjacency();
	}
	
	public Airport findAirportById(int id) {
		for( Airport Node : listofAirports ) {
			if( Node.getId() == id ) {
				return Node;
			}
		}
		return null;
	}

	private void createAdjacency() {
		adjacency = new HashMap<>();
		for( Airport node : listofAirports ) {
			adjacency.put(node, new ArrayList<>());
		}
		for( Route edge : listofroutes ) {
			Airport node = findAirportById(Integer.parseInt(edge.getSourceAirportID()));
			if( node==null ) {
				continue;
			}
			if( findAirportById(Integer.parseInt(edge.getDestinationAirportID())) != null ) {
				adjacency.get(node).add(findAirportById(Integer.parseInt(edge.getDestinationAirportID())));
			}
		}
	}

	public int haversineMethod(Airport source, Airport destination) {
		/*
		 getting the longitude and latitude for the airport in both the start and the destination
		 made use of the Math.toRadians method to convert the latDistance, lonDistance, lat1 and lat2 to radians
		 this calculation was aided by StackOverflow:
		 https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
		*/
		double latDistance = Math.toRadians((destination.getLatitude() - source.getLatitude()));
        double lonDistance = Math.toRadians((destination.getLongitude() - source.getLongitude()));
 
        double lat1 = Math.toRadians((source.getLatitude()));
        double lat2 = Math.toRadians((destination.getLatitude()));
 
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
				* Math.cos(lat1) * Math.cos(lat2);

		// radius of the earth
        double earthRadius = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
		return (int) (earthRadius * c);
	}
	
	private Route getRoute(Airport source, Airport destination) {
		for( Route edge : listofroutes ) {
			if( edge.getSourceAirportID().equals("" + source.getId()) && edge.getDestinationAirportID().equals("" + destination.getId())) {
				return edge;
			}
		}
		return null;
	}
	
	// to find the path between source and destination airport
	public ArrayList<Route> routeTaken(Airport source, Airport destination) {
		ArrayList<Route> route = new ArrayList<>();
		
		// to indicate if a Node is explored
		// all listofAirports are unexplored right now
		HashMap<Airport, Boolean> explored = new HashMap<>();
		// to store shortest distances
		HashMap<Airport, Integer> shortestDistances = new HashMap<>();
		for( Airport Node : listofAirports ) {
			explored.put(Node, false);
			shortestDistances.put(Node, Integer.MAX_VALUE);
		}
		// Storing the parent of each node or airport
		HashMap<Airport, Airport> parent = new HashMap<>();
		shortestDistances.put(source, 0);
		parent.put(source, null);
		
		for( Airport s : listofAirports) {
			
			// finding the nearest node or airport to the parent airport
			Airport nearestNode = null;
			int shortestDistance = Integer.MAX_VALUE;
			for( Airport airport : listofAirports ) {
				if( !explored.get(airport) && shortestDistances.get(airport)<shortestDistance ) { 
					nearestNode = airport;
					shortestDistance = shortestDistances.get(airport);
				}
			}
			
			explored.put(nearestNode, true);
			
			if(null == adjacency.get(nearestNode)) {
				continue;
			}
			
			// updating distances of neighbouring nodes
			for( Airport v : adjacency.get(nearestNode) ) {
				if( (shortestDistance + haversineMethod(nearestNode, v)) < shortestDistances.get(v) ) {
					parent.put(v, nearestNode);
					shortestDistances.put(v, shortestDistance + haversineMethod(nearestNode, v));
				}
			}
		}
		
		Airport curr = destination;
		while( parent.get(curr) != null ) {
			route.add(0, getRoute(parent.get(curr), curr));
			curr = parent.get(curr);
		}
		
		return route;
	}

}
