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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class PiePart extends JComponent implements MouseListener {

    private Point centerPoint;
    private float startAngle;
    private float extendAngle;
    private float startRadius;
    private float endRadius;
    private String text;
    private Color color;
    private Area area;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Color originalColor;
    private Color originalHighlightColor;

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
        this(new Point(0, 0), 0, 90, 10, 100, text);
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
            float startRadius, float endRadius, String text) {
        this(p, startAngle, extendAngle, startRadius, endRadius, text, Color.GREEN, Color.RED);
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
            float endRadius, String text, Color color, Color colorHighlight) {
        setCenterPoint(p);
        setStartAngle(startAngle);
        setExtendAngle(extendAngle);
        setStartRadius(startRadius);
        setEndRadius(endRadius);
        setText(text);
        setOriginalColor(color);
        setOriginalHighlightColor(colorHighlight);

        setColor(this.getOriginalColor());
        addMouseListener(this);
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

        Color oldColor = g2d.getColor();

        // Paint the whole arc
        g2d.setColor(this.getColor());
        g2d.fill(getArea());
        g2d.setColor(Color.BLACK);
        g2d.draw(getArea());
        g2d.setColor(oldColor);
    }

    @Override
    public boolean contains(Point p) {
        return getArea().contains(p);
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
        this.setCenterPoint(new Point(centerPointX - 8 - (int) this.getEndRadius(),
                centerPointY - 30 - (int) this.getEndRadius()));
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

    /**
     * *************** MOUSE LISTENING **********************
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setColor(this.getOriginalHighlightColor());
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setColor(this.getOriginalColor());
        repaint();
    }
}
