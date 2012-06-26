package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main Class for the Level Editor
 * 
 * 
 * @author eik
 * 
 */
public class Editor extends JFrame implements KeyListener, MouseListener {
	JPanel mainPanel = new JPanel();
	Graphics g;
	Graphics2D canvas = (Graphics2D) g;

	public Editor() {

		add(mainPanel);
		canvas.setBackground(Color.black);
		mainPanel.paint(canvas);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setFocusable(true);
		setLocationRelativeTo(null);
		setTitle("Mapeditor Bomberman Island");
		setVisible(true);
		addKeyListener(this);
		addMouseListener(this);
	}

	public static void main(String args[]) {
		new Editor();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}