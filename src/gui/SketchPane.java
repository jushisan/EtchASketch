package gui;

import gui.PointManager.Point;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.util.HashSet;

import javax.swing.JPanel;

/**
 * The panel where the sketch is drawn.
 * @author Judy
 *
 */
public class SketchPane extends JPanel {
	
	private PointManager pointManager;
	
	public SketchPane(PointManager ptMngr) {
		super();
		pointManager = ptMngr;
	}
	
	public SketchPane (LayoutManager layout,
            boolean isDoubleBuffered, PointManager ptMngr) {
		super(layout, isDoubleBuffered);
		pointManager = ptMngr;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		HashSet<Point> points = pointManager.getPoints();
		for (Point pt : points) {
			g2d.fillOval(pt.x, pt.y, pointManager.getPointSize(), pointManager.getPointSize());
		}
	}
	
	public PointManager getPointManager() {
		return pointManager;
	}
	
	public void setPointManager(PointManager ptMngr) {
		pointManager = ptMngr;
	}
	
	
	public void clear() {
		this.repaint();
	}
}