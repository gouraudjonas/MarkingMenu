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
import markingmenu.Callable;
import markingmenu.MarkMenu;

/**
 *
 * @author AdrienSIEGFRIED
 */
public class FunTesting extends JFrame {

    public final int circle_radius = 25;
    public int circle_x = 100;
    public int circle_y = 100;

    private MarkMenu mm;

    /*@Override
     public void paint(Graphics g) {
     Graphics2D g2d = (Graphics2D) g;
     g2d.setColor(Color.RED);
     g2d.drawOval(this.circle_x, circle_y, circle_radius, circle_radius);
     }*/
    public FunTesting() {
        mm = new MarkMenu(this);
        mm.addItem("UpRight", new Callable() {
            @Override
            public void execute() {
                move("UpRight");
                repaint();
            }
        });
        mm.addItem("UpLeft", new Callable() {

            @Override
            public void execute() {
                move("UpLeft");
                repaint();
            }
        });
        mm.addItem("DownLeft", new Callable() {

            @Override
            public void execute() {
                move("DownLeft");
                repaint();
            }
        });
        mm.addItem("DownRight", new Callable() {

            @Override
            public void execute() {
                move("DownRight");
                repaint();
            }
        });

        this.setTitle("Fun testing of Marking Menu");
        this.setSize(450, 450);
        this.setContentPane(mm);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void move(String direction) {
        switch (direction) {
            case "UpRight":
                circle_x += 10;
                circle_y -= 10;
                break;
            case "UpLeft":
                circle_x -= 10;
                circle_y -= 10;
                break;
            case "DownLeft":
                circle_x -= 10;
                circle_y += 10;
                break;
            case "DownRight":
                circle_x += 10;
                circle_y += 10;
                break;
            default:
                break;
        }
    }

    public void action(Graphics g) {
        g.setColor(Color.RED);
        g.drawOval(circle_x, circle_y, circle_radius, circle_radius);
    }

    public static void main(String[] args) {
        FunTesting ball = new FunTesting();
    }
}
