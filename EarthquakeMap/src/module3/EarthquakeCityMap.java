package module3;

// Java utilities libraries
import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
import java.util.List;

// Processing library
import processing.core.PApplet;

// Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

// Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 * Date: July 17, 2015
 */
@SuppressWarnings("unused")
public class EarthquakeCityMap extends PApplet {

	// Ignore. Suppresses Eclipse warning.
	private static final long serialVersionUID = 1L;

	// If working offline, change the value of this variable to true.
	private static final boolean offline = false;
	
	// Below this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Below this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	// Local tiles for working without an Internet connection 
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	private UnfoldingMap map;
	
	// feed with magnitude 2.5+ earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	public void setup() {
		
		// Set Applet window dimensions. Use the Processing library's 2D graphics rendering engine.
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		} else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new OpenStreetMap.OpenStreetMapProvider());
			// To test with a local file, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    
		// Make the map interactive
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    List<Marker> markers = new ArrayList<Marker>();

	    // Get certain properties of the earthquakes
	    // PointFeatures store location and other properties
	    // If new earthquakes are added to feed, setup must be called again to load them
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
		for (PointFeature feature : earthquakes) {
			SimplePointMarker marker = createMarker(feature);
			markers.add(marker);
		}
	    
	    map.addMarkers(markers);
	}
		
	// Helper method that takes in an earthquake feature and returns a SimplePointMarker (which can be added to the map)
	private SimplePointMarker createMarker(PointFeature feature) {  
		// System.out.println(feature.getProperties());
		
		// we only need a few of the properties stored in the PointFeature
		
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation()); 
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
				
	    int red = color(255, 0, 0);
	    int green = color(0, 255, 0);
	    int blue = color(0, 0, 255);
		
		if (mag < THRESHOLD_LIGHT) {
			marker.setRadius(3);
			marker.setColor(green);
		} else if (mag < THRESHOLD_MODERATE) {
			marker.setRadius(5);
			marker.setColor(blue);
		} else {
			marker.setRadius(7);
			marker.setColor(red);
		}
				
	    return marker;
	}
	
	// call in order to update canvas
	public void draw() {
		// Set Applet background color
	    background(10); // why not call this just once in setup?
	    map.draw(); // update and draw map
	    addKey();
	}

	private void addKey() {	
		int rectX = 20;
		int rectY = 20;
		int rectWidth = 150;
		int rectHeight = 120;
		
		fill(255, 253, 208);
		rect(rectX, rectY, rectWidth, rectHeight);
				
		fill(0);
		text("Key", rectX + rectWidth/2 - 5, rectY + 15);
		text("Below " + THRESHOLD_LIGHT, rectX + rectWidth/2 - 25, rectY + 50);
		text(THRESHOLD_LIGHT + "+ magnitude", rectX + rectWidth/2 - 25, rectY + 70);
		text(THRESHOLD_MODERATE + "+ magnitude", rectX + rectWidth/2 - 25, rectY + 90);
				
		int red = color(255, 0, 0);
	    int green = color(0, 255, 0);
	    int blue = color(0, 0, 255);
	    
	    fill(green);
	    ellipse(rectX + rectWidth/2 - 35, rectY + 45, 6, 6);
	    fill(blue);
	    ellipse(rectX + rectWidth/2 - 35, rectY + 65, 10, 10);
	    fill(red);
	    ellipse(rectX + rectWidth/2 - 35, rectY + 85, 14, 14);
	}
}
