package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Delta;
import com.slightlyloony.jsisyphus.Point;
import com.slightlyloony.jsisyphus.lines.Line;
import com.slightlyloony.jsisyphus.lines.StraightLine;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class TestFiller18 extends ATrack {

    double yDelta;

    private static double maxPointDistance = 0.01;  // approximately 2mm on A16 table...

    public TestFiller18() throws Exception {
        super("");

        trace();
    }

    private int lineCount=0;

    protected void trace() throws IOException {
        go(Point.fromXY(.011, .011));

        double i=1;

        while (i<2){
            square(i);

            i+=.02;
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void square(double i){
        line(Point.fromXY(.011*i, .011*i), i);

        line(Point.fromXY(-.011*i, .011*i), i);
        line(Point.fromXY(-.011*i, -.011*i), i);
        line(Point.fromXY(.011*i, -.011*i), i);
        line(Point.fromXY(.011*i, .011*i), i);
    }

    private void line(Point point, double i){
        Point tempPoint=dc.getCurrentRelativePosition().vectorTo(point);

        Line line = new StraightLine( maxPointDistance, tempPoint.x, tempPoint.y);

        Point point2=dc.getCurrentRelativePosition();

        for (Delta delta : line.getDeltas()){
            point2=Point.fromXY(point2.x+delta.x, point2.y+delta.y);

            Point point3=point2;

            //---------------------------------------------------------------

            double pert=i-1;

            point3=Point.fromRT(point3.rho*(1-pert) + pert, point3.theta);

            //---------------------------------------------------------------

            go(point3);
        }

        lineCount++;
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        TestFiller18 me = new TestFiller18();
    }
}