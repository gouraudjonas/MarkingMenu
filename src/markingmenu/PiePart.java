/*
 * Copyright (C) 2015 Adrien Siegfried Jonas Gouraud
 * 
 * This file is part of MarkingMenu.
 *
 * MarkingMenu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MarkingMenu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MarkingMenu. If not, see <http://www.gnu.org/licenses/>.
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
 * PiePart (ou tuile) : un JComponent utilisé par le MarkMenu
 *
 * @author AdrienSIEGFRIED
 * @author JonasGOURAUD
 */
public class PiePart extends JComponent {
    
    private Point centerPoint;      // Le centre de la tuile
    private float startAngle;       // L'angle de début de la tuile
    private float extendAngle;      // L'angle total de la tuile
    private float startRadius;      // Le début du rayon de la tuile
    private float endRadius;        // La fin du rayon de la tuile
    private String text;            // Le texte affiché par la tuile
    private Color color;            // La couleur actuelle de la tuile 
    private Area area;              // L'aire de la tuile
    private Callable action;        // L'action associée à la tuile

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Color originalColor;            // Couleur donnée à l'originie
    private Color originalHighlightColor;   // Couleur donnée pour un survol


    /**
     * Création d'une tuile avec par défaut "text" et l'action d'un Sysout
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
     * Construction d'une tuile avec un centre de (0, 0), avec 90° commençant à 0° avec un rayon de 10 à 100
     * @param text      Le texte à affiché
     * @param func      L'action à exécuter
     */
    public PiePart(String text, Callable func) {
        this(new Point(0, 0), 0, 90, 10, 100, text, func);
    }

    /**
     * Construction d'une tuile avec la couleur bleu en normal et orange au survol
     * @param p
     * @param startAngle
     * @param extendAngle
     * @param startRadius
     * @param endRadius
     * @param text
     * @param func 
     */
    public PiePart(Point p, float startAngle, float extendAngle,
            float startRadius, float endRadius, String text, Callable func) {
        this(p, startAngle, extendAngle, startRadius, endRadius, text, new Color(0x04819E), new Color(0xFF7F00), func);
    }

    /**
     * 
     * @param p                 Convergence de la tuile vers ce point
     * @param startAngle        L'angle de départ
     * @param extendAngle       L'angle totale
     * @param startRadius       Le début du rayon
     * @param endRadius         La fin du rayon
     * @param text              Le texte à affiché
     * @param color             La couleur par défaut
     * @param colorHighlight    La couleur au survol
     * @param func              La fonction à exécuter
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
        area = createArea();
    }

    /**
     * Création de la zone correspondant aux caractéristiques de la tuile.
     * On crée une grande arc et on y soustrait la plus petite (centre)
     *
     * @return area     La zone à déssiner
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

    /**
     * Fonction de dessin
     * 
     * @param g 
     */
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

    /**
     * Convertisseur de degrés en radian
     * 
     * @param degree
     * @return 
     */
    private double degreeToRadian(double degree) {
        return degree * 2 * Math.PI / 360;
    }    

    /**
     * Mise à jour des positions relatives à la souris
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
    public boolean isContained(double degree, double distance) {
        
        if (degree > startAngle && degree < (startAngle + extendAngle)) {            
            if (distance > startRadius && distance < endRadius) return true;
        }
        return false;
    }
    /**
     * Mise en surbrillance de la tuile au survol
     */
    void highlight() {
        setColor(getOriginalHighlightColor());
    }
    /**
     * Remettre la couleur d'origine sur la tuile
     */
    void resetColor() {
        setColor(getOriginalColor());
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
}
