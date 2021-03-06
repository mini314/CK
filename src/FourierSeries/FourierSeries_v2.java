package FourierSeries;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mini
 */

public class FourierSeries_v2 extends javax.swing.JApplet {
    final private int INITIAL_WIDTH = 30;
    final private int INITIAL_N_MAX = 10;
    
    private double width = ((double) INITIAL_WIDTH)/100.0*Math.PI; // width of the trial function
    private int n_max = INITIAL_N_MAX; // the largest order for partial sumation
    
class MainGraphPanel extends JPanel {
        // Proportion of margin of panel
        final double MARGIN_OF_RIGHT = 0.05; // for right
        final double MARGIN_OF_LEFT = 0.05; // for left
        final double MARGIN_OF_ABOVE = 0.1; // for upper side
        final double MARGIN_OF_BELOW = 0.2; // for lower side
        // Width and height of panel
        int WIDTH_OF_PANEL;
        int HEIGHT_OF_PANEL;
        // Virtual maximum and minimum of coordinates
        final double VIRTUAL_X_MIN = -4.0;
        final double VIRTUAL_X_MAX = 4.0;
        final double VIRTUAL_Y_MIN = 0.0;
        final double VIRTUAL_Y_MAX = 2.0;

        public MainGraphPanel(){
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
        // class for the trial function
        private class trialFunction extends Path2D.Double{
            public trialFunction(double width){
                this.moveTo(virtualX(-Math.PI),virtualY(0.0));
                this.lineTo(virtualX(-width), virtualY(0.0));
                this.lineTo(virtualX(-width), virtualY(1.0));
                this.lineTo(virtualX(width), virtualY(1.0));
                this.lineTo(virtualX(width), virtualY(0.0));
                this.lineTo(virtualX(Math.PI), virtualY(0.0));
            }
        }
        // class for the Fourier expansion upto n-th order
        private class fourierSum extends Path2D.Double{
            private double width; // width of the trial function
            private double n_max; // the largest order for partial sumation
            private double[] coeficientOfCos;  // coeficient of n-th cos fourier term a.k.a. a_n
            private double partialSumation(double x){ // the value of partial sumation upto n_max at x
                double result = 0.0;
                for(int n=0;n<=n_max;n++)
                    result += coeficientOfCos[n]*Math.cos((double)n*x);
                return result;
            }
            public fourierSum(double width, int n_max){ // get width of the trial function and the largest order n_max
                this.width = width;
                this.n_max = n_max;
                this.coeficientOfCos = new double[n_max+1];
                
                coeficientOfCos[0] = width/Math.PI; // zeroth order coeficient
                for(int n=1; n<=n_max; n++)
                    coeficientOfCos[n]=2.*Math.sin(width*n)/(double)n/Math.PI; // n-th order coeficient
            
                this.moveTo(virtualX(-Math.PI), virtualY(partialSumation(-Math.PI)));
                for(double x=-Math.PI+0.01;x<Math.PI+0.01;x+=0.01){
                    this.lineTo(virtualX(x), virtualY(partialSumation(x)));
                }
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
        
        // Drawing the box-shaped trial function
        g2.setPaint(Color.blue);
        g2.draw(new trialFunction(width));

        // Drawing the partial sumation of Fourier series
        g2.setPaint(Color.red);
        g2.draw(new fourierSum(width,n_max));
    }
}

class CoefficientGraphPanel extends JPanel {
        // Proportion of margin of panel
        final double MARGIN_OF_RIGHT = 0.05; // for right
        final double MARGIN_OF_LEFT = 0.05; // for left
        final double MARGIN_OF_ABOVE = 0.1; // for upper side
        final double MARGIN_OF_BELOW = 0.2; // for lower side
        // Width and height of panel
        int WIDTH_OF_PANEL;
        int HEIGHT_OF_PANEL;
        // Virtual maximum and minimum of coordinates
        final double VIRTUAL_X_MIN = 0.0;
        final double VIRTUAL_X_MAX = (double) INITIAL_N_MAX+1.0;
        final double VIRTUAL_Y_MIN = -width/Math.PI;
        final double VIRTUAL_Y_MAX = 2.0*width/Math.PI;

        public CoefficientGraphPanel(){
        }

        /*
        * Following two functions are degined
        * to convert virtual coordinates
        * from (VIRTUAL_X_MIN,VIRTUAL_Y_MIN) to (VIRTUAL_X_MAX,VIRTUAL_Y_MAX)
        * into real coordinates.
        */
        private double virtualX(double x) { // get virtual x coordinate
            final double VIRTUAL_X_MIN = 0.0;
            final double VIRTUAL_X_MAX = (double) n_max+1.0;
            return (x-VIRTUAL_X_MIN)*WIDTH_OF_PANEL*(1.0-MARGIN_OF_RIGHT-MARGIN_OF_LEFT)/(VIRTUAL_X_MAX-VIRTUAL_X_MIN)+WIDTH_OF_PANEL*MARGIN_OF_LEFT;
        }
        private double virtualY(double y) { // get vritual y coordinate
            final double VIRTUAL_Y_MIN = -width/Math.PI;
            final double VIRTUAL_Y_MAX = 2.0*width/Math.PI;
            return (VIRTUAL_Y_MAX-y)*HEIGHT_OF_PANEL*(1.0-MARGIN_OF_ABOVE-MARGIN_OF_BELOW)/(VIRTUAL_Y_MAX-VIRTUAL_Y_MIN)+HEIGHT_OF_PANEL*MARGIN_OF_ABOVE;
        }
        // class for axis
        private class axis extends Path2D.Double{
            public axis(){
                final double VIRTUAL_X_MIN = 0.0;
                final double VIRTUAL_X_MAX = (double) n_max+1.0;
                final double VIRTUAL_Y_MIN = -width/Math.PI;
                final double VIRTUAL_Y_MAX = 2.0*width/Math.PI;
                
                this.moveTo(virtualX(VIRTUAL_X_MIN),virtualY(0.0));
                this.lineTo(virtualX(VIRTUAL_X_MAX), virtualY(0.0));
                this.moveTo(virtualX(0.0),virtualY(VIRTUAL_Y_MIN));
                this.lineTo(virtualX(0.0), virtualY(VIRTUAL_Y_MAX));
            }
        }
        
        // 
        private class coeficients extends Path2D.Double{
            private double width; // width of the trial function
            private double n_max; // the largest order for partial sumation
            public coeficients(double width, int n_max){ // get width of the trial function and the largest order n_max
                this.width = width;
                this.n_max = n_max;
                
                this.moveTo(virtualX(0.0), virtualY(0.0));
                this.lineTo(virtualX(0.0), virtualY(width/Math.PI));
                for(int n=1; n<=n_max; n++) {
                        this.moveTo(virtualX((double)n), virtualY(0.0));
                        this.lineTo(virtualX((double)n), virtualY(2.*Math.sin(width*n)/(double)n/Math.PI));
                    }
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

        // Drawing the partial sumation of Fourier series
        g2.setPaint(Color.red);
        g2.draw(new coeficients(width,n_max));
    }
}

    /**
     * Initializes the applet FourierSeriesII
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
            java.util.logging.Logger.getLogger(FourierSeries_v2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FourierSeries_v2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FourierSeries_v2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FourierSeries_v2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
        mainGraphPanel = new MainGraphPanel();
        coefficientPanel = new CoefficientGraphPanel();
        jPanel3 = new javax.swing.JPanel();
        widthSlider = new javax.swing.JSlider();
        nMaxSlider = new javax.swing.JSlider();
        widthLabel = new javax.swing.JLabel();
        nMaxLabel = new javax.swing.JLabel();
        shapeLabel = new javax.swing.JLabel();
        rectangularRadioButton = new javax.swing.JRadioButton();
        triangleRadioButton = new javax.swing.JRadioButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jInternalFrame1.setVisible(true);

        mainGraphPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mainGraphPanel.setToolTipText("");
        mainGraphPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout mainGraphPanelLayout = new javax.swing.GroupLayout(mainGraphPanel);
        mainGraphPanel.setLayout(mainGraphPanelLayout);
        mainGraphPanelLayout.setHorizontalGroup(
            mainGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 528, Short.MAX_VALUE)
        );
        mainGraphPanelLayout.setVerticalGroup(
            mainGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );

        coefficientPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout coefficientPanelLayout = new javax.swing.GroupLayout(coefficientPanel);
        coefficientPanel.setLayout(coefficientPanelLayout);
        coefficientPanelLayout.setHorizontalGroup(
            coefficientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        coefficientPanelLayout.setVerticalGroup(
            coefficientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        widthSlider.setMinimum(1);
        widthSlider.setValue(INITIAL_WIDTH);
        widthSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                widthSliderStateChanged(evt);
            }
        });

        nMaxSlider.setValue(INITIAL_N_MAX);
        nMaxSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nMaxSliderStateChanged(evt);
            }
        });

        widthLabel.setText("폭");

        nMaxLabel.setText("최고차항");

        shapeLabel.setText("모양");

        buttonGroup1.add(rectangularRadioButton);
        rectangularRadioButton.setSelected(true);
        rectangularRadioButton.setText("사각형");
        rectangularRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectangularRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(triangleRadioButton);
        triangleRadioButton.setText("삼각형");
        triangleRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triangleRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(widthSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(nMaxSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rectangularRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(triangleRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(widthLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shapeLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nMaxLabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(widthLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(widthSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nMaxLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nMaxSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(shapeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rectangularRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(triangleRadioButton)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jInternalFrame1.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(coefficientPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mainGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(mainGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(coefficientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        mainGraphPanel.getAccessibleContext().setAccessibleName("");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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

    private void triangleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triangleRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_triangleRadioButtonActionPerformed

    private void rectangularRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectangularRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rectangularRadioButtonActionPerformed

    private void nMaxSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nMaxSliderStateChanged
        // TODO add your handling code here:
        n_max = nMaxSlider.getValue();
        mainGraphPanel.repaint();
        coefficientPanel.repaint();
    }//GEN-LAST:event_nMaxSliderStateChanged

    private void widthSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_widthSliderStateChanged
        // TODO add your handling code here:
        width = ((double)widthSlider.getValue())/100.0*Math.PI;
        mainGraphPanel.repaint();
        coefficientPanel.repaint();
    }//GEN-LAST:event_widthSliderStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel coefficientPanel;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel mainGraphPanel;
    private javax.swing.JLabel nMaxLabel;
    private javax.swing.JSlider nMaxSlider;
    private javax.swing.JRadioButton rectangularRadioButton;
    private javax.swing.JLabel shapeLabel;
    private javax.swing.JRadioButton triangleRadioButton;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JSlider widthSlider;
    // End of variables declaration//GEN-END:variables
}
