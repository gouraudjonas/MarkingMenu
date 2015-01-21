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
import java.awt.geom.Line2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import javax.swing.JComponent;

/**
 *
 * @author sacapuce
 */
public class Biscuit extends JComponent {

    private Point centerPoint;
    public float startAngle;
    public float endAngle;
    public float startRadius;
    public float endRadius;
    private String text;
    private Color color;
    private Color colorHighlight;
    private boolean stateReleased;

    private final PropertyChangeSupport support;

    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color DEFAULT_COLORHIGHLIGHT = Color.RED;

    public static final String PROP_CENTERPOINT = "centerPoint";
    public static final String PROP_STARTANGLE = "startAngle";
    public static final String PROP_ENDANGLE = "endAngle";
    public static final String PROP_STARTRADIUS = "startRadius";
    public static final String PROP_ENDRADIUS = "endRadius";
    public static final String PROP_TEXT = "text";
    public static final String PROP_COLOR = "color";
    public static final String PROP_COLORHIGHLIGHT = "colorHighlight";
    public static final String PROP_STATERELEASED = "stateReleased";

    /**
     * Obligatory empty constructor
     */
    public Biscuit() {
        this("text");
    }

    /**
     * Constructor to create a minimal biscuit (just text)
     *
     * @param text
     */
    public Biscuit(String text) {
        this(new Point(0, 0), 0, 90, 1, 10, text, DEFAULT_COLOR,
                DEFAULT_COLORHIGHLIGHT, false);
    }

    /**
     * Constructor with general information (which won't without dynamic
     * changes)
     *
     * @param text
     * @param startAngle
     * @param endAngle
     * @param startRadius
     * @param endRadius
     */
    public Biscuit(String text, float startAngle, float endAngle,
            float startRadius, float endRadius) {
        this(new Point(0, 0), startAngle, endAngle, startRadius, endRadius,
                text, DEFAULT_COLOR, DEFAULT_COLORHIGHLIGHT, false);
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
     * @param stateRealeased
     */
    public Biscuit(Point p, float startAngle, float endAngle, float startRadius,
            float endRadius, String text, Color color, Color colorHighlight,
            boolean stateRealeased) {
        setCenterPoint(p);
        setStartAngle(startAngle);
        setEndAngle(endAngle);
        setStartRadius(startRadius);
        setEndRadius(endRadius);
        setText(text);
        setColor(color);
        setColorHighlight(colorHighlight);
        setStateReleased(stateReleased);
        support = new PropertyChangeSupport(this);
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

        Color oldColor = g2d.getColor();

        // Paint the whole arc
        g2d.setColor(this.getColor());
        g2d.fillArc((int) centerPoint.getX(), (int) centerPoint.getY(),
                (int) (endRadius * Math.sin(startAngle)),
                (int) (endRadius * Math.cos(endAngle)),
                (int) startAngle, (int) (endAngle - startAngle));

        // Repaint the little arc to give the illusion that there is nothing
        // between 0 and startRadius (he he)
        g2d.setColor(oldColor);
        g2d.fillArc((int) centerPoint.getX(), (int) centerPoint.getY(),
                (int) (startRadius * Math.sin(startAngle)),
                (int) (startRadius * Math.cos(endAngle)),
                (int) startAngle, (int) (endAngle - startAngle));
    }

    @Override
    public boolean contains(Point p) {
        Point startPoint = new Point(pointByPolar(startAngle, endRadius));
        Point endPoint = new Point(pointByPolar(endAngle, endRadius));
        float angleStartPoint = getAngle(centerPoint, startPoint);
        float angleEndPoint = getAngle(centerPoint, endPoint);
        float angleMousePoint = getAngle(centerPoint, p);
        
        if(angleMousePoint >= angleStartPoint && angleMousePoint <= angleEndPoint){
            float mouseDistance =
                    (float) Math.sqrt(Math.pow(p.getX(), 2) +Math.pow(p.getY(), 2));
            if (mouseDistance >= startRadius && mouseDistance <= endRadius){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(int x, int y) {
        return contains(new Point(x, y));
    }

    private float getAngle(Point p1, Point p2) {
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
    }

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

    public void privateSetCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        Point oldCenterPoint = getCenterPoint();
        privateSetCenterPoint(centerPoint);
        repaint();
        support.firePropertyChange(PROP_CENTERPOINT, oldCenterPoint, getCenterPoint());
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void privateSetStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public void setStartAngle(float startAngle) {
        float oldStartAngle = getStartAngle();
        privateSetStartAngle(startAngle);
        repaint();
        support.firePropertyChange(PROP_STARTANGLE, oldStartAngle, getStartAngle());
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

    public void privateSetStateReleased(boolean stateReleased) {
        this.stateReleased = stateReleased;
    }

    public void setStateReleased(boolean stateReleased) {
        boolean oldStateRealeased = isStateReleased();
        privateSetCenterPoint(centerPoint);
        repaint();
        support.firePropertyChange(PROP_STATERELEASED, oldStateRealeased, isStateReleased());
    }
}
