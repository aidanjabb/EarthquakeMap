package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	private final float length;
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);		
		float magnitude = getMagnitude();
		length = 1.5f*magnitude;
		isOnLand = false;
	}
	
	// Draw the earthquake as a square 
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
	}
	
	public float getLength() {
		return length;
	}
	
	public void markWithX(PGraphics pg, float x, float y) {
		pg.fill(0, 0, 0);
		pg.line(x - length/2 - 2, y - length/2 - 2, x + length/2 + 2, y + length/2 + 2);
		pg.line(x - length/2 - 2, y + length/2 + 2, x + length/2 + 2, y - length/2 - 2);
	}
}