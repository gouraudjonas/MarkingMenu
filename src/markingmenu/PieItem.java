/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markingmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javafx.scene.shape.Circle;
import javax.swing.JComponent;

/**
 *
 * @author AdrienSIEGFRIED
 */
public class PieItem extends JComponent {

    private Point centerPoint = new Point(0, 0);
    private float orientation;
    private float angle;
    private float length;
    private String text = "Item";
    private Color color = Color.LIGHT_GRAY;
    private float insideCircleCut = 0;
    
    public PieItem() {
        this(0, 90, 10);
    }
    public PieItem(float orientation, float angle, float length) {
        setOrientation(orientation);
        setAngle(angle);
        setLength(length);
    }
    public PieItem(Point p, float orientation, float angle, float length, String text, Color color, float insideCircleCut) {
        setCenterPoint(p);
        setOrientation(orientation);
        setAngle(angle);
        setLength(length);
        setText(text);
        setColor(color);
        setInsideCircleCut(insideCircleCut);
    }
    
        @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        //scale = Math.min(width, height) / DEFAULT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getColor());
        //g2d.fillArc(getCenterPoint().x, getCenterPoint().y, (int)getLength(), get, ABORT, ABORT);
        g2d.fillArc(-super.getHeight()/2, -super.getWidth()/2, super.getHeight(), super.getWidth(), 0, 90);
    }   
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) 20 * 2, (int) 20 * 2);
    }

    /**
     * @return the centerPoint
     */
    public Point getCenterPoint() {
        return centerPoint;
    }

    /**
     * @param centerPoint the centerPoint to set
     */
    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    /**
     * @return the orientation
     */
    public float getOrientation() {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    /**
     * @return the angle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the insideCircleCut
     */
    public float getInsideCircleCut() {
        return insideCircleCut;
    }

    /**
     * @param insideCircleCut the insideCircleCut to set
     */
    public void setInsideCircleCut(float insideCircleCut) {
        this.insideCircleCut = insideCircleCut;
    }
    
}
