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
 *
 * @author AdrienSIEGFRIED
 */
public class MarkMenu extends JPanel {

    public static final String ACT_SHOW_MENU = "showing menu";
    public static final String ACT_HIDE_MENU = "hiding menu";
    public static final String ACT_WAIT_MARK = "waiting mark";

    private static final long DELAY = 100;

    private MarkMenuState state;
    private List<PiePart> myPieParts;
    private Point menuCenter = new Point(0, 0);
    private boolean isMenuVisible = false;
    private Timer timer;
    private PiePart piePartPicked;
    private JFrame frame;

    public MarkMenu(JFrame frame) {
        myPieParts = new ArrayList<PiePart>();
        setState(MarkMenuState.IDLE);
        MarkMouseAdapter mma = new MarkMouseAdapter();
        addMouseListener(mma);
        addMouseMotionListener(mma);
        this.frame = frame;
    }

    private void updateAngles() {
        int i = 0;
        int nbOfPieParts = this.myPieParts.size();

        for (PiePart p : myPieParts) {
            p.setStartAngle((360 / nbOfPieParts) * i);
            p.setExtendAngle(360 / nbOfPieParts);
            i++;
        }
    }

    private void centerMenu(Point pointer) {
        this.menuCenter = pointer;
        for (PiePart p : myPieParts) {
            p.updatePosition(pointer.x, pointer.y);
        }
    }

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

    private void hideMenu() {
        myPieParts.stream().forEach((p) -> {
            p.resetColor();
        });
        isMenuVisible = false;
        repaint();
    }

    private void setState(MarkMenuState markMenuState) {
        this.state = markMenuState;
        //System.out.println("State is now: "+markMenuState);
    }

    private MarkMenuState getState() {
        return this.state;
    }

    public void addItem(String label) {
        this.addItem(label, new Callable() {
            @Override
            public void execute() {
                System.out.println("Executing: " + label);
            }
        });
    }

    public void addItem(String label, Callable func) {
        PiePart part = new PiePart(label, func);
        part.setVisible(false);
        myPieParts.add(part);
        updateAngles();
    }

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
     * Mouse adapter for the marking menu
     */
    private class MarkMouseAdapter extends MouseAdapter {

        public MarkMouseAdapter() {
        }

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
    }

    /**
     * Timer manager for MarkMenu
     */
    private class MarkingMenuTimer extends TimerTask {

        private Point initialPointer;

        MarkingMenuTimer() {
        }

        MarkingMenuTimer(Point p) {
            initialPointer = p;
        }

        @Override
        public void run() {
            //System.out.println("time out");
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
     * Enumerator of possible states for MarkMenu
     */
    private enum MarkMenuState {

        IDLE,
        MARKING,
        MENU,
        VISIBLE,
        INVISIBLE;
    }

    /**
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
