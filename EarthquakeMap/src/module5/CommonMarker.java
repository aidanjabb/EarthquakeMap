package module5;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Aidan Jabbarzadeh
 *
 */
public abstract class CommonMarker extends SimplePointMarker {

	// Records whether this marker is the most recently clicked one
	protected boolean clicked = false;
	
	public CommonMarker(Location location) {
		super(location);
	}
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	
	public boolean getClicked() {
		return clicked;
	}
	
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	// called every time map needs to be re-rendered
	// where is this called?
	public void draw(PGraphics pg, float x, float y) {
		// For starter code, just drawMarker(...)
		if (!hidden) {
			drawMarker(pg, x, y);
			if (isSelected()) {
				if (this instanceof EarthquakeMarker) {
					System.out.println("selected: " + ((EarthquakeMarker) this).getTitle());
				} 
				showTitle(pg, x, y);  
			}
		}
	}
	
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
	public abstract String getTitle();
}