/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markingmenu;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author AdrienSIEGFRIED
 */
public class MarkMenu extends JComponent implements MouseListener, MouseMotionListener {
    
    public static final String ACT_SHOW_MENU = "showing menu";
    public static final String ACT_HIDE_MENU = "hiding menu";
    public static final String ACT_WAIT_MARK = "waiting mark";
    
    private static final long DELAY = 1000;
    
    private MarkMenuState state;
    private List<PiePart> myPieParts;

    private Timer timer = new Timer();
    
    MarkMenu() {
        setState(MarkMenuState.IDLE);
    }   
           
    private void setState(MarkMenuState markMenuState) {
        this.state = markMenuState;
    }
    private MarkMenuState getState() {
        return this.state;
    }
    
    protected void paintComponent(Graphics g) {        
        for (PiePart p : this.myPieParts) p.setVisible(getState().isMenuVisible);
    }

    
    protected void timerOut() {
        switch(getState()) {
            case IDLE:
                // Forbidden
                break;
            case MARKING:
                setState(MarkMenuState.VISIBLE); // should fire visibility
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
    
    /////////
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch(getState()) {
                case IDLE:
                    // Nothing to do
                    break;
                case MARKING:
                    break;
                case MENU:
                    break;
                case VISIBLE:
                    break;
                case INVISIBLE:
                    break;
                default:
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            switch(getState()) {
                case IDLE:
                    break;
                case MARKING:
                    break;
                case MENU:
                    break;
                case VISIBLE:
                    break;
                case INVISIBLE:
                    break;
                default:
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            switch(getState()) {
                case IDLE:
                    timer.schedule(new MarkingMenuTimer(), DELAY);
                    setState(MarkMenuState.MARKING);
                    break;
                case MARKING:
                    break;
                case MENU:
                    break;
                case VISIBLE:
                    break;
                case INVISIBLE:
                    break;
                default:
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        switch(getState()) {
            case IDLE:
                break;
            case MARKING:
                break;
            case MENU:
                break;
            case VISIBLE:
                break;
            case INVISIBLE:
                break;
            default:
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
    
    
    /**
     * Enumerator of possible states for MarkMenu
     */
    private enum MarkMenuState {

        IDLE(false),
        MARKING(false),
        MENU(true),
        VISIBLE(true),
        INVISIBLE(false);
        
        public final boolean isMenuVisible;

        private MarkMenuState(boolean isMenuVisible) {
            this.isMenuVisible = isMenuVisible;
        }
    }
    private class MarkingMenuTimer extends TimerTask {
        MarkingMenuTimer() {}
        @Override
        public void run() {
            timerOut();
        }
    }
}
