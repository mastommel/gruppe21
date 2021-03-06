package multiplayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GameConstants;
import main.GraphicalGameUnit;
import main.UnitNavigator;
import main.UnitState;
import map.Map;
import mapobjects.Player;
import unitTransitions.TransitionUnit;

/**
 * Subclass of GraphicalGameUnit for simple "Two-Players-One-Keyboard"
 * multiplayer games.
 * 
 * @author tohei
 * 
 */
public class LocalMultiplayerUnit extends GraphicalGameUnit {

	private Player playerOne;
	private Player playerTwo;

	private Map multiplayerMap;
	private BufferedImage mapCanvas;

	private int mapCanvasPosX = 0;
	private int mapCanvasPosY = 0;

	private String mapName = "MP-Woodwars";

	/**
	 * Some win messages one can randomly choose from.
	 */
	private final String[] winMessages = { " is bomb-happy!",
			" achieves a blasting victory!", " leaves nothing but a crater!" };
	/**
	 * Message to be shown in case of a draw.
	 */
	private final String drawMessage = "Unbelieveable! It's a draw!";

	public LocalMultiplayerUnit() {
		initComponent();
	}

	public LocalMultiplayerUnit(String mapName) {
		this.mapName = mapName;
		initComponent();
	}

	@Override
	public void updateComponent() {
		if (!multiplayerMap.isFinished()) {
			multiplayerMap.update();
		} else {
			/*
			 * A player died: Generate the appropriate image ...
			 */
			BufferedImage transitionMsg = createTransitionMessage(drawMessage);
			if (playerOne.isAlive()) {
				transitionMsg = createTransitionMessage("Player One"
						+ winMessages[(int) (Math.random() * 3)]);
			} else if (playerTwo.isAlive()) {
				transitionMsg = createTransitionMessage("Player Two"
						+ winMessages[(int) (Math.random() * 3)]);
			}
			/*
			 * ... and pass it to a TransitionUnit
			 */
			TransitionUnit trans = new TransitionUnit(UnitState.BASE_MENU_UNIT,
					transitionMsg, false);
			UnitNavigator.getNavigator().removeGameUnit(
					UnitState.LEVEL_MANAGER_UNIT);
			UnitNavigator.getNavigator().addGameUnit(trans,
					UnitState.TEMPORARY_UNIT);
			UnitNavigator.getNavigator().set(UnitState.TEMPORARY_UNIT);
		}

	}

	@Override
	public void handleKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		/*
		 * Pause Game
		 */
		if (key == KeyEvent.VK_ESCAPE) {
			UnitNavigator.getNavigator().set(UnitState.BASE_MENU_UNIT);
		}
		/*
		 * playerOne KeyEvents
		 */
		if (key == KeyEvent.VK_W) {
			playerOne.direction.setUp(true);
		}

		if (key == KeyEvent.VK_S) {
			playerOne.direction.setDown(true);
		}

		if (key == KeyEvent.VK_A) {
			playerOne.direction.setLeft(true);
		}

		if (key == KeyEvent.VK_D) {
			playerOne.direction.setRight(true);
		}

		if (key == KeyEvent.VK_SPACE) {
			playerOne.plantBomb(multiplayerMap.getCollisionMap());
		}
		/*
		 * playerTwo KeyEvents
		 */
		if (key == KeyEvent.VK_UP) {
			playerTwo.direction.setUp(true);
		}

		if (key == KeyEvent.VK_DOWN) {
			playerTwo.direction.setDown(true);
		}

		if (key == KeyEvent.VK_LEFT) {
			playerTwo.direction.setLeft(true);
		}

		if (key == KeyEvent.VK_RIGHT) {
			playerTwo.direction.setRight(true);
		}

		if (key == KeyEvent.VK_PLUS) {
			playerTwo.plantBomb(multiplayerMap.getCollisionMap());
		}
	}

	@Override
	public void handleKeyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		/*
		 * playerOne KeyEvents
		 */
		if (key == KeyEvent.VK_W) {
			playerOne.direction.setUp(false);
		}

		if (key == KeyEvent.VK_S) {
			playerOne.direction.setDown(false);
		}

		if (key == KeyEvent.VK_A) {
			playerOne.direction.setLeft(false);
		}

		if (key == KeyEvent.VK_D) {
			playerOne.direction.setRight(false);
		}
		/*
		 * playerTwo KeyEvents
		 */
		if (key == KeyEvent.VK_UP) {
			playerTwo.direction.setUp(false);
		}

		if (key == KeyEvent.VK_DOWN) {
			playerTwo.direction.setDown(false);
		}

		if (key == KeyEvent.VK_LEFT) {
			playerTwo.direction.setLeft(false);
		}

		if (key == KeyEvent.VK_RIGHT) {
			playerTwo.direction.setRight(false);
		}
	}

	@Override
	public void initComponent() {
		/*
		 * load map and players
		 */
		multiplayerMap = new Map(mapName);
		int[] attendingPlayers = new int[] { MPLoungeUnit.PLAYER_READY,
				MPLoungeUnit.PLAYER_READY, MPLoungeUnit.PLAYER_UNAVAILABLE,
				MPLoungeUnit.PLAYER_UNAVAILABLE };
		multiplayerMap.removeUnattendedPlayers(attendingPlayers);
		multiplayerMap.setMaxUpgrades(0);
		playerOne = multiplayerMap.getPlayerByNumber(1);
		playerTwo = multiplayerMap.getPlayerByNumber(2);
		/*
		 * calculate mapCanvas position on panel
		 */
		mapCanvasPosX = (GameConstants.FRAME_SIZE_X - multiplayerMap.getWidth()) / 2;
		mapCanvasPosY = (GameConstants.FRAME_SIZE_Y - multiplayerMap
				.getHeight()) / 2;
		/*
		 * create mapCanvas (used to depict and center the map)
		 */
		mapCanvas = new BufferedImage(multiplayerMap.getWidth(),
				multiplayerMap.getHeight(), BufferedImage.TYPE_INT_ARGB);
		/*
		 * load font
		 */
		try {
			unitFont = loadFont("font1.TTF").deriveFont(50f);
		} catch (Exception e) {
			System.err.println("ERROR LOADING FONT: font1.TTF");
			e.printStackTrace();
			unitFont = new Font("serif", Font.PLAIN, 24);
		}
	}

	@Override
	public void drawComponent(Graphics g) {
		/*
		 * Black background color
		 */
		g.setColor(Color.black);
		g.fillRect(0, 0, GameConstants.FRAME_SIZE_X, GameConstants.FRAME_SIZE_Y);
		/*
		 * draw map onto the mapcanvas
		 */
		multiplayerMap.drawMap((Graphics2D) mapCanvas.getGraphics());
		/*
		 * center mapcanvas on panel by using the Graphics parameter
		 */
		g.drawImage(mapCanvas, mapCanvasPosX, mapCanvasPosY,
				mapCanvas.getWidth(), mapCanvas.getHeight(), null);
	}

	/**
	 * Generates a BufferedImage to be passed to a TransitionUnit.
	 * 
	 * @return BufferedImage message
	 */
	private BufferedImage createTransitionMessage(String message) {
		/*
		 * create BufferedImage and paint map using its Graphics2D object
		 */
		multiplayerMap.drawMap(mapCanvas.createGraphics());
		BufferedImage msg = new BufferedImage(GameConstants.FRAME_SIZE_X,
				GameConstants.FRAME_SIZE_Y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = msg.createGraphics();
		g2d.setColor(Color.black);
		g2d.drawRect(0, 0, msg.getWidth(), msg.getHeight());
		g2d.drawImage(mapCanvas, mapCanvasPosX, mapCanvasPosY,
				mapCanvas.getWidth(), mapCanvas.getHeight(), null);
		g2d.setFont(unitFont);

		/*
		 * center text message
		 */
		Rectangle2D rect = unitFont.getStringBounds(message,
				g2d.getFontRenderContext());
		g2d.drawString(message,
				(int) (GameConstants.FRAME_SIZE_X - rect.getWidth()) / 2,
				(int) (GameConstants.FRAME_SIZE_Y - rect.getHeight()) / 2);
		return msg;
	}
}
