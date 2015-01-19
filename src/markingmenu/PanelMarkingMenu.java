/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markingmenu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author gouraujo
 */
public class PanelMarkingMenu extends JPanel {

    public JPopupMenu popup;
    public boolean showed;
    public static int MENU_ITEMS = 4;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Marking menu of the death");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new PanelMarkingMenu());
        frame.setSize(300, 300);

        // java - get screen size using the Toolkit class
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) screenSize.getWidth() / 2,
                (int) screenSize.getHeight() / 2);
        frame.setVisible(true);
    }

    public PanelMarkingMenu() {
        // Variables initializing
        popup = new JPopupMenu();
        ActionListener menuListener = (ActionEvent event) -> {
            System.out.println("Popup menu item ["
                    + event.getActionCommand() + "] was pressed.");
        };
        showed = false;

        JMenuItem item;
        popup.add(item = new JMenuItem("Left"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("Center"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("Right"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("Full"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.addSeparator();
        popup.add(item = new JMenuItem("Settings . . ."));
        item.addActionListener(menuListener);
        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        popup.addPopupMenuListener(new PopupPrintListener());

        addMouseListener(new MousePopupListener());
    }

    // An inner class to check whether mouse events are the popup trigger
    class MousePopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        private void checkPopup(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (popup.isVisible()) {
                    System.out.println("DEBUG > Pop up menu is visible");
                    popup.setVisible(false);
                } else {
                    System.out.println("DEBUG > Pop up menu is not visible");
                    popup.show(PanelMarkingMenu.this, e.getX(), e.getY());
                }
            }
        }
    }

    // An inner class to show when popup events occur
    class PopupPrintListener implements PopupMenuListener {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be visible!");
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be invisible!");
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            System.out.println("Popup menu is hidden!");
        }
    }

    public JComponent[] createMenuItems() {
        JComponent[] items = new JComponent[MENU_ITEMS];
        int step = 360 / MENU_ITEMS;

        if (MENU_ITEMS != 0) {
            for (int i = 1; i <= MENU_ITEMS; i++) {
                items[i] = new PiePart(step * (i - 1), step * i);
            }
        }

        return items;
    }
}
