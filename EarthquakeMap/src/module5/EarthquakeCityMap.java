package module5;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * @author: UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 * Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {
	
	// Get rid of Eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// local tiles
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	private UnfoldingMap map;
	
	private List<Marker> cityMarkers;
	private List<Marker> quakeMarkers;
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker currentHover; // marker that is currently being currentHovered over
	@SuppressWarnings("unused")
	private CommonMarker lastClicked;
	
	public void setup() {		
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; // The same feed, but saved August 7, 2015
		} else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
			// If testing with local file, uncomment the next line
		    // earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		// each city cannot be represented by single pt on map, therefore Feature, not PointFeature
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		// each earthquake can be represented by single pt on map, therefore PointFeature
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL); 
	    quakeMarkers = new ArrayList<Marker>();
	    for(PointFeature feature : earthquakes) {
		  if (isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  } else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // use next line for debugging
	    // printQuakes();
	 		
	    // Note: Country markers are not added to the map. They are used for their geometric properties.
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers); 
	}  
	
	// where is this called?
	public void draw() {
		background(0);
		map.draw();
		addKey();		
	}
	
	// Event handler that gets called automatically when the mouse moves.
	@Override
	public void mouseMoved() {
		// clear the last selection 
		if (currentHover != null) {
			currentHover.setSelected(false);s
			currentHover = null;
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
	}
	
	// called by mouseMoved()
	// select a marker at random from under the cursor  
	private void selectMarkerIfHover(List<Marker> markers) {
		for (Marker marker : markers) {
			if (marker.isInside(map, mouseX, mouseY)) {
				currentHover = (CommonMarker) marker; // typecast is necessary in order to call setSelected
				currentHover.setSelected(true); 
				break;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}	
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, ybase+70, 10, 10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);			
	}

	private boolean isLand(PointFeature earthquake) {		
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				// TODO set "country" property of quake, remove that functionality from isInCountry method
				return true;
			}
		}		
		return false;
	}
	
	// prints countries with number of earthquakes
	@SuppressWarnings("unused")
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers) {
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		Location loc = earthquake.getLocation();

		// some countries are represented as MultiMarkers
		// looping over SimplePolygonMarkers which compose them to use isInsideByLoc
		if (country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for (Marker marker : ((MultiMarker)country).getMarkers()) {
					
				if (((AbstractShapeMarker)marker).isInsideByLocation(loc)) {
					earthquake.addProperty("country", country.getProperty("name"));	
					return true;
				}
			}
		} 
		// check if inside country represented by SimplePolygonMarker
		else if (((AbstractShapeMarker)country).isInsideByLocation(loc)) { 
			earthquake.addProperty("country", country.getProperty("name"));
			return true;
		}
		return false;
	}
}