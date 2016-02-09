package demos;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.providers.Google.*;

import java.util.List;
import java.util.ArrayList;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;

import java.util.HashMap;

import module5.CommonMarker;
import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Visualizes which countries have their name in the the name of a subreddit. 
 * 
 * Thanks to /u/elasto for the list of 10,000 subreddits
 * https://www.reddit.com/r/findareddit/comments/wnoc2/i_have_a_csv_file_of_the_top_10000_subreddits_for/
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Daniel McKnight
 */
public class SubredditDistribution extends PApplet {

	UnfoldingMap map;
	HashMap<String, Float> lifeExpMap;
	List<Feature> countries;
	List<Marker> countryMarkers;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		// Country markers are shaded according to life expectancy (only once)
		shadeCountries();
	}
	
	@Override
	public void mouseClicked()
	{
		for(Marker marker : countryMarkers) {		
			Location markerLocation = marker.getLocation();
			// checking if inside
			if(marker.isInside(map, mouseX, mouseY)) {
				System.out.println("======================================");
		    	System.out.println(marker.getProperty("name"));
		    	System.out.println("======================================");
				List<String> subreddits = (List<String>) marker.getProperty("subreddits");
			    if(subreddits.isEmpty()) {
			    	System.out.println("None.");
			    } else {
					for(String subreddit : subreddits) {
				    	System.out.println("/r/"+subreddit);
				    }
			    }
			}
		}
	}
	
	public void draw() {
		// Draw map tiles and country markers
		map.draw();
	}

	//Helper method to color each country based on life expectancy
	//Red-orange indicates low (near 40)
	//Blue indicates high (near 100)
	private void shadeCountries() {
		
		// Load subreddit names
		String[] lines = loadStrings("subreddits.csv");
		println("there are " + lines.length + " lines");
		
		// init subreddit counter
		int numOfSubreddits = 0;
		for (Marker marker : countryMarkers) {
			// init list of subreddits for this country
			List<String> subreddits = new ArrayList<String>();
			java.util.HashMap<String, Object> properties = marker.getProperties();
			//for each subreddit
			for (int i = 0 ; i < lines.length; i++) {
				// get this marker's country name and remove spaces
				String countryName = marker.getProperty("name").toString().replaceAll("\\s+","");
				//if the country name is inside the subreddit name
				if(lines[i].toLowerCase().contains(countryName.toLowerCase())) {
					// println(lines[i]);
					// then increase count and add to list of subreddits
					numOfSubreddits++;
					subreddits.add(lines[i]);
				}
			}
			// add list of subreddits to marker's properties
			properties.put("subreddits", subreddits );
			marker.setProperties(properties);
			
			// set country color
			int colorLevel = numOfSubreddits * 64;
			if (colorLevel > 255) {colorLevel = 255;}
			if (numOfSubreddits > 0) {
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			} else {marker.setColor(color(100,100,100));}
			
			//reset count for next country
			numOfSubreddits = 0;
		}
	}

}
