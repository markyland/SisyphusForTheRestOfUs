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
public class Stars {

    public Stars() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                double eraseSpace=0.0125;

                double rho=.01;
                double theta=0;//dc.getCurrentPosition().getTheta();

                double lastStar=55;

                while (rho<1){
                    theta+=.015/rho;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho, theta)));



                    if ((int)(theta/32*Math.PI)>(int)(lastStar/32*Math.PI)){
                        lastStar=theta;

                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho-.2, theta)));
                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho-.1, theta)));
                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho-.1, theta-.08/rho)));
                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho-.1, theta+.08/rho)));
                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho-.1, theta)));
                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho, theta)));
                    }
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\stars.png" );
                dc.write( "c:\\users\\mark\\desktop\\stars.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\stars.png");
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        Stars me = new Stars();
    }
}