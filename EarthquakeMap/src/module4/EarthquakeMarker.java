package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 */
public abstract class EarthquakeMarker extends SimplePointMarker {	
	protected boolean isOnLand;

	// Set radius in the constructor using either the thresholds below, or a continuous function based on magnitude. 
  
	// Greater than or equal to this threshold is a moderate earthquake 
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;

	// Greater than or equal to this threshold is an intermediate depth 
	public static final float THRESHOLD_INTERMEDIATE = 70;
	public static final float THRESHOLD_DEEP = 300;

	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	public EarthquakeMarker(PointFeature feature) {
		super(feature.getLocation());
		java.util.HashMap<String, Object> properties = feature.getProperties();
		setProperties(properties);
	} 
	
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		colorDetermine(pg);
		
		drawEarthquake(pg, x, y);
		
		String age = getAge();
		if (age.equals("Past Hour") || age.equals("Past Day")) {
			markWithX(pg, x, y);
		}
		// reset to previous styling
		pg.popStyle();
	}
	
	// Determine color of marker from depth 
	// shallow = yellow, intermediate = blue, deep = red
	private void colorDetermine(PGraphics pg) {
		float depth = getDepth();
		if (depth < THRESHOLD_INTERMEDIATE)  { 
			pg.fill(255, 255, 0); // yellow
		} else if (depth < THRESHOLD_DEEP) {
			pg.fill(0, 0, 255); // blue
		} else {
			pg.fill(255, 0, 0); // red
		}
	}
		
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {
		return getProperty("title").toString();	
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public String getAge() {
		return getProperty("age").toString();
	}
	
	public boolean isOnLand() {
		return isOnLand;
	}
	
	public abstract void markWithX(PGraphics pg, float x, float y);
}
