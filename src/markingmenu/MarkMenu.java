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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import test.FunTesting;

/**
 * Un marking menu permettant d'exécuter des actions de différentes façons. Nécessite d'avoir ajouter des item avec addItem(String nom, Callable action)
 * @author AdrienSIEGFRIED
 * @author JonasGOURAUD
 */
public class MarkMenu extends JPanel {

    private static final long DELAY = 100;  // Délai pour le timer de marking

    private MarkMenuState state;
    private List<PiePart> myPieParts;           // l'ensemble
    private Point menuCenter = new Point(0, 0); // centre du menu
    private boolean isMenuVisible = false;      // état de la visibilité
    private Timer timer;                        // timer pour le marking
    private PiePart piePartPicked;              // l'item sélectionné
    private JFrame frame;                       // la frame dans laquelle le menu va apparaître

    public MarkMenu(JFrame frame) { 
        this.frame = frame;
        
        MarkMouseAdapter mma = new MarkMouseAdapter();
        addMouseListener(mma);
        addMouseMotionListener(mma);        
        
        myPieParts = new ArrayList<PiePart>();
        setState(MarkMenuState.IDLE);
    }

    /**
     * Mise à jour de tous les angles des éléments du menu
     */
    private void updateAngles() {
        int i = 0;
        int nbOfPieParts = this.myPieParts.size();

        for (PiePart p : myPieParts) {
            p.setStartAngle((360 / nbOfPieParts) * i);  // angle de début
            p.setExtendAngle(360 / nbOfPieParts);       // l'angle totale
            i++;
        }
    }

    /**
     * Mise à jour du centre du menu en prenant "pointer" comme référence
     * @param pointer   nouveau center
     */
    private void centerMenu(Point pointer) {
        this.menuCenter = pointer;
        for (PiePart p : myPieParts) {
            p.updatePosition(pointer.x, pointer.y);
        }
    }
    
    /**
     * Affichage du menu par succession des couches des éléments du menu
     */
    private void displayMenu() {
        if (myPieParts.size() < 1) {
            return;
        }
        this.setLayout(new javax.swing.OverlayLayout(this));
        myPieParts.stream().forEach((p) -> {
            this.add(p);
        });
        isMenuVisible = true;
        repaint();
    }

    /**
     * Cache le menu
     */
    private void hideMenu() {
        myPieParts.stream().forEach((p) -> {
            p.resetColor();
        });
        isMenuVisible = false;
        repaint();
    }

    /**
     * Changement de l'état du marking menu avec markMenuState
     * @param markMenuState     nouvel état
     */
    private void setState(MarkMenuState markMenuState) {
        this.state = markMenuState;
    }

    /**
     * Récupérer l'état du marking menu
     * @return  état du marking menu
     */
    private MarkMenuState getState() {
        return this.state;
    }

    /**
     * Ajoute un élément au menu avec l'action par défaut : Sysout de son label
     * @param label     texte affiché sur le marking menu
     */
    public void addItem(String label) {
        this.addItem(label, new Callable() {
            @Override
            public void execute() {
                System.out.println("Executing: " + label);
            }
        });
    }

    /**
     * Ajoute un élément au menu
     * @param label     text à afficher
     * @param func      fonction à exécuter
     */
    public void addItem(String label, Callable func) {
        PiePart part = new PiePart(label, func);
        part.setVisible(false);
        myPieParts.add(part);
        updateAngles();
    }
    
    /**
     * Mise à jour de l'élément du menu sélectionné (piePartPicked) selon le point donné et retourne vrai s'il y a une sélection
     * @param pt    la position à prendre en compte
     * @return      s'il y a un menu sélectionné
     */
    private boolean piePartUnderPointer(Point pt) {
        double degree = calcRotationAngleInDegrees(pt, menuCenter);
        double distance = Math.sqrt((pt.x - menuCenter.x) * (pt.x - menuCenter.x)
                + (pt.y - menuCenter.y) * (pt.y - menuCenter.y));
        for (PiePart p : myPieParts) {
            if (p.isContained(degree, distance)) {
                piePartPicked = p;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Met en surbrilliance l'élément du menu sous le point
     * @param pt    le point servant de référence
     */
    private void highlight(Point pt) {
        double degree = calcRotationAngleInDegrees(pt, menuCenter);
        double distance = Math.sqrt((pt.x - menuCenter.x) * (pt.x - menuCenter.x)
                + (pt.y - menuCenter.y) * (pt.y - menuCenter.y));
        for (PiePart p : myPieParts) {
            if (p.isContained(degree, distance)) {
                p.highlight();
            } else {
                p.resetColor();
            }
        }
        repaint();
    }

    /**
     * Dessine le composant
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        myPieParts.stream().map((p) -> {
            p.setVisible(isMenuVisible);
            return p;
        }).forEach((p) -> {
            p.paint(g);
        });

        ((FunTesting) frame).action(g);
    }

    /**
     * Un MouseAdapter pour le marking menu
     */
    private class MarkMouseAdapter extends MouseAdapter {

        /**
         * Gestion de l'évènement clic gauche :
         *      - ne rien faire pour IDLE
         *      - exécute ou non item pour MENU, puis change pour IDLE
         * @param e 
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                switch (getState()) {
                    case IDLE:
                        setState(MarkMenuState.IDLE);
                        break;
                    case MARKING:
                        // Impossible
                        break;
                    case MENU:
                        if (piePartUnderPointer(e.getPoint())) {
                            piePartPicked.getAction().execute();
                        }
                        hideMenu();
                        setState(MarkMenuState.IDLE);
                        break;
                    case VISIBLE:
                        // Impossible
                        break;
                    case INVISIBLE:
                        // Impossible
                        break;
                    default:
                }
            }
        }

        /**
         * Gestion de l'évènement pression droite :
         *      - lance le timer pour IDLE
         * @param e 
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                switch (getState()) {
                    case IDLE:
                        timer = new Timer();
                        timer.schedule(new MarkingMenuTimer(), DELAY);
                        centerMenu(e.getPoint());
                        setState(MarkMenuState.MARKING);
                        break;
                    case MARKING:
                        // Impossible
                        break;
                    case MENU:
                        // Impossible
                        break;
                    case VISIBLE:
                        // Impossible
                        break;
                    case INVISIBLE:
                        // Impossible
                        break;
                    default:
                }
            }
        }

        /**
         * Gestion de l'évènement de déplacement (sans pression de la souris):
         *      - Surbrilliance éventuelle d'un élément du menu
         * @param e 
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            switch (getState()) {
                case IDLE:
                    setState(MarkMenuState.IDLE);
                    break;
                case MARKING:
                    // Impossible
                    break;
                case MENU:
                    highlight(e.getPoint());
                    setState(MarkMenuState.MENU);
                    break;
                case VISIBLE:
                    // Impossible
                    break;
                case INVISIBLE:
                    // Impossible
                    break;
                default:
            }
        }

        /**
         * Gestion de l'évènement de relâchement (pression droite souris):
         *      - affichage du menu pour MARKING
         *      - exécution éventuelle pour VISIBLE
         *      - exécution éventuelle pour INVISIBLE
         * @param e 
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                switch (getState()) {
                    case IDLE:
                        System.out.println("DEBUG > through mouseReleased in IDLE");
                        // Impossible
                        break;
                    case MARKING:
                        System.out.println("DEBUG > through mouseReleased in MARKING");
                        timer.cancel();
                        displayMenu();
                        // Execute item if one selected
                        setState(MarkMenuState.MENU);
                        break;
                    case MENU:
                        System.out.println("DEBUG > through mouseReleased in MENU");
                        // Impossible
                        break;
                    case VISIBLE:
                        System.out.println("DEBUG > through mouseReleased in VISIBLE");
                        if (piePartUnderPointer(e.getPoint())) {
                            piePartPicked.getAction().execute();
                        }
                        hideMenu();
                        setState(MarkMenuState.IDLE);
                        break;
                    case INVISIBLE:
                        System.out.println("DEBUG > through mouseReleased in INVISIBLE");
                        if (piePartUnderPointer(e.getPoint())) {
                            piePartPicked.getAction().execute();
                        }
                        setState(MarkMenuState.IDLE);
                        break;
                    default:
                }
            }
        }

        /**
         * Gestion de l'évènement de déplacement avec pression droite :
         *      - Mise en surbrilliance pour VISIBLE
         * @param e 
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                switch (getState()) {
                    case IDLE:
                        // Impossible
                        break;
                    case MARKING:
                        //timer.cancel();
                        setState(MarkMenuState.INVISIBLE);
                        break;
                    case MENU:
                        // Impossible
                        break;
                    case VISIBLE:
                        highlight(e.getPoint());
                        setState(MarkMenuState.VISIBLE);
                        break;
                    case INVISIBLE:
                        // Nothing to do
                        setState(MarkMenuState.INVISIBLE);
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Timer manager pour le marking menu
     */
    private class MarkingMenuTimer extends TimerTask {

        private Point initialPointer;

        MarkingMenuTimer() {}
        MarkingMenuTimer(Point p) {
            initialPointer = p;
        }

        /**
         * Affichage du menu si état est MARKING
         */
        @Override
        public void run() {
            switch (getState()) {
                case IDLE:
                    // Forbidden
                    break;
                case MARKING:
                    displayMenu();
                    setState(MarkMenuState.VISIBLE);
                    break;
                case MENU:
                    // Forbidden
                    break;
                case VISIBLE:
                    // Forbidden
                    break;
                case INVISIBLE:
                    // Forbidden
                    break;
                default:
            }
        }
    }

    /**
     * Type enum pour l'état du marking menu
     */
    private enum MarkMenuState {
        IDLE,
        MARKING,
        MENU,
        VISIBLE,
        INVISIBLE;
    }

    /**
     * > http://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
     * 
     * Calculates the angle from centerPt to targetPt in degrees. The return
     * should range from [0,360), rotating CLOCKWISE, 0 and 360 degrees
     * represents NORTH, 90 degrees represents EAST, etc...
     *
     * Assumes all points are in the same coordinate space. If they are not, you
     * will need to call SwingUtilities.convertPointToScreen or equivalent on
     * all arguments before passing them to this function.
     *
     * @param centerPt Point we are rotating around.
     * @param targetPt Point we want to calcuate the angle to.
     * @return angle in degrees. This is the angle from centerPt to targetPt.
     */
    public static double calcRotationAngleInDegrees(Point centerPt, Point targetPt) {
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.  
        // NOTE: By preserving Y and X param order to atan2,  we are expecting 
        // a CLOCKWISE angle direction.  
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        // rotate the theta angle clockwise by 90 degrees 
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.  
        // subtracting would rotate it counter-clockwise
        theta += Math.PI / 2.0;

        // convert from radians to degrees
        // this will give you an angle from [0->270],[-180,0]
        double angle = Math.toDegrees(theta);
        // Turn 90° degrees clockwise
        angle += 90;
        // convert to positive range [0-360)
        // since we want to prevent negative angles, adjust them now.
        // we can assume that atan2 will not return a negative value
        // greater than one partial rotation
        if (angle < 0) {
            angle += 360;
        }

        return 360 - angle; // Reverse direction
    }
}
