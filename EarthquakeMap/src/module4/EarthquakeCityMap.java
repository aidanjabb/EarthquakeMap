package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * @author: UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 * Date: July 17, 2015
 * */
@SuppressWarnings("unused")
public class EarthquakeCityMap extends PApplet {
	
	// Get rid of Eclipse warnings
	private static final long serialVersionUID = 1L;

	// If working offline, change value of this variable to true
	private static final boolean offline = false;
	
	// Local tiles for working without an Internet connection 
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// Feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// Files containing city and country names and info 
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	private UnfoldingMap map;
	
	private List<Marker> cityMarkers;
	private List<Marker> quakeMarkers;
	private List<Marker> countryMarkers;
	
	// presumably first func called by main
	public void setup() {		
		// Initialize canvas and map tiles
		size(900, 700, OPENGL);
		
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		} else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
			// If testing with local file, uncomment the next line
		    // earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// For testing, set earthquakesURL to be one of the testing files below
		// earthquakesURL = "test1.atom";
		// earthquakesURL = "test2.atom";
		
		// When taking quiz, uncomment next line
		// earthquakesURL = "quiz1.atom";
		
		// get countries from file
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries); 
		// why don't we need a CountryMarker class? bc all functionality we need for country markers already exists?
		
		// get cities from file
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		// get quakes from feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>(); 
	    for (PointFeature feature : earthquakes) {
		  if (isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  } else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // printQuakes();
	 		
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    // NOTE: Country markers are not added to the map. They are simply used for their geometric properties.
	}  
	
	// presumably second and last func called by main
	public void draw() {
		background(255);
		map.draw();
		addKey();
	} 
	
	// helper func for draw()
	private void addKey() {	
		int topYVal = 50;
		int cityMarkerTextY = topYVal + 50;
		int diameter = 2*CityMarker.TRI_SIZE;
		int circleX = 50;
		int offset = CityMarker.TRI_SIZE;
		
		fill(255, 250, 240);
		rect(25, topYVal, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, topYVal + 25);
		
		text("City Marker", 75, cityMarkerTextY);
		text("Land Quake", 75, cityMarkerTextY + 20);
		text("Ocean Quake", 75, cityMarkerTextY + 40);		 
		text("Size ~ Magnitude", 45, cityMarkerTextY + 65);
		text("Shallow", 75, cityMarkerTextY + 85);
		text("Medium", 75, cityMarkerTextY + 105);
		text("Deep", 75, cityMarkerTextY + 125);
		text("Past Day", 75, cityMarkerTextY + 145);

		/* text("Below " + EarthquakeMarker.THRESHOLD_LIGHT, 75, cityMarkerTextY + 80);
		text(EarthquakeMarker.THRESHOLD_LIGHT + "+ magnitude", 75, cityMarkerTextY + 100);
		text(EarthquakeMarker.THRESHOLD_MODERATE + "+ magnitude", 75, cityMarkerTextY + 120);
		*/
		
		// city marker
		fill(255,165,0);
		triangle(circleX - offset, cityMarkerTextY + offset, circleX, cityMarkerTextY - offset, circleX + offset, cityMarkerTextY + offset);
		
		// land and ocean quakes
		fill(255, 255, 255);
		ellipse(circleX, cityMarkerTextY + 22, diameter, diameter);
		rect(circleX - offset, cityMarkerTextY + 38, 2*offset, 2*offset);
		
		// quake magnitudes
		/* ellipse(50, cityMarkerTextY + 82, 2*CityMarker.TRI_SIZE, 2*CityMarker.TRI_SIZE);
		ellipse(50, cityMarkerTextY + 102, 2*CityMarker.TRI_SIZE, 2*CityMarker.TRI_SIZE);
		ellipse(50, cityMarkerTextY + 122, 2*CityMarker.TRI_SIZE, 2*CityMarker.TRI_SIZE);
		*/

		// quake depths
		fill(255, 255, 0);
		ellipse(circleX, cityMarkerTextY + 87, diameter, diameter);
		fill(0, 0, 255);
		ellipse(circleX, cityMarkerTextY + 107, diameter, diameter);
		fill(255, 0, 0);
		ellipse(circleX, cityMarkerTextY + 127, diameter, diameter);
		
		// quakes in past day
		fill(255, 255, 255);
		ellipse(circleX, cityMarkerTextY + 147, diameter, diameter);		
		line(circleX - diameter/2 - 2, cityMarkerTextY + 147 - diameter/2 - 2, 52 + diameter/2, cityMarkerTextY + 147 + diameter/2 + 2);
		line(circleX - diameter/2 - 2, cityMarkerTextY + 147 + diameter/2 + 2, 52 + diameter/2, cityMarkerTextY + 147 - diameter/2 - 2);
	}
	
	// Helper func for setup(); If true, set "country" property of PointFeature
		private boolean isLand(PointFeature earthquake) {
			
			// For each country marker, check if earthquake is in corresponding country
			for (Marker m : countryMarkers) {
				if (isInCountry(earthquake, m)) {
					return true;
				}
			} 
			return false;
		}
	
	// Helper func for isLand(); If true, add country property to quake features 
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker -> looping over SimplePolygonMarkers which compose them
		if (country.getClass() == MultiMarker.class) {
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if (((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			return true;
		}
		return false;
	} 
	
	/* 
	 * prints quake count as follows:
	 * Country1: numQuakes1
	 * Country2: numQuakes2
	 * ...
	 * OCEAN QUAKES: numOceanQuakes
	 */
	private void printQuakes() {
		Map<String, Integer> quakesByCountry = new TreeMap<>();
		EarthquakeMarker currQuake;
		String country;
		int numOceanQuakes = 0;
		
		for (int i = 0; i < quakeMarkers.size(); i++) {
			currQuake = (EarthquakeMarker) quakeMarkers.get(i);
			if (currQuake.isOnLand()) {
				country = ((LandQuakeMarker)currQuake).getCountry();
				if (quakesByCountry.containsKey(country)) {
					quakesByCountry.put(country, quakesByCountry.get(country) + 1); 
				} else {
					quakesByCountry.put(country, 1); 
				}
			} else {
				numOceanQuakes++;
			}
		}
		
		quakesByCountry.put("OCEAN QUAKES", numOceanQuakes);
		
		for (Map.Entry<String, Integer> entry : quakesByCountry.entrySet()) {
			if (entry.getKey() == "OCEAN QUAKES") {
				continue;
			}
		    System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		System.out.println("OCEAN QUAKES: " + numOceanQuakes);
	}
}