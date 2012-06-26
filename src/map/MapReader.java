package map;

import imageloader.ImageLoader;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import main.GameConstants;
import mapobjects.AnimatedFloor;
import mapobjects.Exit;
import mapobjects.Floor;
import mapobjects.MapObject;
import mapobjects.Player;
import mapobjects.Wall;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import enemies.Ghost;
import enemies.Skeleton;
import enemies.runningEnemy;
import enemies.sillyEnemy;

/**
 * processes the xml file of the map reads graphics and writes them into an
 * ImageLoader loads the map into the MapObjects object
 * 
 * @author eik
 * 
 */
public class MapReader {
	private Document mapXML;
	private Element mapRoot;
	private boolean graphicisloaded = false;
	private String[] moList = { "floor", "animatedfloor", "bomb", "effect",
			"sillyEnemy", "runningEnemy", "skeleton", "ghost", "exit",
			"player", "wall" };
	private int enemies = 0;

	public MapReader(String mn) {

		// read XML map with given name
		try {
			mapXML = new SAXBuilder().build(GameConstants.MAP_FILES_DIR + mn
					+ ".xml");
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mapRoot = mapXML.getRootElement();
	}

	/**
	 * returns headerinformation
	 * 
	 * @param v
	 *            name of the header attribute
	 * @return headerinformation as string
	 */
	public String getHeader(String v) {
		return mapRoot.getChild("header").getChildText(v);
	}

	/**
	 * iterates over the xml document and adds graphics and animations to the
	 * given ImageLoader
	 * 
	 * @param il
	 *            the ImageLoader to add graphics
	 */
	public void loadGraphics(ImageLoader il) {
		List<Element> levels = mapRoot.getChild("mapobjects").getChildren(
				"level");

		for (int i = 0; i < levels.size(); i++) {
			List<Element> tile = levels.get(i).getChildren("tile");
			for (int j = 0; j < tile.size(); j++) {
				if (Boolean.parseBoolean(tile.get(j).getAttributeValue(
						"animated"))) {
					il.addAnimationSet(
							tile.get(j).getChildText("animationset"), "map");
				} else {
					il.addImage(
							GameConstants.MAP_GRAPHICS_DIR
									+ tile.get(j).getChildText("image"),
							Integer.parseInt(tile.get(j).getChild("image")
									.getAttributeValue("rotation")));
				}
			}
		}
		for (int i = 0; i < levels.size(); i++) {
			List<Element> tile = levels.get(i).getChildren("character");
			for (int j = 0; j < tile.size(); j++) {
				il.addAnimationSet(tile.get(j).getChildText("animationset"),
						"characters");

			}
		}

		List<Element> items = mapRoot.getChild("items").getChildren(
				"animationset");
		for (int i = 0; i < items.size(); i++) {
			il.addAnimationSet(items.get(i).getText(), "items");
		}

		items = mapRoot.getChild("items").getChildren("image");
		for (int i = 0; i < items.size(); i++) {
			il.addImage(
					GameConstants.MAP_GRAPHICS_DIR + items.get(i).getText(), 0);
		}

		graphicisloaded = true;
	}

	/**
	 * iterates over the xml dom and adds the objects to the MapObject vector
	 * 
	 * @param mo
	 *            the MapObject vector
	 * @param gr
	 *            the ImageLoader of the Map
	 */

	public void getMap(Vector<Vector<MapObject>> mo, ImageLoader gr) {
		// until graphics have been loaded you can't get the map;
		if (!graphicisloaded) {/* TODO exception */
		} else {
			List<Element> levels = mapRoot.getChild("mapobjects").getChildren(
					"level");
			// run through draw levels
			for (int i = 0; i < levels.size(); i++) {
				mo.add(new Vector<MapObject>());
				// look for diffobjecttypes
				for (int j = 0; j < moList.length; j++) {
					Vector<Element> templist = new Vector<Element>();
					List<Element> tile = levels.get(i).getChildren();
					for (int c = 0; c < tile.size(); c++) {
						if (tile.get(c).getAttributeValue("type")
								.equals(moList[j])) {
							templist.add(tile.get(c));
						}
					}
					for (int c = 0; c < templist.size(); c++) {
						// TODO das ist kacka muss noch anders gemacht werden
						if (moList[j].equals("wall")) {
							mo.get(i).add(
									new Wall(Integer.parseInt(templist.get(c)
											.getChildText("posx")), Integer
											.parseInt(templist.get(c)
													.getChildText("posy")),
											Integer.parseInt(templist
													.get(c)
													.getChild("image")
													.getAttributeValue(
															"rotation")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"image")));
						}
						if (moList[j].equals("floor")) {
							mo.get(i).add(
									new Floor(Integer.parseInt(templist.get(c)
											.getChildText("posx")), Integer
											.parseInt(templist.get(c)
													.getChildText("posy")),
											Integer.parseInt(templist
													.get(c)
													.getChild("image")
													.getAttributeValue(
															"rotation")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"image")));
						}
						if (moList[j].equals("exit")) {
							mo.get(i).add(
									new Exit(Integer.parseInt(templist.get(c)
											.getChildText("posx")), Integer
											.parseInt(templist.get(c)
													.getChildText("posy")),
											Integer.parseInt(templist
													.get(c)
													.getChild("image")
													.getAttributeValue(
															"rotation")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"image")));
						}
						if (moList[j].equals("animatedfloor")) {
							mo.get(i).add(
									new AnimatedFloor(Integer.parseInt(templist
											.get(c).getChildText("posx")),
											Integer.parseInt(templist.get(c)
													.getChildText("posy")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"animationset"), gr));

						}
						if (moList[j].equals("player")) {
							mo.get(i).add(
									new Player(Integer.parseInt(templist.get(c)
											.getChildText("posx")), Integer
											.parseInt(templist.get(c)
													.getChildText("posy")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"animationset"), gr));
						}
						if (moList[j].equals("sillyEnemy")) {
							mo.get(i).add(
									new sillyEnemy(Integer.parseInt(templist
											.get(c).getChildText("posx")),
											Integer.parseInt(templist.get(c)
													.getChildText("posy")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"animationset"), gr));
							enemies++;
						}
						if (moList[j].equals("runningEnemy")) {
							mo.get(i).add(
									new runningEnemy(Integer.parseInt(templist
											.get(c).getChildText("posx")),
											Integer.parseInt(templist.get(c)
													.getChildText("posy")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"animationset"), gr));
							enemies++;
						}
						if (moList[j].equals("skeleton")) {
							mo.get(i).add(
									new Skeleton(Integer.parseInt(templist.get(
											c).getChildText("posx")), Integer
											.parseInt(templist.get(c)
													.getChildText("posy")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"animationset"), gr));
							enemies++;
						}
						if (moList[j].equals("ghost")) {
							mo.get(i).add(
									new Ghost(Integer.parseInt(templist.get(c)
											.getChildText("posx")), Integer
											.parseInt(templist.get(c)
													.getChildText("posy")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"visible")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"destroyable")),
											Boolean.parseBoolean(templist
													.get(c).getChildText(
															"collision")),
											templist.get(c).getChildText(
													"animationset"), gr));
							enemies++;
						}
					}
				}
			}

			// wir gehen VORERST davon aus das die tiles in der richtigen
			// reihenfolge geliefert werden
			// beim adden
		}
	}

	/**
	 * 
	 * @return the number of levels to draw
	 */
	public int getDrawLevels() {
		return mapRoot.getChild("mapobjects").getChildren().size();
	}

	/**
	 * 
	 * @return number of enemies on the map
	 */
	public int getEnemies() {
		return enemies;
	}

}
