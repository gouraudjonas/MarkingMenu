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
package test;

import java.awt.Color;
import java.awt.Graphics;
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
