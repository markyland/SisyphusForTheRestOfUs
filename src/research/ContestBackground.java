package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class ContestBackground {
    double eraseSpace=0.0125;

    public ContestBackground() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {

                double rho=0;
                double theta=0;

                while (true){
                    theta+=.005;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    double additionalRho=getAddRho(rho, theta);

                    if (rho+additionalRho>1){
                        break;
                    }

                    Point dest = Point.fromRT(rho+additionalRho, theta);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
                dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
            }
        }.trace();
    }

    private double getAddRho(double rho, double theta){
        double wiggleAmount=3*(1-Math.abs(2*Math.pow(rho, 1.1)-1));
        double frequency=20;

        double rhoRed=wiggleAmount*1.1 * Math.cos((theta)*12) *(.8+rho)*(.8+rho)* eraseSpace;    //background
        double rhoGreen=wiggleAmount/1.5 * Math.sin(theta*frequency+theta/20) * eraseSpace;            //foreground
        double rhoBlue=wiggleAmount/2 * Math.sin(theta*frequency*1.5) * eraseSpace;            //foreground
        double rhoWhite=0;   //wiggleAmount * Math.sin(theta*rho*frequency*2) * eraseSpace;
        double rhoBlack=wiggleAmount/1.2 * Math.pow(Math.sin(theta*frequency*2.3),2) * eraseSpace;//wiggleAmount/1.5 * Math.sin(theta*frequency*2.3) * eraseSpace;
        double rhoGrey100=wiggleAmount * Math.sin(theta*frequency+theta/3) * eraseSpace;;
        double rhoGrey200=wiggleAmount*2 * Math.sin(theta*frequency % 5) * eraseSpace;;
        double rhoGrey150=wiggleAmount/2 * Math.sin(theta*frequency/1.5) * eraseSpace;    //background
        double rhoGrey50=wiggleAmount/2 * (Math.sin(theta*frequency)>0 ? 1 : -1) * eraseSpace;    //background
        double rhoGrey250=wiggleAmount/1.5 * Math.sin(Math.pow(theta, 1.005)*frequency) * eraseSpace;    //background

        double additionalRho=rhoRed;

        if (((int)(theta/(Math.PI/8)))%2==0){
            additionalRho=rhoRed;
        }

        return additionalRho;
    }

    private int getFill(double rho, double theta, int[][] pixels, int height, int width){
        Point point = Point.fromRT(rho, theta);

        double x=point.x;
        double y=point.y;

        return pixels[(int)Math.round(height-height*(y/2+.5))][(int)Math.round(width*(x/2+.5))];
    }

    public static void main(String args[]) throws Exception {
        ContestBackground me = new ContestBackground();
    }
}