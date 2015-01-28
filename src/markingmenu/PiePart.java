/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markingmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;

/**
 * PiePart which can be used to create a marking menu
 * 
 * @author Jonas Gouraud
 */
public class PiePart extends JComponent {

    private Point centerPoint;
    private float startAngle;
    private float endAngle;
    private float startRadius;
    private float endRadius;
    private String text;
    private Color color;
    private Color colorHighlight;
    private boolean stateReleased = false;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private static final Color DEFAULT_COLOR = Color.GREEN;
    private static final Color DEFAULT_COLORHIGHLIGHT = Color.RED;

    public static final String PROP_STATERELEASED = "stateReleased";

    /**
     * Obligatory empty constructor
     */
    public PiePart() {
        this("text");
    }

    /**
     * Constructor to create a minimal biscuit (just text)
     *
     * @param text
     */
    public PiePart(String text) {
        this(new Point(0, 0), 0, 90, 5, 50, text);
    }

    /**
     * Constructor with general information
     *
     * @param text
     * @param startAngle
     * @param endAngle
     * @param startRadius
     * @param endRadius
     */
    public PiePart(Point p, float startAngle, float endAngle,
            float startRadius, float endRadius, String text) {
        this(p, startAngle, endAngle, startRadius, endRadius,
                text, DEFAULT_COLOR, DEFAULT_COLORHIGHLIGHT);
    }

    /**
     * Complete constructor
     *
     * @param p centerPoint
     * @param startAngle
     * @param endAngle
     * @param startRadius
     * @param endRadius
     * @param text
     * @param color
     * @param colorHighlight
     */
    public PiePart(Point p, float startAngle, float endAngle, float startRadius,
            float endRadius, String text, Color color, Color colorHighlight) {
        setCenterPoint(p);
        setStartAngle(startAngle);
        setEndAngle(endAngle);
        setStartRadius(startRadius);
        setEndRadius(endRadius);
        setText(text);
        setColor(color);
        setColorHighlight(colorHighlight);
    }

    /**
     * Create the area according to PiePart caracteristics
     * 
     * @return area
     */
    private Area createArea() {
        Area startingArea = new Area();
        Arc2D bigArc = new Arc2D.Float((float) centerPoint.getX(),
                (float) centerPoint.getY(), endRadius * 2,
                endRadius * 2, startAngle, endAngle - startAngle, Arc2D.PIE);
        Arc2D smallArc = new Arc2D.Float(
                (float) centerPoint.getX() + endRadius - startRadius,
                (float) centerPoint.getY() + endRadius - startRadius,
                startRadius * 2, startRadius * 2, startAngle,
                endAngle - startAngle, Arc2D.PIE);

        startingArea.add(new Area(bigArc));
        startingArea.subtract(new Area(smallArc));

        return startingArea;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Color oldColor = g2d.getColor();

        // Paint the whole arc
        g2d.setColor(this.getColor());
        g2d.fill(createArea());
        g2d.setColor(oldColor);

        /*System.out.println("DEBUG > paint caracteristics : width = "
                + area.getBounds().height + " - height = "
                + area.getBounds().width + " at " + area.getBounds().x + ";"
                + area.getBounds().y);
                
        System.out.println("DEBUG > paint caracteristics : centerPoint("
                + centerPoint.getX() + ";" + centerPoint.getY()
                + ") - startAngle = " + startAngle + " - endAngle = "
                + endAngle + " - startRadius = " + startRadius
                + " - endRadius = " + endRadius);*/
    }

    @Override
    public boolean contains(Point p) {
        return createArea().contains(p);
    }

    @Override
    public boolean contains(int x, int y) {
        return createArea().contains(new Point(x, y));
    }

    /*private float getAngle(Point p1, Point p2) {
        int mouseX = p2.x - p1.x;
        int mouseY = p2.y - p1.y;
        double l = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
        double lx = mouseX / l;
        double ly = mouseY / l;
        double theta;

        if (lx > 0) {
            theta = Math.atan(ly / lx);
        } else if (lx < 0) {
            theta = -1 * Math.atan(ly / lx);
        } else {
            theta = 0;
        }

        if ((mouseX > 0) && (mouseY < 0)) {
            theta = -1 * theta;
        } else if (mouseX < 0) {
            theta += Math.PI;
        } else {
            theta = 2 * Math.PI - theta;
        }

        return (float) (theta / (2 * Math.PI));
    }

    public Point pointByPolar(float angle, float distance) {
        Point newPoint = centerPoint;

        newPoint.x += distance * Math.cos(angle);
        newPoint.y += distance * Math.sin(angle);

        return newPoint;
    }*/

    /**
     * ********* PROPERTY CHANGE LISTENER **************
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * *************** PROPERTIES **********************
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) 20 * 2, (int) 20 * 2);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(float endAngle) {
        this.endAngle = endAngle;
    }

    public float getStartRadius() {
        return startRadius;
    }

    public void setStartRadius(float startRadius) {
        this.startRadius = startRadius;
    }

    public float getEndRadius() {
        return endRadius;
    }

    public void setEndRadius(float endRadius) {
        this.endRadius = endRadius;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColorHighlight() {
        return colorHighlight;
    }

    public void setColorHighlight(Color colorHighlight) {
        this.colorHighlight = colorHighlight;
    }

    public boolean isStateReleased() {
        return stateReleased;
    }

    private void privateSetStateReleased(boolean stateReleased) {
        this.stateReleased = stateReleased;
    }

    public void setStateReleased(boolean stateReleased) {
        boolean oldStateRealeased = isStateReleased();
        privateSetStateReleased(stateReleased);
        repaint();
        support.firePropertyChange(PROP_STATERELEASED, oldStateRealeased, isStateReleased());
    }
}
