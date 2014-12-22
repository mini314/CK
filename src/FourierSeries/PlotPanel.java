/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FourierSeries;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

/**
 *
 * @author mini
 */
class PlotPanel  extends JPanel {
    // Proportion of margin of panel
    final double MARGIN_OF_RIGHT = 0.05; // for right
    final double MARGIN_OF_LEFT = 0.05; // for left
    final double MARGIN_OF_ABOVE = 0.1; // for upper side
    final double MARGIN_OF_BELOW = 0.2; // for lower side
    // Width and height of panel
    int WIDTH_OF_PANEL;
    int HEIGHT_OF_PANEL;
    // Virtual maximum and minimum of coordinates
    final double VIRTUAL_X_MIN;
    final double VIRTUAL_X_MAX;
    final double VIRTUAL_Y_MIN;
    final double VIRTUAL_Y_MAX;

    public PlotPanel(double x_min, double x_max, double y_min, double y_max){
        VIRTUAL_X_MIN = x_min;
        VIRTUAL_X_MAX = x_max;
        VIRTUAL_Y_MIN = y_min;
        VIRTUAL_Y_MAX = y_max;
    }

    /*
    * Following two functions are degined
    * to convert virtual coordinates
    * from (VIRTUAL_X_MIN,VIRTUAL_Y_MIN) to (VIRTUAL_X_MAX,VIRTUAL_Y_MAX)
    * into real coordinates.
    */
    protected double virtualX(double x) { // get virtual x coordinate
        return (x-VIRTUAL_X_MIN)*WIDTH_OF_PANEL*(1.0-MARGIN_OF_RIGHT-MARGIN_OF_LEFT)/(VIRTUAL_X_MAX-VIRTUAL_X_MIN)+WIDTH_OF_PANEL*MARGIN_OF_LEFT;
    }
    protected double virtualY(double y) { // get vritual y coordinate
        return (VIRTUAL_Y_MAX-y)*HEIGHT_OF_PANEL*(1.0-MARGIN_OF_ABOVE-MARGIN_OF_BELOW)/(VIRTUAL_Y_MAX-VIRTUAL_Y_MIN)+HEIGHT_OF_PANEL*MARGIN_OF_ABOVE;
    }
    // class for axis
    protected class axis extends Path2D.Double{
        public axis(){
            this.moveTo(virtualX(VIRTUAL_X_MIN),virtualY(VIRTUAL_Y_MIN));
            this.lineTo(virtualX(VIRTUAL_X_MAX), virtualY(VIRTUAL_Y_MIN));
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
