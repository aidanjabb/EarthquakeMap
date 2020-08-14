package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;
import processing.core.PApplet;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 *
 */
public abstract class EarthquakeMarker extends CommonMarker {
	
	protected boolean isOnLand;
	
	protected static final float kmPerMile = 1.6f;
	
	// Greater than or equal to this threshold is a light earthquake 
	public static final float THRESHOLD_LIGHT = 4;
	// Greater than or equal to this threshold is a moderate earthquake 
	public static final float THRESHOLD_MODERATE = 5;
	
	// Greater than or equal to this threshold is an intermediate depth 
	public static final float THRESHOLD_INTERMEDIATE = 70;
	// Greater than or equal to this threshold is a deep depth 
	public static final float THRESHOLD_DEEP = 300;

	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	public EarthquakeMarker (PointFeature feature) {
		super(feature.getLocation());
		java.util.HashMap<String, Object> properties = feature.getProperties();
		setProperties(properties);
	}
	
	// draw earthquake and possibly an X
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
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

	// Show the title of the earthquake 
	// TODO
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		String title = getTitle();
		
		float width = pg.textWidth(title);
		float height = pg.textAscent() + pg.textDescent();
		pg.fill(255, 255, 255);

		pg.rect(x + 10, y - 10, width, height);
		
		// TODO change text color
		pg.fill(0, 0, 0);
		pg.text(title, x, y);
		
		
	}

	// Return radius of "threat circle", which outlines area where this earthquake has an effect  
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
		double km = (miles * kmPerMile);
		return km;
	}
	
	// determine color of marker from depth
	// We use: deep = red, intermediate = blue, shallow = yellow
	private void colorDetermine(PGraphics pg) {
		float depth = getDepth();
		
		if (depth < THRESHOLD_INTERMEDIATE) {
			pg.fill(255, 255, 0);
		} else if (depth < THRESHOLD_DEEP) {
			pg.fill(0, 0, 255);
		} else {
			pg.fill(255, 0, 0);
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
	
	public String getAge() {
		return getProperty("age").toString();
	}
	
	public boolean isOnLand() {
		return isOnLand;
	}
		
	public abstract void markWithX(PGraphics pg, float x, float y);
}