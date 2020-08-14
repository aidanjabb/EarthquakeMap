package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 *
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
		
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties "name" (city name), "country" (country name) and "population" (population, in millions)
	}

	public void draw(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	// Show the title of the city 
	// TODO
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		
	}
	
	public String getTitle() {
		return getStringProperty("name");
	}
	
	public String getCountry() {
		return getStringProperty("country");
	}
	
	public float getPopulation() {
		return Float.parseFloat(getStringProperty("population"));
	}

	@Override
	// TODO 
	public void drawMarker(PGraphics pg, float x, float y) {
	}
}