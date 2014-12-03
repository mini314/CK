/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FourierSeries;

import java.awt.geom.Path2D;

/**
 *
 * @author mini
 */
public class MainPanel extends PlotPanel {
    private double width; // width of the trial function
    private int n_max; // the largest order for partial sumation

    public  MainPanel(double width, int n_max){
        super(-4.0,4.0,0.0,2.0);
        this.width = width;  // 0.3*Math.PI
        this.n_max = n_max;  // 10
    }
    public void setWidth(double width){
        this.width = width;
    }
    public void setN_max(int n_max){
        this.n_max = n_max;
    }
    // class for the trial function
    private class trialFunction extends Path2D.Double{
        public trialFunction(double width){
            this.moveTo(this.virtualX(-Math.PI),this.virtualY(0.0));
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