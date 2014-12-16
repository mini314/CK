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

public class FourierSeries_v3 extends javax.swing.JApplet {
    interface SHAPE {
        final int LECTANGULAR = 0;
        final int TRIANGLE = 1;
        final int FORMULA = 2;
        final int COSINE = 3;
    }
    
    final private int INITIAL_WIDTH = 30;
    final private int INITIAL_N_MAX = 10;
    final private int INITIAL_SHAPE = SHAPE.LECTANGULAR;
    
    private double width = ((double) INITIAL_WIDTH)/100.0*Math.PI; // width of the trial function
    private int n_max = INITIAL_N_MAX; // the largest order for partial sumation
    private int shape = INITIAL_SHAPE;
    
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
            public trialFunction(){
                final double dx = width*0.01;
                switch (shape){
                    case SHAPE.LECTANGULAR :
                        this.moveTo(virtualX(-Math.PI),virtualY(0.0));
                        this.lineTo(virtualX(-width), virtualY(0.0));
                        this.lineTo(virtualX(-width), virtualY(1.0));
                        this.lineTo(virtualX(width), virtualY(1.0));
                        this.lineTo(virtualX(width), virtualY(0.0));
                        this.lineTo(virtualX(Math.PI), virtualY(0.0));
                        break;
                    case SHAPE.TRIANGLE :
                        this.moveTo(virtualX(-Math.PI),virtualY(0.0));
                        this.lineTo(virtualX(-width), virtualY(0.0));
                        this.lineTo(virtualX(0.0), virtualY(1.0));
                        this.lineTo(virtualX(width), virtualY(0.0));
                        this.lineTo(virtualX(Math.PI), virtualY(0.0));
                        break;
                    case SHAPE.FORMULA :
                        this.moveTo(virtualX(-Math.PI),virtualY(0.0));
                        for(double x = -width;x<width;x+=dx)
                            this.lineTo(virtualX(x), virtualY((width*width-x*x)/(width)/(width)));
                        this.lineTo(virtualX(width), virtualY(0.0));
                        this.lineTo(virtualX(Math.PI), virtualY(0.0));
                        break;
                    case SHAPE.COSINE :
                        this.moveTo(virtualX(-Math.PI),virtualY(0.0));
                        for(double x = -width;x<width;x+=dx)
                            this.lineTo(virtualX(x), virtualY(Math.cos(x*Math.PI/(2.*width))));
                        this.lineTo(virtualX(width), virtualY(0.0));
                        this.lineTo(virtualX(Math.PI), virtualY(0.0));
                        break;
                }
            }
        }
        // class for the Fourier expansion upto n-th order
        private class fourierSum extends Path2D.Double{
            private double[] coeficientOfCos;  // coeficient of n-th cos fourier term a.k.a. a_n
            private double partialSumation(double x){ // the value of partial sumation upto n_max at x
                double result = 0.0;
                for(int n=0;n<=n_max;n++)
                    result += coeficientOfCos[n]*Math.cos((double)n*x);
                return result;
            }
            public fourierSum(){ // get width of the trial function and the largest order n_max
                this.coeficientOfCos = new double[n_max+1];
                switch (shape){
                    case SHAPE.LECTANGULAR :
                        coeficientOfCos[0] = width/Math.PI; // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=2./Math.PI*Math.sin(width*n)/n; // n-th order coeficient
                        break;
                    case SHAPE.TRIANGLE :
                        coeficientOfCos[0] = width/(2.*Math.PI); // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=2./Math.PI*(1.-Math.cos(width*n))/(width*n*n); // n-th order coeficient
                        break;
                    case SHAPE.FORMULA :
                        coeficientOfCos[0] = 2.*width/(3.*Math.PI); // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=4./Math.PI*(Math.sin(width*n)/(width*width*n*n*n)-Math.cos(width*n)/(width*n*n)); // n-th order coeficient
                        break;
                    case SHAPE.COSINE :
                        coeficientOfCos[0] = 2.*width/Math.PI/Math.PI; // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=1./Math.PI*((Math.sin(width*(Math.PI/2./width+n)))/(Math.PI/2./width+n)
                                                           +(Math.sin(width*(Math.PI/2./width-n)))/(Math.PI/2./width-n)); // n-th order coeficient
                        break;
                }
            
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
        g2.draw(new trialFunction());

        // Drawing the partial sumation of Fourier series
        g2.setPaint(Color.red);
        g2.draw(new fourierSum());
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
        double VIRTUAL_X_MIN = 0.0;
        double VIRTUAL_X_MAX = (double) INITIAL_N_MAX+1.0;
        double VIRTUAL_Y_MIN = -width/Math.PI;
        double VIRTUAL_Y_MAX = 2.0*width/Math.PI;
        
        public CoefficientGraphPanel(){
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
                VIRTUAL_X_MIN = 0.0;
                VIRTUAL_X_MAX = (double) n_max+1.0;
                switch (shape){
                    case SHAPE.LECTANGULAR :
                        VIRTUAL_Y_MIN = -width/Math.PI;
                        VIRTUAL_Y_MAX = 3.0*width/Math.PI;
                        break;
                    case SHAPE.TRIANGLE :
                        VIRTUAL_Y_MIN = -width/(2.*Math.PI);
                        VIRTUAL_Y_MAX = 3.0*width/(2.*Math.PI);
                        break;
                    case SHAPE.FORMULA :
                        VIRTUAL_Y_MIN = -2.*width/(3.*Math.PI);
                        VIRTUAL_Y_MAX = 3.0*2.*width/(3.*Math.PI);
                        break;
                    case SHAPE.COSINE :
                        VIRTUAL_Y_MIN = -2.*width/Math.PI/Math.PI;
                        VIRTUAL_Y_MAX = 4.0*width/Math.PI/Math.PI;
                        break;
                }
                
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
            private double[] coeficientOfCos;
            public coeficients(double width, int n_max){ // get width of the trial function and the largest order n_max
                this.width = width;
                this.n_max = n_max;
                
                coeficientOfCos = new double[n_max+1];
                switch (shape){
                    case SHAPE.LECTANGULAR :
                        coeficientOfCos[0] = width/Math.PI; // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=2./Math.PI*Math.sin(width*n)/n; // n-th order coeficient
                        break;
                    case SHAPE.TRIANGLE :
                        coeficientOfCos[0] = width/(2.*Math.PI); // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=2./Math.PI*(1.-Math.cos(width*n))/(width*n*n); // n-th order coeficient
                        break;
                    case SHAPE.FORMULA :
                        coeficientOfCos[0] = 2.*width/(3.*Math.PI); // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=4./Math.PI*(Math.sin(width*n)/(width*width*n*n*n)-Math.cos(width*n)/(width*n*n)); // n-th order coeficient
                        break;
                    case SHAPE.COSINE :
                        coeficientOfCos[0] = 2.*width/Math.PI/Math.PI; // zeroth order coeficient
                        for(int n=1; n<=n_max; n++)
                            coeficientOfCos[n]=1./Math.PI*((Math.sin(width*(Math.PI/2./width+n)))/(Math.PI/2./width+n)
                                                           +(Math.sin(width*(Math.PI/2./width-n)))/(Math.PI/2./width-n)); // n-th order coeficient
                        break;
                }
                
                this.moveTo(virtualX(0.0), virtualY(0.0));
                this.lineTo(virtualX(0.0), virtualY(coeficientOfCos[0]));
                this.lineTo(virtualX(0.0+0.5), virtualY(coeficientOfCos[0]));
                this.lineTo(virtualX(0.0+0.5), virtualY(0.0));
                for(int n=1; n<=n_max; n++) {
                        this.moveTo(virtualX((double)n-0.5), virtualY(0.0));
                        this.lineTo(virtualX((double)n-0.5), virtualY(coeficientOfCos[n]));
                        this.lineTo(virtualX((double)n+0.5), virtualY(coeficientOfCos[n]));
                        this.lineTo(virtualX((double)n+0.5), virtualY(0.0));
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
            java.util.logging.Logger.getLogger(FourierSeries_v3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FourierSeries_v3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FourierSeries_v3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FourierSeries_v3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

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
        nMaxSpinner = new javax.swing.JSpinner();
        formulaRadioButton = new javax.swing.JRadioButton();
        cosineRadioButton = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jInternalFrame1.setVisible(true);

        mainGraphPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainGraphPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mainGraphPanel.setToolTipText("");
        mainGraphPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout mainGraphPanelLayout = new javax.swing.GroupLayout(mainGraphPanel);
        mainGraphPanel.setLayout(mainGraphPanelLayout);
        mainGraphPanelLayout.setHorizontalGroup(
            mainGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        mainGraphPanelLayout.setVerticalGroup(
            mainGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 193, Short.MAX_VALUE)
        );

        coefficientPanel.setBackground(new java.awt.Color(255, 255, 255));
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

        nMaxSlider.setMaximum(99);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, nMaxSpinner, org.jdesktop.beansbinding.ELProperty.create("${value}"), nMaxSlider, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        nMaxSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nMaxSliderStateChanged(evt);
            }
        });

        widthLabel.setText("폭");

        nMaxLabel.setText("최고차항");

        shapeLabel.setText("함수 모양");

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

        nMaxSpinner.setValue(INITIAL_N_MAX);

        buttonGroup1.add(formulaRadioButton);
        formulaRadioButton.setText("포물선");
        formulaRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formulaRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(cosineRadioButton);
        cosineRadioButton.setText("코사인");
        cosineRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cosineRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(widthSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(nMaxSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rectangularRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(triangleRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(nMaxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nMaxSpinner))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(widthLabel)
                            .addComponent(shapeLabel)
                            .addComponent(formulaRadioButton)
                            .addComponent(cosineRadioButton))
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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nMaxLabel)
                    .addComponent(nMaxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nMaxSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(shapeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rectangularRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(triangleRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(formulaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cosineRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                .addContainerGap()
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

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void triangleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triangleRadioButtonActionPerformed
        // TODO add your handling code here:
        shape = SHAPE.TRIANGLE;
        mainGraphPanel.repaint();
        coefficientPanel.repaint();
    }//GEN-LAST:event_triangleRadioButtonActionPerformed

    private void rectangularRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectangularRadioButtonActionPerformed
        // TODO add your handling code here:
        shape = SHAPE.LECTANGULAR;
        mainGraphPanel.repaint();
        coefficientPanel.repaint();
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

    private void formulaRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formulaRadioButtonActionPerformed
        // TODO add your handling code here:
        shape = SHAPE.FORMULA;
        mainGraphPanel.repaint();
        coefficientPanel.repaint();
    }//GEN-LAST:event_formulaRadioButtonActionPerformed

    private void cosineRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cosineRadioButtonActionPerformed
        // TODO add your handling code here:
        shape = SHAPE.COSINE;
        mainGraphPanel.repaint();
        coefficientPanel.repaint();
    }//GEN-LAST:event_cosineRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel coefficientPanel;
    private javax.swing.JRadioButton cosineRadioButton;
    private javax.swing.JRadioButton formulaRadioButton;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel mainGraphPanel;
    private javax.swing.JLabel nMaxLabel;
    private javax.swing.JSlider nMaxSlider;
    private javax.swing.JSpinner nMaxSpinner;
    private javax.swing.JRadioButton rectangularRadioButton;
    private javax.swing.JLabel shapeLabel;
    private javax.swing.JRadioButton triangleRadioButton;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JSlider widthSlider;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
