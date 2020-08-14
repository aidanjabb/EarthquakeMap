package module4;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 *
 */
public class CityMarker extends SimplePointMarker {
	
	// The size of the triangle marker
	public static final int TRI_SIZE = 5;  
	
	public CityMarker(Location location) {
		super(location);
	}
		
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}
	
	public void draw(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		pg.fill(255,165,0);
		pg.triangle(x-TRI_SIZE, y+TRI_SIZE, x, y-TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	public String getCity() {
		return getStringProperty("name");
	}
	
	public String getCountry() {
		return getStringProperty("country");
	}
	
	public float getPopulation() {
		return Float.parseFloat(getStringProperty("population"));
	}
}