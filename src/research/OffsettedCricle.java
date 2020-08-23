package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class OffsettedCricle extends ATrack {

    double rho=0;
    double theta=0;

    double eraseSpace=0.01;

    double x=-4;
    double y=0;

    public OffsettedCricle() throws Exception {
        super("");

        trace();
    }

    @Override
    protected void trace() throws IOException {
        go(Point.fromXY(x, y));

        spiral(true);

        for (int i=0; i<310; i++) {
            theta += Math.PI * 2;

            spiral(false);

            theta += Math.PI * 2;

            spiral(true);
        }

        go(Point.fromRT(1, theta));

        dc.arcAroundRT(-1, theta, Math.PI*2);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void spiral(boolean forward){
        int i=0;
        while (true){
            theta += forward ? .01 : -.01;

            rho = theta / (2 * Math.PI) * (eraseSpace);

            Point dest = Point.fromRT(rho, theta);

            dest=Point.fromXY(dest.x+x, dest.y+y);

            if (dest.rho<1){
                break;
            }

            if (i++>10000){
                return;
            }
        }

        while (true){
            theta+=forward ? .01 : -.01;

            rho=theta / (2 * Math.PI) * (eraseSpace);

            Point dest = Point.fromRT(rho, theta);

            dest=Point.fromXY(dest.x+x, dest.y+y);

            if (dest.rho>1.1){
                break;
            }

            go(dest);
        }
    }

    private void go(Point p){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(p));

    }

    public static void main(String args[]) throws Exception {
        OffsettedCricle me = new OffsettedCricle();
    }
}