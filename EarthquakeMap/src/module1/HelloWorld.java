package module1;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google.GoogleMapProvider;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Your name here
  * Date: July 17, 2015
  * */
public class HelloWorld extends PApplet
{
	// Goal: add code to display second map, zoom in, and customize the background.

	// You can ignore this. It's to keep Eclipse from reporting a warning. 
	private static final long serialVersionUID = 1L;

	// Local tiles for working without an Internet connection 
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// If working offline, change the value of this variable to true
	private static final boolean offline = false;
	
	// The map we use to display our hometown: La Jolla, CA 
	UnfoldingMap map1;
	
	// The map you will use to display your hometown  
	UnfoldingMap map2;

	// do everything besides actually display our maps on the canvas
	public void setup() {
	
		// Set Applet window dimensions. Use the Processing library's 2D graphics rendering engine.
		this.size(800, 600, P2D); 
		                   
		// Set Applet background color
		background(200, 200, 200);
	
		// Select map provider
		AbstractMapProvider provider = new GoogleMapProvider();
		
		int zoomLevel = 10;
		
		if (offline) {
			// If you are working offline, you need to use this provider to work with local maps  
			provider = new MBTilesMapProvider(mbTilesString);
			// 3 is the max zoom level for offline work
			zoomLevel = 3;
		}
		
		/*
		 * Create a new UnfoldingMap to be displayed in this window.  
		 * The 2nd-5th arguments give the map's x, y, width, and height.
		 * The 6th argument specifies the map provider.  
		 * There are several providers available.
		 * Note that if you are working offline you must use the MBTilesMapProvider.
		 */
		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);

		// Zoom in and center the map at 32.9 latitude, -117.2 longitude
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		
		// Make the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		
		map2 = new UnfoldingMap(this, 425, 50, 350, 500, provider);
	    map2.zoomAndPanTo(zoomLevel, new Location(42.19f, -87.91f));
		MapUtils.createDefaultEventDispatcher(this, map2);
	}
	
	// Draw the Applet window. 
	public void draw() {
		map1.draw();
		map2.draw();
	} 
}
