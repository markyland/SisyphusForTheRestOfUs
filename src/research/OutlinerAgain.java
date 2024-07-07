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
public class OutlinerAgain extends ATrack {
    private static double maxPointDistance = 0.001;  // approximately 2mm on A16 table...

    private static double PI2=Math.PI*2;

    private double LINE_SPACE=.03;

    public OutlinerAgain() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        squareH();

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH() {
        Point point;

        go(Point.fromXY(-.2, .2));
        previousPoint=dc.getCurrentRelativePosition();

        for (double i = 0; i < 1; i+=LINE_SPACE) {
            line(Point.fromXY(.2, .2), i);
            line(Point.fromXY(.2, -.2), i);
            line(Point.fromXY(-.2, -.2), i);
            line(Point.fromXY(-.2, .2), i);
        }
    }

    Point previousPoint;
    private double startTheta=Double.NaN;

    private void line(Point point, double gradient){
        double amount=.1;

        Point tempPoint=previousPoint.vectorTo(point);

        Line line = new StraightLine( maxPointDistance, tempPoint.x, tempPoint.y );

        Point point2=previousPoint;

        for (Delta delta : line.getDeltas()){
            point2=Point.fromXY(point2.x+delta.x, point2.y+delta.y);

            if (Double.isNaN(startTheta)){
                startTheta= point2.theta;
            }

//            double theta=point2.theta<0 ? point2.theta+PI2 : point2.theta   ;
//            theta=theta-startTheta;
//            System.err.println(theta);
            //Point point3=Point.fromRT(point2.rho+amount*(count + ((theta)/PI2)), point2.theta);
//            Point point3=Point.fromRT(point2.rho+amount*(count), point2.theta);

            double theta=point2.theta%PI2-startTheta;
            theta=theta<0 ? theta+PI2 : theta;

            double gradient2=gradient+theta/PI2*.03;
            Point point3=Point.fromRT(point2.rho*(1-gradient2)+gradient2, point2.theta);

            // System.err.println(point2 + " " + point3);

            go(point3);
        }

        previousPoint=Point.fromXY(point.x, point.y);
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        OutlinerAgain me = new OutlinerAgain();
    }
}