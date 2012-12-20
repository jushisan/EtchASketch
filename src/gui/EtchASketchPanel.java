package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * A panel with the classic Etch A Sketch look (i.e., red border with white knobs).
 * @author Judy
 *
 */
public class EtchASketchPanel extends JPanel {
	
	public EtchASketchPanel() {
		setBackground(Color.red);
		Border paneEdge = BorderFactory.createEmptyBorder(40,60,60,60);
        setBorder(paneEdge);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.white);
		Dimension size = this.getSize();
		int buttonSize = 70;
		g2d.fillOval(0, size.height - buttonSize, buttonSize, buttonSize);
		g2d.fillOval(size.width - buttonSize, size.height - buttonSize, buttonSize, buttonSize);
	}
	

}
