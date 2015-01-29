/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IdealGas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class IdealGas_v1 extends javax.swing.JApplet {

    final private double TEMPERATURE_MAX = 1.0;
    final private double PRESSURE_MAX = 1.0;
    final private double VOLUME_MAX = 1.0;
    
    class VPGraphPanel extends JPanel {
        // Proportion of margin of panel
        final double MARGIN_OF_RIGHT = 0.1; // for right
        final double MARGIN_OF_LEFT = 0.1; // for left
        final double MARGIN_OF_ABOVE = 0.1; // for upper side
        final double MARGIN_OF_BELOW = 0.1; // for lower side
        // Width and height of panel
        int WIDTH_OF_PANEL;
        int HEIGHT_OF_PANEL;
        // Virtual maximum and minimum of coordinates
        final double VIRTUAL_X_MIN = 0.0;
        final double VIRTUAL_X_MAX = VOLUME_MAX;
        final double VIRTUAL_Y_MIN = 0.0;
        final double VIRTUAL_Y_MAX = PRESSURE_MAX;

        public VPGraphPanel(){
        }

        /*
        * Following two functions are degined
        * to convert virtual coordinates
        * from (VIRTUAL_X_MIN,VIRTUAL_Y_MIN) to (VIRTUAL_X_MAX,VIRTUAL_Y_MAX)
        * into real coordinates.
        */
        private double virtualX(double x) { // get virtual x coordinate
            return (x-VIRTUAL_X_MIN)*WIDTH_OF_PANEL*(1.0-MARGIN_OF_RIGHT-MARGIN_OF_LEFT)/(VIRTUAL_X_MAX-VIRTUAL_X_MIN)+WIDTH_OF_PANEL*MARGIN_OF_LEFT;
        }
        private double virtualY(double y) { // get vritual y coordinate
            return (VIRTUAL_Y_MAX-y)*HEIGHT_OF_PANEL*(1.0-MARGIN_OF_ABOVE-MARGIN_OF_BELOW)/(VIRTUAL_Y_MAX-VIRTUAL_Y_MIN)+HEIGHT_OF_PANEL*MARGIN_OF_ABOVE;
        }
        // class for axis
        private class axis extends Path2D.Double{
            public axis(){
                this.moveTo(virtualX(VIRTUAL_X_MIN),virtualY(0.0));
                this.lineTo(virtualX(VIRTUAL_X_MAX), virtualY(0.0));
                this.moveTo(virtualX(0.0),virtualY(VIRTUAL_Y_MIN));
                this.lineTo(virtualX(0.0), virtualY(VIRTUAL_Y_MAX));
            }
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            // Width and height of panel
            WIDTH_OF_PANEL = getWidth();
            HEIGHT_OF_PANEL = getHeight();
        
            // Drawing x-axis and y-axis
            g2.setPaint(Color.black);
            g2.draw(new axis());
        }
    }
    
    class TVGraphPanel extends JPanel {
        // Proportion of margin of panel
        final double MARGIN_OF_RIGHT = 0.1; // for right
        final double MARGIN_OF_LEFT = 0.1; // for left
        final double MARGIN_OF_ABOVE = 0.1; // for upper side
        final double MARGIN_OF_BELOW = 0.1; // for lower side
        // Width and height of panel
        int WIDTH_OF_PANEL;
        int HEIGHT_OF_PANEL;
        // Virtual maximum and minimum of coordinates
        final double VIRTUAL_X_MIN = 0.0;
        final double VIRTUAL_X_MAX = TEMPERATURE_MAX;
        final double VIRTUAL_Y_MIN = 0.0;
        final double VIRTUAL_Y_MAX = VOLUME_MAX;

        public TVGraphPanel(){
        }

        /*
        * Following two functions are degined
        * to convert virtual coordinates
        * from (VIRTUAL_X_MIN,VIRTUAL_Y_MIN) to (VIRTUAL_X_MAX,VIRTUAL_Y_MAX)
        * into real coordinates.
        */
        private double virtualX(double x) { // get virtual x coordinate
            return (x-VIRTUAL_X_MIN)*WIDTH_OF_PANEL*(1.0-MARGIN_OF_RIGHT-MARGIN_OF_LEFT)/(VIRTUAL_X_MAX-VIRTUAL_X_MIN)+WIDTH_OF_PANEL*MARGIN_OF_LEFT;
        }
        private double virtualY(double y) { // get vritual y coordinate
            return (VIRTUAL_Y_MAX-y)*HEIGHT_OF_PANEL*(1.0-MARGIN_OF_ABOVE-MARGIN_OF_BELOW)/(VIRTUAL_Y_MAX-VIRTUAL_Y_MIN)+HEIGHT_OF_PANEL*MARGIN_OF_ABOVE;
        }
        // class for axis
        private class axis extends Path2D.Double{
            public axis(){
                this.moveTo(virtualX(VIRTUAL_X_MIN),virtualY(0.0));
                this.lineTo(virtualX(VIRTUAL_X_MAX), virtualY(0.0));
                this.moveTo(virtualX(0.0),virtualY(VIRTUAL_Y_MIN));
                this.lineTo(virtualX(0.0), virtualY(VIRTUAL_Y_MAX));
            }
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            // Width and height of panel
            WIDTH_OF_PANEL = getWidth();
            HEIGHT_OF_PANEL = getHeight();
        
            // Drawing x-axis and y-axis
            g2.setPaint(Color.black);
            g2.draw(new axis());
        }
    }
    
    /**
     * Initializes the applet NewJApplet
     */
    @Override
    public void init() {
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
            java.util.logging.Logger.getLogger(IdealGas_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IdealGas_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IdealGas_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IdealGas_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        AboveGraphPanel = new VPGraphPanel();
        BelowGraphPanel = new TVGraphPanel();
        GraphicPanel = new javax.swing.JPanel();
        ControlPanel = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();

        jInternalFrame1.setVisible(true);

        AboveGraphPanel.setBackground(new java.awt.Color(255, 255, 255));
        AboveGraphPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout AboveGraphPanelLayout = new javax.swing.GroupLayout(AboveGraphPanel);
        AboveGraphPanel.setLayout(AboveGraphPanelLayout);
        AboveGraphPanelLayout.setHorizontalGroup(
            AboveGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );
        AboveGraphPanelLayout.setVerticalGroup(
            AboveGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
        );

        BelowGraphPanel.setBackground(new java.awt.Color(255, 255, 255));
        BelowGraphPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout BelowGraphPanelLayout = new javax.swing.GroupLayout(BelowGraphPanel);
        BelowGraphPanel.setLayout(BelowGraphPanelLayout);
        BelowGraphPanelLayout.setHorizontalGroup(
            BelowGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );
        BelowGraphPanelLayout.setVerticalGroup(
            BelowGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 163, Short.MAX_VALUE)
        );

        GraphicPanel.setBackground(new java.awt.Color(255, 255, 255));
        GraphicPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout GraphicPanelLayout = new javax.swing.GroupLayout(GraphicPanel);
        GraphicPanel.setLayout(GraphicPanelLayout);
        GraphicPanelLayout.setHorizontalGroup(
            GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        GraphicPanelLayout.setVerticalGroup(
            GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        ControlPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("volume");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("temperature");

        javax.swing.GroupLayout ControlPanelLayout = new javax.swing.GroupLayout(ControlPanel);
        ControlPanel.setLayout(ControlPanelLayout);
        ControlPanelLayout.setHorizontalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlPanelLayout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(ControlPanelLayout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addGap(34, 34, 34)))
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        ControlPanelLayout.setVerticalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AboveGraphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BelowGraphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(GraphicPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(AboveGraphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BelowGraphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(GraphicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jInternalFrame1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jInternalFrame1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AboveGraphPanel;
    private javax.swing.JPanel BelowGraphPanel;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JPanel GraphicPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    // End of variables declaration//GEN-END:variables
}
