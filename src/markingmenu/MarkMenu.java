/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markingmenu;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author AdrienSIEGFRIED
 */
public class MarkMenu extends JPanel {
    
    public static final String ACT_SHOW_MENU = "showing menu";
    public static final String ACT_HIDE_MENU = "hiding menu";
    public static final String ACT_WAIT_MARK = "waiting mark";   
    
    private static final long DELAY = 1000;
    
    private MarkMenuState state;
    private List<PiePart> myPieParts;
    private Point menuCenter = new Point(0, 0);
    private boolean isMenuVisible = false;

    private Timer timer;
    
    public MarkMenu() {
        myPieParts = new ArrayList<PiePart>();
        setState(MarkMenuState.IDLE);
        MarkMouseAdapter mma = new MarkMouseAdapter();
        addMouseListener(mma);
        addMouseMotionListener(mma);
    }   
    
    private void displayMenu() {
        if (myPieParts.size() >= 1) return;
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        
        int i = 0;
        int nbOfPieParts = this.myPieParts.size();
        for (PiePart p: myPieParts) {
            p.setCenterPoint(mousePosition);
            p.setStartAngle(i*nbOfPieParts);
            p.setExtendAngle((i+1)*nbOfPieParts);
            i++;
        }
        isMenuVisible = true;       
        repaint();
    }
    private void hideMenu() {
        isMenuVisible = false;
        repaint();
    }
           
    private void setState(MarkMenuState markMenuState) {
        this.state = markMenuState;
        System.out.println("State is now: "+markMenuState);
    }
    private MarkMenuState getState() {
        return this.state;
    }
    
    public void addItem(String label) {
        this.addItem(label, new Callable() {
            @Override
            public void execute() {
                System.out.println("Executing: "+label);
            }
        });
    }
    
    public void  addItem(String label, Callable func) {
        // TODO: item needs to know what to execute
        myPieParts.add(new PiePart(label));
    }
    
    protected void paintComponent(Graphics g) {        
        if (isMenuVisible) {
            this.myPieParts.forEach((PiePart) -> setVisible(true));
        }
    }
        
    
    /**
     * Mouse adapter for the marking menu
     */
    private class MarkMouseAdapter extends MouseAdapter {

        public MarkMouseAdapter() {}
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                switch(getState()) {
                    case IDLE:
                        setState(MarkMenuState.IDLE);
                        break;
                    case MARKING:
                        // Impossible
                        break;
                    case MENU:
                        // PiePart should execute command if mouse hover
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
            System.out.println("Pressed");
            if (SwingUtilities.isRightMouseButton(e)) {
                switch(getState()) {
                    case IDLE:
                        timer = new Timer();
                        timer.schedule(new MarkingMenuTimer(), DELAY);
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
            switch(getState()) {
                case IDLE:
                    setState(MarkMenuState.IDLE);
                    break;
                case MARKING:
                    // Impossible
                    break;
                case MENU:
                    // PiePart should be highlighted
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
            System.out.println("Released");
            if (SwingUtilities.isRightMouseButton(e)) {
                switch(getState()) {
                    case IDLE:
                        // Impossible
                        break;
                    case MARKING:
                        timer.cancel();
                        displayMenu();
                        // Execute item if one selected
                        setState(MarkMenuState.MENU);
                        break;
                    case MENU:
                        // Impossible
                        break;
                    case VISIBLE:
                        hideMenu();
                        // A PiePart should be executing an action
                        setState(MarkMenuState.IDLE);
                        break;
                    case INVISIBLE:
                        // A PiePart should be executing an action
                        setState(MarkMenuState.IDLE);
                        break;
                    default:
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                switch(getState()) {
                    case IDLE:
                        // Impossible
                        break;
                    case MARKING:
                        timer.cancel();
                        setState(MarkMenuState.INVISIBLE);
                        break;
                    case MENU:
                        // Impossible
                        break;
                    case VISIBLE:
                        // A PiePart should be highlighted if hover
                        setState(MarkMenuState.VISIBLE);
                        break;
                    case INVISIBLE:
                        // A PiePart should be registering the selection
                        setState(MarkMenuState.INVISIBLE);
                        break;
                    default:
                }
            }
        }
    }
    /** 
     * Timer manager for MarkMenu
     */
    private class MarkingMenuTimer extends TimerTask {
        MarkingMenuTimer() {
        }
        @Override
        public void run() {
            timerOut();
        }
        protected void timerOut() {
            System.out.println("time out");
            switch(getState()) {
                case IDLE:
                    // Forbidden
                    break;
                case MARKING:
                     // should fire visibility
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
    
    // TODO use package test when stable
    public static void main(String s[]) {
        MarkMenu mm = new MarkMenu();
        mm.addItem("One");
        mm.addItem("Two");
        mm.addItem("Three");
        JFrame frame = new JFrame("MarkMenu Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mm);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
