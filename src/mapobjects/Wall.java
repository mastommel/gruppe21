package mapobjects;

import imageloader.ImageLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * mapobject for simple walls
 * 
 * 
 * @author eik
 * 
 */

public class Wall extends MapObject {
	private boolean hiddenObject;

	/**
	 * constructor
	 * 
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param r
	 *            rotation of the graphic (0,1,2,3)
	 * @param v
	 *            sets visibility
	 * @param d
	 *            sets destroyable flag
	 * @param c
	 *            sets collision
	 * @param p
	 *            image url
	 */
	public Wall(int x, int y, int r, boolean v, boolean d, boolean c, String p) {
		super(x, y, r, v, d, c, p);
	}

	/**
	 * overrides super method
	 * 
	 * draws the image on the game canvas draws the collision map
	 */
	@Override
	public void draw(Graphics2D g2d, ImageLoader gr, Graphics2D cm) {
		g2d.drawImage(gr.getImage(imageUrl), posX, posY, null);

		if (collides()) {
			if (destroyable) {
				cm.setPaint(Color.gray);
			} else {
				cm.setPaint(Color.black);
			}
			cm.fillRect(posX, posY, 50, 50);
		} else {
			cm.setPaint(Color.white);
			cm.fillRect(posX, posY, 50, 50);
		}
	}

	/**
	 * overrides super method
	 * 
	 * if the wall is destroyable , wall is checked for collision with bomb
	 * 
	 * @param cm
	 *            collision map
	 */
	@Override
	public void update(BufferedImage cm) {
		if (isDestroyable()) {
			if (simpleHasColl(posX, posY, cm, Color.orange)) {
				destroyed = true;
				visible = false;
			}
		}
	}

	public void addHiddenObject() {
	}

	public void destroy() {
	}
}
