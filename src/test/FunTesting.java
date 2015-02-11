/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import markingmenu.Callable;
import markingmenu.MarkMenu;

/**
 *
 * @author AdrienSIEGFRIED
 */
public class FunTesting extends JPanel {
    
        private int circle_radius = 25;
        public int circle_x = 100;
        public int circle_y = 100;
    
	@Override
	public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.drawOval(this.circle_x, circle_y, circle_radius, circle_radius);		
	}
	
	public static void main(String[] args) {
            JFrame frame = new JFrame("Mini Tennis");
            FunTesting ball = new FunTesting();
            frame.setSize(450, 450);
            MarkMenu mm = new MarkMenu();
            mm.addItem("UpRight", new Callable() {
                @Override
                public void execute() {
                    ball.circle_x += 10;
                    ball.circle_y -= 10;
                    ball.repaint();
                }
            });
            mm.addItem("UpLeft", new Callable() {

                @Override
                public void execute() {
                    ball.circle_x -= 10;
                    ball.circle_y -= 10;
                    ball.repaint();
                }
            });
            mm.addItem("DownLeft", new Callable() {

                @Override
                public void execute() {
                    ball.circle_x -= 10;
                    ball.circle_y += 10;
                    ball.repaint();
                }
            });
            mm.addItem("DownRight", new Callable() {

                @Override
                public void execute() {
                    ball.circle_x += 10;
                    ball.circle_y += 10;
                    ball.repaint();
                }
            });
            frame.setContentPane(mm);
            frame.add(ball);            
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
