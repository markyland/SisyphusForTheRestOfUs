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
public class ContestBackground2 extends ATrack {
    private static double maxPointDistance = 0.001;  // approximately 2mm on A16 table...

    private static final double yDelta=.012;
    private static final double xDelta=.016;

    public ContestBackground2() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {

        draw();

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void draw(){
        Point origin = Point.fromXY(0, -.6);

        for (double theta=Math.PI*3/2-.6; theta<Math.PI*5/2+.6; theta+=.07){
            go(origin);
            go(Point.fromRT(1, theta));

            origin=Point.fromXY(origin.x+.02, origin.y);
        }

    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        ContestBackground2 me = new ContestBackground2();
    }
}