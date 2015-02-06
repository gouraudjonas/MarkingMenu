/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testmarkingmenu;

/**
 *
 * @author sacapuce
 */
public class FrameTest extends javax.swing.JFrame {

    /**
     * Creates new form FrameTest
     */
    public FrameTest() {
        initComponents();
        
        piePart1.setStartAngle(0);
        piePart2.setStartAngle(90);
        piePart3.setStartAngle(180);
        piePart4.setStartAngle(270);
        
        piePart1.setVisible(false);
        piePart2.setVisible(false);
        piePart3.setVisible(false);
        piePart4.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        piePart1 = new testmarkingmenu.PiePart();
        piePart2 = new testmarkingmenu.PiePart();
        piePart3 = new testmarkingmenu.PiePart();
        piePart4 = new testmarkingmenu.PiePart();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(200, 200, 500, 500));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));
        getContentPane().add(piePart1);
        getContentPane().add(piePart2);
        getContentPane().add(piePart3);
        getContentPane().add(piePart4);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    }//GEN-LAST:event_formMouseClicked

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved

    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        System.out.println("DEBUG > clicked at " + evt.getX() + "-" + evt.getY());

        piePart1.updatePosition(evt.getX(), evt.getY());
        piePart1.setVisible(true);
        piePart1.repaint();
        
        piePart2.updatePosition(evt.getX(), evt.getY());
        piePart2.setVisible(true);
        piePart2.repaint();

        piePart3.updatePosition(evt.getX(), evt.getY());
        piePart3.setVisible(true);
        piePart3.repaint();
        
        piePart4.updatePosition(evt.getX(), evt.getY());
        piePart4.setVisible(true);
        piePart4.repaint();
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        piePart1.setVisible(false);
        piePart2.setVisible(false);
        piePart3.setVisible(false);
        piePart4.setVisible(false);
    }//GEN-LAST:event_formMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameTest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private testmarkingmenu.PiePart piePart1;
    private testmarkingmenu.PiePart piePart2;
    private testmarkingmenu.PiePart piePart3;
    private testmarkingmenu.PiePart piePart4;
    // End of variables declaration//GEN-END:variables
}