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
    private float extendAngle;
    private float startRadius;
    private float endRadius;
    private String text;
    private Color color;
    private Area area;
    private Callable action;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Color originalColor;
    private Color originalHighlightColor;

    /**
     * Obligatory empty constructor
     */
    public PiePart() {
        this("text", new Callable() {

            @Override
            public void execute() {
                System.out.println("Action executed is: text");
            }
        });
    }

    /**
     * Constructor to create a minimal biscuit (just text)
     *
     * @param text
     */
    public PiePart(String text, Callable func) {
        this(new Point(0, 0), 0, 90, 10, 100, text, func);
    }

    /**
     * Constructor with general information
     *
     * @param p
     * @param text
     * @param startAngle
     * @param extendAngle
     * @param startRadius
     * @param endRadius
     */
    public PiePart(Point p, float startAngle, float extendAngle,
            float startRadius, float endRadius, String text, Callable func) {
        this(p, startAngle, extendAngle, startRadius, endRadius, text, new Color(0x04819E), new Color(0xFF7F00), func);
    }

    /**
     * Complete constructor
     *
     * @param p centerPoint
     * @param startAngle
     * @param extendAngle
     * @param startRadius
     * @param endRadius
     * @param text
     * @param color
     * @param colorHighlight
     */
    public PiePart(Point p, float startAngle, float extendAngle, float startRadius,
            float endRadius, String text, Color color, Color colorHighlight, Callable func) {
        setCenterPoint(p);
        setStartAngle(startAngle);
        setExtendAngle(extendAngle);
        setStartRadius(startRadius);
        setEndRadius(endRadius);
        setText(text);
        setOriginalColor(color);
        setOriginalHighlightColor(colorHighlight);
        setAction(func);

        setColor(this.getOriginalColor());
        //addMouseListener(this);
        area = createArea();
    }

    /**
     * Create the area according to PiePart caracteristics : a big arc, then
     * substracting a small arc around center
     *
     * @return area
     */
    private Area createArea() {
        Area startingArea = new Area();
        Arc2D bigArc = new Arc2D.Float((float) centerPoint.getX(),
                (float) centerPoint.getY(), endRadius * 2,
                endRadius * 2, startAngle, extendAngle, Arc2D.PIE);
        Arc2D smallArc = new Arc2D.Float(
                (float) centerPoint.getX() + endRadius - startRadius,
                (float) centerPoint.getY() + endRadius - startRadius,
                startRadius * 2, startRadius * 2, startAngle,
                extendAngle, Arc2D.PIE);

        startingArea.add(new Area(bigArc));
        startingArea.subtract(new Area(smallArc));

        return startingArea;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (this.isVisible()) {
            Font oldFont = g2d.getFont();
            Color oldColor = g2d.getColor();

            // Paint the whole arc
            g2d.setColor(this.getColor());
            g2d.fill(getArea());
            g2d.setColor(Color.BLACK);
            g2d.draw(getArea());

            // Paint the text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            int xString = (int) (centerPoint.x + endRadius +
                    Math.cos(degreeToRadian(startAngle + extendAngle / 2)) * endRadius / 2);
            int yString = (int) (centerPoint.y + endRadius -
                    Math.sin(degreeToRadian(startAngle + extendAngle / 2)) * endRadius / 2);
            g2d.drawString(this.getText(), xString, yString);

            // Get back to the old color
            g2d.setColor(oldColor);
            g2d.setFont(oldFont);
        }
    }

    private double degreeToRadian(double degree) {
        return degree * 2 * Math.PI / 360;
    }

    @Override
    public boolean contains(Point p) { // TODO
        //return getArea().contains(p);
        return true;
    }

    @Override
    public boolean contains(int x, int y) {
        return getArea().contains(new Point(x, y));
    }

    /**
     * Used before showing this PiePart, to update it with the current mouse
     * position
     *
     * @param centerPointX
     * @param centerPointY
     */
    public void updatePosition(int centerPointX, int centerPointY) {
        // Update of centerPoint and the area
        this.setCenterPoint(new Point(centerPointX - (int) this.getEndRadius(),
                centerPointY - (int) this.getEndRadius()));
        area = createArea();
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

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getExtendAngle() {
        return extendAngle;
    }

    public void setExtendAngle(float extendAngle) {
        this.extendAngle = extendAngle;
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

    public Color getOriginalColor() {
        return originalColor;
    }

    public void setOriginalColor(Color originalColor) {
        this.originalColor = originalColor;
    }

    public Color getOriginalHighlightColor() {
        return originalHighlightColor;
    }

    public void setOriginalHighlightColor(Color originalHighlightColor) {
        this.originalHighlightColor = originalHighlightColor;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    private void setAction(Callable func) {
        this.action = func;
    }
    
    public Callable getAction() {
        return this.action;
    }

    public boolean isContained(double degree, double distance) {
        
        if (degree > startAngle && degree < (startAngle + extendAngle)) {            
            if (distance > startRadius && distance < endRadius) return true;
        }
        return false;
    }

    void highlight() {
        setColor(getOriginalHighlightColor());
    }

    void resetColor() {
        setColor(getOriginalColor());
    }
}
