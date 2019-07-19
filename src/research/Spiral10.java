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
public class Spiral10 {
    double eraseSpace=0.01;

    public Spiral10() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                double rho=0;
                double theta=0;//dc.getCurrentPosition().getTheta();

                while (rho<1.05){
                    theta+=.01;

                    rho=(int)(theta / (2 * Math.PI) + .0001) * (eraseSpace);

                    double spacing=getSpacing(rho, theta);

                    double rho2=(int)(rho/spacing + .0001)*spacing;

                    Point dest = Point.fromRT(rho2, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\waky.png" );
                dc.write( "c:\\users\\mark\\desktop\\waky.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\waky.png");
            }
        }.trace();
    }

    private double getSpacing(double rho, double theta){
        theta=theta % (2 * Math.PI);

        if (theta<0){
            theta+=2 * Math.PI;
        }

        int index=(int)(theta/((Math.PI*2)/4));

        index=index%4;

        if (index==0){
            return eraseSpace;
        }
        else if (index==1){
           return eraseSpace*1.4;
        }
        else if (index==2){
           return eraseSpace*2.4;
        }
        else {
            return eraseSpace*3.6;
        }
    }

    public static void main(String args[]) throws Exception {
        Spiral10 me = new Spiral10();
    }
}