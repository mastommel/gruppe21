package mapobjects;

import imageloader.ImageLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GameConstants;

// bei start deaktiviert , kaschierendes bild über map normal laden , und exit image items

public class Exit extends MapObject {
	private boolean activated = false;

	public Exit(int x, int y, int r, boolean v, boolean d, boolean c, String p) {
		super(x, y, r, v, d, c, p);
	}

	@Override
	public void draw(Graphics2D g2d, ImageLoader gr, Graphics2D cm) {
		g2d.drawImage(gr.getImage(imageUrl), posX, posY, null);
		if (collides()) {
			if (activated) {
				cm.setPaint(Color.yellow);
				cm.fillRect(posX + GameConstants.TILE_SIZE / 3, posY
						+ GameConstants.TILE_SIZE / 3,
						GameConstants.TILE_SIZE / 3,
						GameConstants.TILE_SIZE / 3);
			} else {
				cm.setPaint(Color.black);
				cm.fillRect(posX, posY, GameConstants.TILE_SIZE,
						GameConstants.TILE_SIZE);
			}

		} else {
			cm.setPaint(Color.white);
			cm.fillRect(posX, posY, 50, 50);
		}
	}

	@Override
	public void update(BufferedImage cm) {
	}

	public void activate() {
		imageUrl = GameConstants.MAP_GRAPHICS_DIR + "exit.png";
		activated = true;
	}

}
