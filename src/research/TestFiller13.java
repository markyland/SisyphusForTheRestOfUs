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
public class TestFiller13 extends ATrack {

    double yDelta;

    private static double maxPointDistance = 0.01;  // approximately 2mm on A16 table...

    public TestFiller13() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        Point point=Point.fromXY(-1.1, 1.1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        squareH(120);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    double y=1.1;

    double slide=0;
    double slideAt=y-.16;


    private void squareH(int numRows){
        yDelta=2.2/numRows;

        Point point;

        point=Point.fromXY(-1.1, y);
        go(point);

        while (y>-1.1){
            if (y<=slideAt){
                slide-=2*Math.PI*Math.random();

                y+=.1;

                slideAt-=.16;
            }

            point=Point.fromXY(1.1, y);
            line(point);

            y-=yDelta;

            point=Point.fromXY(1.1, y);
            go(point);

            point=Point.fromXY(-1.1, y);
            line(point);

            y-=yDelta;

            point=Point.fromXY(-1.1, y);
            go(point);
        }

        point=Point.fromXY(1.1, y);
        line(point);
    }

    private void line(Point point){
        Point tempPoint=dc.getCurrentRelativePosition().vectorTo(point);

        Line line = new StraightLine( maxPointDistance, tempPoint.x, tempPoint.y );

        Point point2=dc.getCurrentRelativePosition();

        for (Delta delta : line.getDeltas()){
            if (Math.abs(delta.y)>.001){
                System.err.println();
            }

            point2=Point.fromXY(point2.x+delta.x, point2.y+delta.y);

            Point point3=point2;

            point3=Point.fromXY(point3.x, point3.y+Math.sin(22*point3.x+slide)*.03);

            go(point3);
        }
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

//    private void moveTo(Point fromPoint, Point toPoint){
//        if (fromPoint.rho<=1 && toPoint.rho>1){
//            double m=(toPoint.y-fromPoint.y)/(toPoint.x-fromPoint.x);
//
//            while ()
//        }
//
//
//        tempPoint = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
//        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));
//
//        tempPoint = Point.fromXY(tempPoint.x, tempPoint.y-yDelta);
//
//        Point tempPoint2 = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
//
//        double diff=tempPoint2.theta-tempPoint.theta;
//
//        if (!Double.isNaN(diff)) {
//            dc.arcAroundTableCenter(diff);
//        }
//    }

    public static void main(String args[]) throws Exception {
        TestFiller13 me = new TestFiller13();
    }
}