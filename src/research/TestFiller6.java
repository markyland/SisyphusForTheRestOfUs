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
public class TestFiller6 extends ATrack {

    private double scale=7;
    private double gridLength=2.0/scale;

    private double diag=.15;

    private int gridY=0;

    public TestFiller6() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        Point point=Point.fromXY(-1, 1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        for (int y=0; y<scale; y++){
            gridY=y;

                squareD(100);
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
}

    private void squareH(int numRows){
        double y=1;

        double yDelta=1.0/numRows;

        Point point;

        point=Point.fromXY(0, y);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            point=Point.fromXY(1, y);
            go(point);

            y-=yDelta;

            point=Point.fromXY(1, y);
            go(point);

            point=Point.fromXY(0, y);
            go(point);

            y-=yDelta;

            point=Point.fromXY(0, y);
            go(point);
        }

        point=Point.fromXY(1, y);
        go(point);
    }

    private void squareV(int numRows){
        double x=0;

        double xDelta=1.0/numRows;

        Point point;

        point=Point.fromXY(x, 0);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            point=Point.fromXY(x, 1);
            go(point);

            x+=xDelta;

            point=Point.fromXY(x, 1);
            go(point);

            point=Point.fromXY(x, 0);
            go(point);

            x+=xDelta;

            point=Point.fromXY(x, 0);
            go(point);
        }

        point=Point.fromXY(x, 1);
        go(point);
    }

    private void squareD(int numRows){
        double x=0;

        double xDelta=1.0/numRows;

        Point point;

        point=Point.fromXY(x, 0);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            point=Point.fromXY(x+diag, 1);
            go(point);

            x+=xDelta;

            point=Point.fromXY(x+diag, 1);
            go(point);

            point=Point.fromXY(x, 0);
            go(point);

            x+=xDelta;

            point=Point.fromXY(x, 0);
            go(point);
        }

        point=Point.fromXY(x+diag, 1);
        go(point);
    }

    private void go(Point point){
        double x=point.x;
        double y=point.y;

        if (isBackwards()){
            x=1-x;
        }

        x=x*2.8-1.4;
        y=(y*2-2)/scale + 1 - gridY*gridLength;

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y)));
    }

    private boolean isBackwards(){
        return gridY % 2 == 1;
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
        TestFiller6 me = new TestFiller6();
    }
}