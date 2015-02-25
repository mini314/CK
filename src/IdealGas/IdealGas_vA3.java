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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.text.DecimalFormat;

/**
 *
 * @author Home
 */
public class IdealGas_vA3 extends javax.swing.JApplet {
    interface MODE {
        final int VOLUME = 0;
        final int TEMPERATURE = 1;
    }
    
    final private int PARTICLE_NUMBER = 1000;
    final private double TEMPERATURE_MAX = 1.1; // maximal value of the temperature of environment
    final private double PRESSURE_MAX = 11.0; // maximal value of the pressure
    final private double VOLUME_MAX = 1.1; // maximal value of the volume
    
    private double temperature = 0.5;
    private int pressure_counter = 0;
    final private int COUNTER_MAX = 1000;
    private double[] pressure = new double[COUNTER_MAX];
    private double positionPiston = 0.5; // positon of the piston
    final private double TIME_INTERVAL = 0.001;
    
    private int mode = MODE.VOLUME;
    
    private double[] xGas = new double[PARTICLE_NUMBER]; // x position of a i-th gas particle
    private double[] yGas = new double[PARTICLE_NUMBER]; // y position of a i-th gas particle
    private double[] vxGas = new double[PARTICLE_NUMBER]; // x velocity of a i-th gas particle
    private double[] vyGas = new double[PARTICLE_NUMBER]; // y velocity of a i-th gas particle

    private void evolve(int duration) {
        for(int t=0;t<duration;t++) {
            pressure[pressure_counter] = 0.0;
            for(int i=0;i<PARTICLE_NUMBER;i++) {
                // Reflection
                if(xGas[i]<0.0) {
                    vxGas[i]=Math.abs(vxGas[i]);
                    vyGas[i]=Math.signum(vyGas[i])*Math.abs(Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random()));
                    pressure[pressure_counter] += Math.abs(vxGas[i]);
                }
                if(xGas[i]>positionPiston) {
                    vxGas[i]=-Math.abs(vxGas[i]);
                    vyGas[i]=Math.signum(vyGas[i])*Math.abs(Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random()));
                    pressure[pressure_counter] += Math.abs(vxGas[i]);
                }
                if(yGas[i]<0.0) {
                    vxGas[i]=Math.signum(vxGas[i])*Math.abs(Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random()));
                    vyGas[i]=Math.abs(vyGas[i]);
                    pressure[pressure_counter] += Math.abs(vyGas[i])/positionPiston;
                }
                if(yGas[i]>1.0) {
                    vxGas[i]=Math.signum(vxGas[i])*Math.abs(Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random()));
                    vyGas[i]=-Math.abs(vyGas[i]);
                    pressure[pressure_counter] += Math.abs(vyGas[i])/positionPiston;
                }
                xGas[i]+=vxGas[i]*TIME_INTERVAL;
                yGas[i]+=vyGas[i]*TIME_INTERVAL;
            }
            pressure_counter++;
            if(pressure_counter==COUNTER_MAX)
                pressure_counter = 0;
        }
    }
    private void initialize() {
        for(int i=0;i<PARTICLE_NUMBER;i++) {
            xGas[i] = positionPiston*Math.random();
            yGas[i] = Math.random();
            vxGas[i] = Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random());
            vyGas[i] = Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random());
        }
        evolve(1000);
    }
    private double pressure() {
        double temp = 0.0;
        for(int i=0;i<COUNTER_MAX;i++) {
            temp += pressure[i];
        }
        return temp/PARTICLE_NUMBER/2.;
    }
    private double temperature() {
        double temp = 0.0;
        for(int i=0;i<COUNTER_MAX;i++) {
            temp += vxGas[i]*vxGas[i]+vyGas[i]*vyGas[i];
        }
        return temp/PARTICLE_NUMBER/2.;
    }

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
        private class theory extends Path2D.Double{
            public theory(){
                this.moveTo(virtualX(1.015*temperature/10.0),virtualY(10.0));
                for(double x = 1.015*temperature/10.0;x<1.0;x+=0.01)
                    this.lineTo(virtualX(x), virtualY(1.015*temperature/x));
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
            g2.drawString("V", (int) virtualX(VIRTUAL_X_MAX), (int)virtualY(-1.0));
            g2.drawString("p", (int) virtualX(-0.05), (int)virtualY(VIRTUAL_Y_MAX));
            
            // Drawing theory line
            g2.setPaint(Color.blue);
            g2.draw(new theory());
            
            // Drawing point
            g2.setPaint(Color.red);
            g2.fillOval((int)virtualX(positionPiston)-5, (int)virtualY(pressure())-5, 10, 10);
        }
    }    
    class TPGraphPanel extends JPanel {
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

        public TPGraphPanel(){
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
        private class theory extends Path2D.Double{
            public theory(){
                this.moveTo(virtualX(0.0),virtualY(0.0));
                switch (mode){
                    case MODE.VOLUME :
                        for(double x = 0.0;x<1.0;x+=0.01)
                            this.lineTo(virtualX(x), virtualY(1.015*x/pressure()));
                    break;
                    case MODE.TEMPERATURE :
                        for(double x = 0.0;x<1.0;x+=0.01)
                            this.lineTo(virtualX(x), virtualY(1.015*x));
                    break;
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
            g2.drawString("T", (int) virtualX(VIRTUAL_X_MAX), (int)virtualY(-0.1));
            g2.drawString("V", (int) virtualX(-0.05), (int)virtualY(VIRTUAL_Y_MAX));
            
            // Drawing theory line
            g2.setPaint(Color.blue);
            g2.draw(new theory());
            
            // Drawing point
            g2.setPaint(Color.red);
            g2.fillOval((int)virtualX(temperature)-5, (int)virtualY(positionPiston)-5, 10, 10);
        }
    }
    
    class AnimationPanel extends JPanel implements Runnable {
        final int POSITION_X_CYLINDER = 20; // x position of the inner square of the cylinder
        final int POSITION_Y_CYLINDER = 20; // y position of the inner square of the cylinder
        final int WIDTH_CYLINDER = 260; // maximal width of the inner square of the cylinder
        final int HEIGHT_CYLINDER = 120; // height of the inner square of the cylinder
        final int THICKNESS_WALL = 10; // thickness of the walls building the cylinder
        
        Thread thread = new Thread(this);
        
        AnimationPanel() {
            initialize();
            thread.start();
        }

        public void run() {
            Thread current = Thread.currentThread();
            int count = 0;
            while(current==thread) {
                if(count < 1) {
                    DecimalFormat printText = new DecimalFormat("#.####");
                    volumeTextField.setText(printText.format(positionPiston));
                    temperatureTextField.setText(printText.format(temperature()));
                    pressureTextField.setText(printText.format(PARTICLE_NUMBER*pressure()));
                    particlesTextField.setText(printText.format(PARTICLE_NUMBER));
                    calculationTextField.setText(printText.format(PARTICLE_NUMBER*pressure()*positionPiston)+" - "+printText.format(PARTICLE_NUMBER*temperature()));
                    finalTextField.setText(printText.format(PARTICLE_NUMBER*pressure()*positionPiston-PARTICLE_NUMBER*temperature()));

                    count=100000;
                }
                AboveGraphPanel.repaint();
                BelowGraphPanel.repaint();
                repaint();
                count--;
            }
        }
        
        private int virtualX(double x) { // get virtual x coordinate
            return (int)(x*WIDTH_CYLINDER+POSITION_X_CYLINDER);
        }
        private int virtualY(double y) { // get vritual y coordinate
            return (int)((1.0-y)*HEIGHT_CYLINDER+POSITION_Y_CYLINDER);
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Drawing the cylinder
            g2.setPaint(Color.gray);
            g2.fillRect(POSITION_X_CYLINDER-THICKNESS_WALL,POSITION_Y_CYLINDER-THICKNESS_WALL,THICKNESS_WALL,HEIGHT_CYLINDER+2*THICKNESS_WALL);
            g2.fillRect(POSITION_X_CYLINDER,POSITION_Y_CYLINDER-THICKNESS_WALL,WIDTH_CYLINDER+THICKNESS_WALL,THICKNESS_WALL);
            g2.fillRect(POSITION_X_CYLINDER,POSITION_Y_CYLINDER+HEIGHT_CYLINDER,WIDTH_CYLINDER+THICKNESS_WALL,THICKNESS_WALL);
            
            // Drawing the inner space
            g2.setPaint(Color.white);
            g2.fillRect(POSITION_X_CYLINDER,POSITION_Y_CYLINDER,virtualX(positionPiston)-THICKNESS_WALL,HEIGHT_CYLINDER);
            
            // Drawing the piston
            g2.setPaint(Color.blue);
            g2.fillRect(virtualX(positionPiston),POSITION_Y_CYLINDER,THICKNESS_WALL,HEIGHT_CYLINDER);

            // Drawing the particles
            switch (mode){
                case MODE.VOLUME :
                    evolve(100);
                    for(int i=0;i<PARTICLE_NUMBER;i++) {
                        g2.drawOval(virtualX(xGas[i]), virtualY(yGas[i]), 1, 1);
                    }
                break;
                case MODE.TEMPERATURE :
                    positionPiston += 10.*(pressure()-1.0)*TIME_INTERVAL;
                    for(int i=0;i<PARTICLE_NUMBER;i++) {
                        if(xGas[i]>positionPiston) {
                            xGas[i]=positionPiston;
                            vxGas[i]=-Math.abs(vxGas[i]);
                            vyGas[i]=Math.signum(vyGas[i])*Math.abs(Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random()));
                        }
                    }
                    evolve(100);
                    for(int i=0;i<PARTICLE_NUMBER;i++) {
                        g2.drawOval(virtualX(xGas[i]), virtualY(yGas[i]), 1, 1);
                    }
                break;
            }
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
            java.util.logging.Logger.getLogger(IdealGas_vA3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IdealGas_vA3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IdealGas_vA3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IdealGas_vA3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        AboveGraphPanel = new VPGraphPanel();
        BelowGraphPanel = new TPGraphPanel();
        GraphicPanel = new AnimationPanel();
        pressureTextField = new javax.swing.JTextField();
        finalTextField = new javax.swing.JTextField();
        volumeTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        particlesTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        calculationTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        temperatureTextField = new javax.swing.JTextField();
        ControlPanel = new javax.swing.JPanel();
        volumeButton = new javax.swing.JRadioButton();
        temperatureRadioButton = new javax.swing.JRadioButton();
        volumeSlider = new javax.swing.JSlider();
        temperatureSlider = new javax.swing.JSlider();

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

        GraphicPanel.setBackground(new java.awt.Color(186, 183, 205));
        GraphicPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pressureTextField.setText("1.2345");

        finalTextField.setText("1.2345");

        volumeTextField.setText("1.2345");
        volumeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volumeTextFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("pressure(p)");

        jLabel2.setText("temperature(T)");

        jLabel3.setText("volume(V)");

        particlesTextField.setText("0.000");

        jLabel4.setText("particles(N)");

        jLabel5.setText("PV-NT =");

        calculationTextField.setText("1.2345");

        jLabel6.setText(" =");

        temperatureTextField.setText("1.2345");

        javax.swing.GroupLayout GraphicPanelLayout = new javax.swing.GroupLayout(GraphicPanel);
        GraphicPanel.setLayout(GraphicPanelLayout);
        GraphicPanelLayout.setHorizontalGroup(
            GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GraphicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(GraphicPanelLayout.createSequentialGroup()
                        .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(GraphicPanelLayout.createSequentialGroup()
                                .addComponent(volumeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2))
                            .addGroup(GraphicPanelLayout.createSequentialGroup()
                                .addComponent(pressureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(temperatureTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(particlesTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                    .addGroup(GraphicPanelLayout.createSequentialGroup()
                        .addComponent(calculationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(finalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        GraphicPanelLayout.setVerticalGroup(
            GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GraphicPanelLayout.createSequentialGroup()
                .addContainerGap(168, Short.MAX_VALUE)
                .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(particlesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(pressureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(temperatureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(volumeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(GraphicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(calculationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(finalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        ControlPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        buttonGroup1.add(volumeButton);
        volumeButton.setSelected(true);
        volumeButton.setText("volume");
        volumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volumeButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(temperatureRadioButton);
        temperatureRadioButton.setText("temperature");
        temperatureRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temperatureRadioButtonActionPerformed(evt);
            }
        });

        volumeSlider.setMinimum(10);
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeSliderStateChanged(evt);
            }
        });

        temperatureSlider.setMinimum(10);
        temperatureSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        temperatureSlider.setEnabled(false);
        temperatureSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                temperatureSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout ControlPanelLayout = new javax.swing.GroupLayout(ControlPanel);
        ControlPanel.setLayout(ControlPanelLayout);
        ControlPanelLayout.setHorizontalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlPanelLayout.createSequentialGroup()
                        .addComponent(temperatureRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(ControlPanelLayout.createSequentialGroup()
                        .addComponent(volumeButton)
                        .addGap(34, 34, 34)))
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(volumeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(temperatureSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ControlPanelLayout.setVerticalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(volumeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(temperatureSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(temperatureRadioButton, javax.swing.GroupLayout.Alignment.TRAILING))
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
                .addContainerGap(15, Short.MAX_VALUE))
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
            .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jInternalFrame1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void volumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volumeButtonActionPerformed
        // TODO add your handling code here:
        volumeSlider.setEnabled(true);
        temperatureSlider.setEnabled(false);
        mode = MODE.VOLUME;
    }//GEN-LAST:event_volumeButtonActionPerformed

    private void volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeSliderStateChanged
        // TODO add your handling code here:
        positionPiston = ((double)volumeSlider.getValue())/100.0;
        for(int i=0;i<PARTICLE_NUMBER;i++) {
            if(xGas[i]>positionPiston) {
                xGas[i]=positionPiston;
                vxGas[i]=-Math.abs(vxGas[i]);
                vyGas[i]=Math.signum(vyGas[i])*Math.abs(Math.sqrt(-temperature*2.*Math.log(Math.random()))*Math.cos(2.*Math.PI*Math.random()));
            }
        }
        evolve(100);
    }//GEN-LAST:event_volumeSliderStateChanged

    private void temperatureSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_temperatureSliderStateChanged
        // TODO add your handling code here:
        temperature = ((double)temperatureSlider.getValue())/100.0;
        GraphicPanel.setBackground(new java.awt.Color(142+(int)(88*temperature),180+(int)(5*temperature),227-(int)(43*temperature)));
        evolve(100);
    }//GEN-LAST:event_temperatureSliderStateChanged

    private void temperatureRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temperatureRadioButtonActionPerformed
        // TODO add your handling code here:
        volumeSlider.setEnabled(false);
        temperatureSlider.setEnabled(true);
        mode = MODE.TEMPERATURE;
    }//GEN-LAST:event_temperatureRadioButtonActionPerformed

    private void volumeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volumeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_volumeTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AboveGraphPanel;
    private javax.swing.JPanel BelowGraphPanel;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JPanel GraphicPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField calculationTextField;
    private javax.swing.JTextField finalTextField;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField particlesTextField;
    private javax.swing.JTextField pressureTextField;
    private javax.swing.JRadioButton temperatureRadioButton;
    private javax.swing.JSlider temperatureSlider;
    private javax.swing.JTextField temperatureTextField;
    private javax.swing.JRadioButton volumeButton;
    private javax.swing.JSlider volumeSlider;
    private javax.swing.JTextField volumeTextField;
    // End of variables declaration//GEN-END:variables
}
