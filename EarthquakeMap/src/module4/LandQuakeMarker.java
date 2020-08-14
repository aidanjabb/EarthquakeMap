package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for land earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 *
 */
public class LandQuakeMarker extends EarthquakeMarker {
	
	public LandQuakeMarker(PointFeature quake) {		
		super(quake);
		float magnitude = getMagnitude(); 
		radius = 1.5f*magnitude;
		/* if (magnitude > THRESHOLD_LIGHT && magnitude < THRESHOLD_MODERATE) {
			radius = .75f*1.75f*magnitude;
		} else if (magnitude > THRESHOLD_MODERATE) {
			radius = 1.75f*magnitude;
		} else {
			radius = .5f*1.75f*magnitude; 
		} */
		isOnLand = true;
	}

	// Draw a centered circle.
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.ellipse(x, y, 2*radius, 2*radius);
	}
	
	public void markWithX(PGraphics pg, float x, float y) {
		pg.fill(0, 0, 0);
		pg.line(x - radius - 2, y - radius - 2, x + radius + 2, y + radius + 2);
		pg.line(x - radius - 2, y + radius + 2, x + radius + 2, y - radius - 2);
	} 

	public String getCountry() {
		return (String) getProperty("country");
	}		
}