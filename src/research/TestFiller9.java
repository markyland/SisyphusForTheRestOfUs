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
public class TestFiller9 extends ATrack {

    private double moonR=.65;
    private double moonR2=1;

    private boolean backwards=false;

    public TestFiller9() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        dc.spiralToXY(-(moonR+.05), 0, 0, 0, 75);

        Point point=Point.fromXY(-1, 1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        squareH(150);
        squareH2(150);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    double y=1;

    private void squareH(int numRows){
        double yDelta=2.0/numRows;

        Point point;

        point=Point.fromXY(-1, y);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            double rightX;

            if (inMoon(y, moonR)){
                if (!inMoon(y-yDelta, moonR)){
                    rightX=1;
                }
                else {
                    rightX = -Math.sqrt(moonR * moonR - y * y);
                }
            }
            else{
                rightX=1;
            }

            System.err.println("h : " + y);

            point=Point.fromXY(rightX, y);
            go(point);

            y-=yDelta;

            if (inMoon(y, moonR)){
                if (!inMoon(y+yDelta, moonR)){
                    rightX=1;
                }
                else {
                    rightX = -Math.sqrt(moonR * moonR - y * y);
                }
            }
            else{
                rightX=1;
            }

            point=Point.fromXY(rightX, y);
            go(point);

            point=Point.fromXY(-1, y);
            go(point);

            y-=yDelta;

            point=Point.fromXY(-1, y);
            go(point);
        }

        point=Point.fromXY(1, y);
        go(point);
    }

    private void squareH2(int numRows){
        double yDelta=2.0/numRows;

        y-=yDelta;

        Point point;

        point=Point.fromXY(1, y);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            double rightX;

            if (inMoon(y, moonR)){
                if (!inMoon(y+yDelta, moonR)){
                    rightX=-1;
                }
                else {
                    rightX = Math.sqrt(moonR * moonR - y * y);
                    point=Point.fromXY(rightX, y);
                    go(point);

                    rightX = -Math.sqrt(moonR2 * moonR2 - y * y)+.7;

                    point=Point.fromXY(rightX, y-yDelta);
                    go(point);

                    rightX = Math.sqrt(moonR * moonR - y * y);
                    point=Point.fromXY(rightX, y);
                    go(point);
                }
            }
            else{
                rightX=-1;
            }

            System.err.println("h2 : " + y);

            point=Point.fromXY(rightX, y);
            go(point);

            y+=yDelta;

            if (inMoon(y, moonR)){
                if (!inMoon(y-yDelta, moonR)){
                    rightX=-1;
                }
                else {
                    rightX = -Math.sqrt(moonR2 * moonR2 - y * y)+.7;
                }
            }
            else{
                rightX=-1;
            }

            point=Point.fromXY(rightX, y);
            go(point);

            point=Point.fromXY(1, y);
            go(point);

            y+=yDelta;

            point=Point.fromXY(1, y);
            go(point);
        }

        point=Point.fromXY(-1, y);
        go(point);
    }

    private boolean inMoon(double y, double r) {
        return y >= -r && y <= r;
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
        TestFiller9 me = new TestFiller9();
    }
}