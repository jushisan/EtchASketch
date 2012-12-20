package gui;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;

/**
 * Manage all the drawn points.
 * @author Judy
 *
 */
public class PointManager implements Serializable {
	
	private int _currentX = 0;
	private int _currentY = 0;
	
	protected class Point implements Serializable {
		private static final long serialVersionUID = 1234567891;
		public int x;
		public int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(Object obj) {
			Point pt = (Point) obj;
			return (pt.x == x && pt.y == y);
		}
		
		public int hashCode() {
			return x + y;
		}
	}
	
	private static final long serialVersionUID = 1234567890;
	
	private HashSet<Point> points  = new HashSet<Point>();
	private int pointSize = 2;
	
	public HashSet<Point> getPoints() {
		return points;
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
		_currentX = x;
		_currentY = y;
	}
	
	public int getPointSize() {
		return pointSize;
	}
	
	public int getCurrentX() {
		return _currentX;
	}
	
	public int getCurrentY() {
		return _currentY;
	}
		
	public void setPointSize(int ptSize) {
		pointSize = ptSize;
	}
	
	public void clearPoints() {
		points.clear();
	}
	
	public void setCurrentX(int x) {
		_currentX = x;
	}
	
	public void setCurrentY(int y) {
		_currentY = y;
	}
	
	public void setCurrentPoint(int x, int y) {
		_currentX = x;
		_currentY = y;
	}
}
