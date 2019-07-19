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
public class Spiral11 {
    double eraseSpace=0.0125;

    public Spiral11() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                double rho=0;
                double theta=0;//dc.getCurrentPosition().getTheta();

                while (rho<1){
                    theta+=.0125;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    Point dest = Point.fromRT(rho, theta);

                    dest=Point.fromXY(dest.x+.2, dest.y);
                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\panda.png" );
                dc.write( "c:\\users\\mark\\desktop\\panda.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\panda.png");
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        Spiral11 me = new Spiral11();
    }
}