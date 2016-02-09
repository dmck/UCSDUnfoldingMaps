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

import java.util.HashMap;

import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Visualizes life expectancy in different countries. 
 * 
 * It loads the country shapes from a GeoJSON file via a data reader, and loads the population density values from
 * another CSV file (provided by the World Bank). The data value is encoded to transparency via a simplistic linear
 * mapping.
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

		// Load lifeExpectancy data
		lifeExpMap = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");
		println("Loaded " + lifeExpMap.size() + " data entries");

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		// Country markers are shaded according to life expectancy (only once)
		shadeCountries();
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
		
		int numOfSubreddits = 0;
		for (Marker marker : countryMarkers) {
			List<String> subreddits = new ArrayList<String>();
			java.util.HashMap<String, Object> properties = marker.getProperties();
			for (int i = 0 ; i < lines.length; i++) {
				String countryName = marker.getProperty("name").toString().replaceAll("\\s+","");
				if(lines[i].toLowerCase().contains(countryName.toLowerCase())) {
					numOfSubreddits++;
					// println(lines[i]);
					subreddits.add(lines[i]);
				}
			}
			properties.put("subreddits", subreddits );
			marker.setProperties(properties);
			System.out.println(numOfSubreddits+" - "+marker.getProperty("name").toString());
			//int brightness = numOfSubreddits * 50;
			//
			//marker.setColor(color(brightness,brightness,brightness));
			
			int colorLevel = numOfSubreddits * 64;
			if (colorLevel > 255) {colorLevel = 255;}
			marker.setColor(color(255-colorLevel, 100, colorLevel));
			
			numOfSubreddits = 0;
		}
	}

	//Helper method to load life expectancy data from file
	private HashMap<String, Float> loadLifeExpectancyFromCSV(String fileName) {
		HashMap<String, Float> lifeExpMap = new HashMap<String, Float>();

		String[] rows = loadStrings(fileName);
		for (String row : rows) {
			// Reads country name and population density value from CSV row
			// NOTE: Splitting on just a comma is not a great idea here, because
			// the csv file might have commas in their entries, as this one does.  
			// We do a smarter thing in ParseFeed, but for simplicity, 
			// we just use a comma here, and ignore the fact that the first field is split.
			String[] columns = row.split(",");
			if (columns.length == 6 && !columns[5].equals("..")) {
				lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
			}
		}

		return lifeExpMap;
	}

}
