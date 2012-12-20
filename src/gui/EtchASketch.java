package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This class imitates the classic drawing tool: Etch A Sketch.
 * Up, down, left and right keys draw points in the said direction.
 * Space clears the board.
 * @author Judy
 *
 */
public class EtchASketch extends JFrame implements KeyListener, ActionListener {
	
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int MOVE_LEFT = 3;
	public static final int MOVE_RIGHT = 4;
	
	public static final String EXPORT_MENU_ITEM_TEXT = "Export Image as JPG";
	public static final String SAVE_MENU_ITEM_TEXT = "Save";
	public static final String LOAD_MENU_ITEM_TEXT = "Load";
	
	private PointManager pointManager = new PointManager();
	private SketchPane pane = new SketchPane(new GridLayout(1, 1), false, pointManager);
	int margin = 5;
	
	public EtchASketch(int x, int y) {
		super("Etch A Sketch");
		
		// Set up point manager and sketch pane
		pointManager.setCurrentPoint(x + margin, y + margin);
		pointManager.setPointSize(2);
		Dimension size = new Dimension(800,600);
		this.setSize(size);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = getContentPane();
		focusSketchPane();
		addMenu();
		
		// Set up sketch boarder
		JPanel border = getBorderLayout();
		con.add(border);
		
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void focusSketchPane() {
		pane.setFocusable(true);
		pane.requestFocusInWindow();
		pane.addKeyListener(this);
	}

	/**
	 * Adds menu items for saving, exporting and loading drawings. Export
	 * function exports the file into a jpg format picture, and save function
	 * allows the user to load it back and continue drawing in the next session.
	 */
	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		// Create "File" menu
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		// Create menu items;
		JMenuItem loadItem = new JMenuItem(LOAD_MENU_ITEM_TEXT);
		loadItem.addActionListener(this);
		menu.add(loadItem);
		
		JMenuItem saveItem = new JMenuItem(SAVE_MENU_ITEM_TEXT);
		saveItem.addActionListener(this);
		menu.add(saveItem);
		
		JMenuItem exportItem = new JMenuItem(EXPORT_MENU_ITEM_TEXT);
		exportItem.addActionListener(this);
		menu.add(exportItem);
		this.setJMenuBar(menuBar);
	}
	
	private JPanel getBorderLayout() {
        EtchASketchPanel simpleBorders = new EtchASketchPanel();        
        Border blackline = BorderFactory.createLineBorder(Color.black);
        addCompForBorder(blackline, "line border", simpleBorders);
        return simpleBorders;
	}
	
	void addCompForBorder(Border border, String description, Container container) {
		pane.setBorder(border);
		container.add(Box.createRigidArea(new Dimension(0, 10)));
		container.add(pane);
	}
	
	public void clearBoard() {
		pointManager.clearPoints();
		pane.repaint();
	}
	
		
	@Override
	public void keyPressed(KeyEvent e) {
		movePoints(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		movePoints(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		movePoints(e);
	}
	
	/**
	 * Update the drawing according to the key pressed.
	 * @param e
	 */
	public void movePoints(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
        case KeyEvent.VK_UP:
        	drawNextPoint(MOVE_UP);
        	break;
        case KeyEvent.VK_DOWN:
        	drawNextPoint(MOVE_DOWN);
        	break;
        case KeyEvent.VK_LEFT:
        	drawNextPoint(MOVE_LEFT);
        	break;
        case KeyEvent.VK_RIGHT:
        	drawNextPoint(MOVE_RIGHT);
        	break;
        case KeyEvent.VK_SPACE:
        	clearBoard();
        	break;
        }
	}
	
	/**
	 * Draw new point according to the input direction.
	 * @param direction
	 */
	public void drawNextPoint(int direction) {
		int x = pointManager.getCurrentX();
		int y = pointManager.getCurrentY();
		switch (direction) {
		case 1:
			if (y > margin)
				y--;
			break;
		case 2:
			if (y < (pane.getSize().height - margin))
				y++;
			break;
		case 3:
			if (x > margin)
				x--;
			break;
		case 4:
			if (x < (pane.getSize().width - margin))
				x++;
			break;
		}
		pointManager.addPoint(x, y);
		pane.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.requestFocusInWindow();
		if (arg0.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) arg0.getSource();
			if (item.getText().equals(EXPORT_MENU_ITEM_TEXT)) {
				exportDrawing();
			} else if (item.getText().equals(LOAD_MENU_ITEM_TEXT)) {
				loadDrawing();
			} else if (item.getText().equals(SAVE_MENU_ITEM_TEXT)) {
				saveDrawing();
			}
		}
	}
	
	public void saveDrawing() {
		JFileChooser c = new JFileChooser();
		int rVal = c.showSaveDialog(this);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			try {
				String filename = c.getSelectedFile().getCanonicalPath();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(filename)));
				out.writeObject(pane.getPointManager());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO: Display "File Not Found" message.
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: Display "Cannot Save File" message.
				e.printStackTrace();
			}
		}
		focusSketchPane();
	}
	
	public void loadDrawing() {
		JFileChooser c = new JFileChooser();
		int rVal = c.showOpenDialog(this);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			try {
				String filename = c.getSelectedFile().getCanonicalPath();
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
				PointManager ptMngr = (PointManager) in.readObject();
				pointManager = ptMngr;
				pane.setPointManager(ptMngr);
				in.close();
			} catch (FileNotFoundException e) {
				// TODO: Display "File Not Found" message.
			} catch (IOException e) {
				// TODO: Display "Cannot Load File" message.
			} catch (ClassNotFoundException e) {
				// TODO: Display "Class Not Found" message.
			}
		}
		pane.clear();
		pane.repaint();
		focusSketchPane();
	}
	
	public void exportDrawing() {
		JFileChooser c = new JFileChooser();
		int rVal = c.showSaveDialog(this);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			try {
				String filename = c.getSelectedFile().getCanonicalPath();
				Dimension size = getContentPane().getSize();
				BufferedImage img = (BufferedImage) getContentPane().createImage(
						size.width, size.height);
				Graphics g = img.getGraphics();
				getContentPane().paint(g);
				g.dispose();
				ImageIO.write(img, "jpg", new File(filename + ".jpg"));
			} catch (IOException e) {
				// TODO:  Displays cannot export file message.
				e.printStackTrace();
			}
		}
		getContentPane().repaint();
		focusSketchPane();
	}
}
