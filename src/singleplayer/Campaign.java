package singleplayer;

import java.util.ArrayList;

/**
 * Stores sequences of map names ( = level) and the corresponding world map. To
 * load a campaign object from an XML file, use a CampaignReader.
 * 
 * @author tohei
 * @see singleplayer.WorldMap
 */
public class Campaign {

	private String campaignName;

	/**
	 * An ArrayList of levels; each level being another ArrayList of
	 * StoryMapContainers
	 */
	private ArrayList<ArrayList<StoryMapContainer>> levels;

	/**
	 * Flag indicating the status of a particular campaign object. If every
	 * level has been activated and completed successfully, this flag will be
	 * set to true.
	 */
	private boolean campaignFinished;
	/**
	 * Keeps track of the maps in a MapSequence.
	 */
	private int mapCounter;
	/**
	 * Every campaign needs a WorldMap to visualize the player's progress.
	 */
	private WorldMap worldMap;

	/**
	 * Construct a new Campaign object from a series of MapSequences and a
	 * WorldMap.
	 * 
	 * @param levels
	 *            list of MapSequences (sequences of map names).
	 * @param worldMap
	 *            a WorldMap object.
	 */
	public Campaign(ArrayList<ArrayList<StoryMapContainer>> levels,
			WorldMap worldMap, String campaignName) {
		if (levels == null || worldMap == null) {
			throw new IllegalArgumentException();
		} else {
			this.levels = levels;
			this.worldMap = worldMap;
			this.campaignName = campaignName;
			campaignFinished = false;
			mapCounter = 0;
		}
	}

	/**
	 * Returns name of map that is being referred to by the world map counters.
	 * 
	 * @return current map name
	 */
	public String getCurrentMap() {
		return levels.get(worldMap.getSelectedLevel()).get(mapCounter)
				.getMapName();
	}

	/**
	 * Returns textual introduction to current map if there is one and if it has
	 * not yet been displayed. Returns null otherwise.
	 * 
	 * @return textual introduction to current map
	 */
	public String[] getIntroToCurrentMap() {
		StoryMapContainer currentMap = levels.get(worldMap.getSelectedLevel())
				.get(mapCounter);

		if (!currentMap.introWasShown()) {
			currentMap.setIntroShown(true);
			return currentMap.getIntroMessage();
		}
		return null;
	}

	public WorldMap getWorldMap() {
		return worldMap;
	}

	/**
	 * Called by the LevelManager. Returns boolean value: <br>
	 * <ul>
	 * <LI>true - map counter has been successfully incremented</LI>
	 * <LI>false - map counter could not be incremented => there were no more
	 * maps left in this particular level</LI>
	 * </ul>
	 * 
	 * As long as there is a level remaining, update level progress, otherwise
	 * turn campaignFinished to true.<br>
	 * <br>
	 * 
	 * Providing these return values, the LevelManager is able to decide whether
	 * to ask for the next map or to initiate the WorldMap.
	 */
	public boolean updateCounters() {
		if (mapCounter < levels.get(worldMap.getSelectedLevel()).size() - 1) {
			mapCounter++;
			return true;
		} else {
			mapCounter = 0;
			if (worldMap.getSelectedLevel() == worldMap.getMaxLevelAccessible()) {
				if (worldMap.getMaxLevelAccessible() == levels.size() - 1) {
					campaignFinished = true;
				} else {
					worldMap.setMaxLevelAccessible(worldMap
							.getMaxLevelAccessible() + 1);
				}
			}
			return false;
		}
	}

	public void setMapCounter(int counter) {
		this.mapCounter = counter;
	}

	/**
	 * Check for campaign status.
	 * 
	 * @return campaignFinished flag
	 */
	public boolean isFinished() {
		return campaignFinished;
	}

	/**
	 * Get relevant data for saving.
	 * 
	 * @return
	 */
	public CampaignData getCampaignData() {
		return CampaignData.extractData(this);
	}

	/**
	 * Restore campaign to a previously stored state.
	 * 
	 * @param data
	 */
	public void restoreCampaignToData(CampaignData data) {
		data.restoreCampaign(this);
	}

	public String getName() {
		return campaignName;
	}

	/**
	 * Simple Container class storing a map name and an introductory text
	 * message. Does also contain a boolean variable that may be used as a flag
	 * to determine if the intro has been displayed yet.
	 * 
	 * @author tohei
	 * 
	 */
	public static class StoryMapContainer {
		private String mapName;
		private String[] introMessage;
		private boolean introShown;

		public StoryMapContainer(String mapName, String[] introMessage) {
			this.mapName = mapName;
			this.introMessage = introMessage;
			introShown = false;
		}

		public String getMapName() {
			return mapName;
		}

		public String[] getIntroMessage() {
			return introMessage;
		}

		public void setIntroShown(boolean introShown) {
			this.introShown = introShown;
		}

		public boolean introWasShown() {
			return introShown;
		}

	}

	/**
	 * Data container for all important campaign status variables. Used to
	 * create Savegames.
	 * 
	 * @author tohei
	 * 
	 */
	public static class CampaignData {

		private static final int id = 0;
		private int selectedLevel;
		private int maxLevelAccessible;
		private String campaignName;
		private int numOfLevels;

		private CampaignData(int selectedLevel, int maxLevelAccessible,
				String campaignName, int numOfLevels) {
			this.selectedLevel = selectedLevel;
			this.maxLevelAccessible = maxLevelAccessible;
			this.campaignName = campaignName;
			this.numOfLevels = numOfLevels;
		}

		public static CampaignData extractData(Campaign campaign) {
			return new CampaignData(campaign.getWorldMap().getSelectedLevel(),
					campaign.getWorldMap().getMaxLevelAccessible(),
					campaign.getName(), campaign.getWorldMap().getNumOfLevels());
		}

		public void restoreCampaign(Campaign campaign) {
			campaign.getWorldMap().setMaxLevelAccessible(maxLevelAccessible);
			campaign.getWorldMap().setSelectedLevel(selectedLevel);
		}

		public static CampaignData extractDataFromString(String input) {
			String[] inputData = input.split(";");
			int sLevel = 0, mLevel = 0, lnum = 0;
			String name = null;
			for (int i = 1; i < inputData.length; i++) {
				String[] data = inputData[i].split("=");
				if (data[0].equals("sl")) {
					sLevel = Integer.parseInt(data[1]);
				}
				if (data[0].equals("ml")) {
					mLevel = Integer.parseInt(data[1]);
				}
				if (data[0].equals("name")) {
					name = data[1];
				}
				if (data[0].equals("lnum")) {
					lnum = Integer.parseInt(data[1]);
				}
			}
			return new CampaignData(sLevel, mLevel, name, lnum);
		}

		public String writeDataToString() {
			StringBuilder sb = new StringBuilder();
			sb.append("campaign_data");
			sb.append(";id=").append(id);
			sb.append(";sl=").append(selectedLevel);
			sb.append(";ml=").append(maxLevelAccessible);
			sb.append(";name=").append(campaignName);
			sb.append(";lnum=").append(numOfLevels);
			return sb.toString();
		}

		public int getSelectedLevel() {
			return selectedLevel;
		}

		public int getMaxLevelAccessible() {
			return maxLevelAccessible;
		}

		public int getNumOfLevels() {
			return numOfLevels;
		}

		public String getCampaignName() {
			return campaignName;
		}
	}

}
