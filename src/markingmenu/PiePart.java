/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markingmenu;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;

/**
 *
 * @author gouraujo
 */
public class PiePart extends JComponent {

    public int startAngle;
    public int endAngle;

    public static final int DEFAULT_RADIUS = 1;
    public static final String PROP_SHAPE = "shape";
    public static final String PROP_COLOR = "color";
    public static final String PROP_STARTANGLE = "startAngle";
    public static final String PROP_ENDANGLE = "endAngle";
    private static final Color DEFAULT_COLOR = Color.GRAY;

    private Color color;
    private int shape;
    private final PropertyChangeSupport support;

    public PiePart(int startAngle, int endAngle) {
        this(startAngle, endAngle, DEFAULT_COLOR, DEFAULT_RADIUS);
    }

    public PiePart(int startAngle, int endAngle, Color color, int shape) {
        privateSetStartAngle(startAngle);
        privateSetEndAngle(endAngle);
        privateSetColor(color);
        privateSetShape(shape);
        support = new PropertyChangeSupport(this);
    }

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
     * ******** COLOR ********
     */
    public Color getColor() {
        return color;
    }

    public void privateSetColor(Color color) {
        this.color = color;
    }

    public void setColor(Color color) {
        Color oldColor = getColor();
        privateSetColor(color);
        repaint();
        support.firePropertyChange(PROP_COLOR, oldColor, getColor());
    }

    /**
     * ******** SHAPE ********
     */
    public int getShape() {
        return shape;
    }

    private void privateSetShape(int shape) {
        this.shape = shape;
    }

    public void setShape(int shape) {
        int oldShape = getShape();
        privateSetShape(shape);
        repaint();
        support.firePropertyChange(PROP_SHAPE, oldShape, getShape());
    }

    /**
     * ******** START ANGLE ********
     */
    public int getStartAngle() {
        return startAngle;
    }

    public void privateSetStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public void setStartAngle(int startAngle) {
        int oldStartAngle = getStartAngle();
        privateSetStartAngle(startAngle);
        repaint();
        support.firePropertyChange(PROP_STARTANGLE, oldStartAngle, getStartAngle());
    }

    /**
     * ******** END ANGLE ********
     *
     */
    public int getEndAngle() {
        return endAngle;
    }

    public void privateSetEndAngle(int endAngle) {
        this.endAngle = endAngle;
    }

    public void setEndAngle(int endAngle) {
        int oldEndAngle = getEndAngle();
        privateSetEndAngle(endAngle);
        repaint();
        support.firePropertyChange(PROP_ENDANGLE, oldEndAngle, getEndAngle());
    }

    /**
     * Draw the component on the screen
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(getColor());

        // Drawing the form - scheme on the first try (for 4 square, and not
        // 4 pie parts)
        if (startAngle == 0) {
            g.fillRect(0, -getShape(), getShape(), getShape());
        } else if (startAngle == 90) {
            g.fillRect(0, 0, getShape(), getShape());
        } else if (startAngle == 90) {
            g.fillRect(0, -getShape(), getShape(), getShape());
        } else if (startAngle == 90) {
            g.fillRect(-getShape(), -getShape(), getShape(), getShape());
        } else if (startAngle == 90) {

        }

        g.setColor(oldColor);
    }
}
