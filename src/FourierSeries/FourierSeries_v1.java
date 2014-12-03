package FourierSeries;

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.Math.*;

class GraphJPanel extends JPanel {
    private double width; // width of the trial function
    private int n_max; // the largest order for partial sumation
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

    public GraphJPanel(){
        this.width = 0.3*Math.PI;
        this.n_max = 10;
    }
    public GraphJPanel(double width, int n_max){
        this.width = width;
        this.n_max = n_max;
    }
    public void setWidth(double width){
        this.width = width;
    }
    public void setN_max(int n_max){
        this.n_max = n_max;
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
            this.moveTo(virtualX(VIRTUAL_X_MIN),virtualY(VIRTUAL_Y_MIN));
            this.lineTo(virtualX(VIRTUAL_X_MAX), virtualY(VIRTUAL_Y_MIN));
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
        g2.draw(new trialFunction(this.width));

        // Drawing the partial sumation of Fourier series
        g2.setPaint(Color.red);
        g2.draw(new fourierSum(this.width,this.n_max));
    }
}

public class FourierSeries_v1 extends JPanel implements ActionListener, ChangeListener {
    public static void main(String[] args) {
        JFrame window = new JFrame("FourierSeries v1");
        FourierSeries_v1 content = new FourierSeries_v1();
        window.setContentPane(content);
        window.pack();
        window.setLocation(100,100);
        window.setResizable(false);
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setVisible(true);
    }
    
    private GraphJPanel graphPanel;
    private JLabel widthLabel;
    private JSlider widthSlider;
    private JLabel n_maxLabel;
    private JSlider n_maxSlider;
    
    public FourierSeries_v1() {
        setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        
        setLayout(null);
        setBorder( BorderFactory.createEtchedBorder() );
        setPreferredSize( new Dimension(1000,400) );
        
        graphPanel = new GraphJPanel();
        graphPanel.setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
        graphPanel.setBounds(0,0,800,400);
        add(graphPanel);
        
        widthLabel = new JLabel("Width", JLabel.LEFT);
        widthLabel.setOpaque(true);
        widthLabel.setFont( new Font("Serif", Font.BOLD, 24) );
        widthLabel.setBounds(800,0,200,30);
        add(widthLabel);
        
        widthSlider = new JSlider(0,100,30);
        widthSlider.addChangeListener(this);
        widthSlider.setBounds(800, 30, 200, 30);
        add(widthSlider);
        
        n_maxLabel = new JLabel("Largest n", JLabel.LEFT);
        n_maxLabel.setOpaque(true);
        n_maxLabel.setFont( new Font("Serif", Font.BOLD, 24) );
        n_maxLabel.setBounds(800,60,200,30);
        add(n_maxLabel);
        
        n_maxSlider = new JSlider(0,100,10);
        n_maxSlider.addChangeListener(this);
        n_maxSlider.setBounds(800, 90, 200, 30);
        add(n_maxSlider);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setBounds(800,120,200,30);
        add(okButton);
    }
    
    public void actionPerformed(ActionEvent evt) {
        graphPanel.setWidth(((double)widthSlider.getValue())/100.0*Math.PI);
        graphPanel.setN_max(n_maxSlider.getValue());
        graphPanel.repaint();
    }
    
    public void stateChanged(ChangeEvent evt) {
        graphPanel.setWidth(((double)widthSlider.getValue())/100.0*Math.PI);
        graphPanel.setN_max(n_maxSlider.getValue());
        graphPanel.repaint();
    }
}