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
public class TestFiller7 extends ATrack {

    private double moonR=.75;

    private boolean backwards=false;

    public TestFiller7() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        Point point=Point.fromXY(-1, 1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        squareH(75);
        squareH2(75);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(int numRows){
        double y=1;

        double yDelta=2.0/numRows;

        Point point;

        point=Point.fromXY(-1, y);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            double rightX;

            if (y>=-moonR && y<=moonR){
                if (y-yDelta<-moonR || y-yDelta>moonR){
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

            y-=yDelta;

            if (y>=-moonR && y<=moonR){
                if (y+yDelta<-moonR || y+yDelta>moonR){
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
        double y=-1;

        double yDelta=2.0/numRows;

        Point point;

        point=Point.fromXY(1, y);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            double rightX;

            if (y>=-moonR && y<=moonR){
                if (y+yDelta<-moonR || y+yDelta>moonR){
                    rightX=-1;
                }
                else {
                    rightX = Math.sqrt(moonR * moonR - y * y);
                }
            }
            else{
                rightX=-1;
            }

            point=Point.fromXY(rightX, y);
            go(point);

            y+=yDelta;

            if (y>=-moonR && y<=moonR){
                if (y-yDelta<-moonR || y-yDelta>moonR){
                    rightX=-1;
                }
                else {
                    rightX = Math.sqrt(moonR * moonR - y * y);
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
        TestFiller7 me = new TestFiller7();
    }
}