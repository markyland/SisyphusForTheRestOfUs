import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class Blah {

    public Blah() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                for (double t=-3; t<=3; t+=.001){
                    double x = 7*Math.pow(Math.cos(Math.cos(1.84*Math.round(t))), 2) * (1 + Math.pow(Math.cos(1.92*t), 4));
                    double y = 7*Math.pow(Math.sin(1.84*t), 2) * Math.sin(Math.sin(1.92*t));

                    x=x/10;
                    y=y/10;

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y)));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\blah.png" );
                dc.write( "c:\\users\\mark\\desktop\\blah.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\blah.png");
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        Blah me = new Blah();
    }
}