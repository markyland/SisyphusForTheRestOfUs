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
public class TestFiller4 extends ATrack {

    private double scale=5;
    private double gridLength=2.0/scale;

    private int gridX=0;
    private int gridY=0;

    public TestFiller4() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        Point point=Point.fromXY(-1, 1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        for (int y=0; y<scale; y++){
            for (int x=0; x<scale; x++) {
                gridY=y;
                gridX=isBackwardsX() ? (((int)scale)-x-1) : x;

                if ((gridY*scale+gridX)%2==0){
                    squareH(20);
                }
                else{
                    squareV(20);
                }
            }
        }

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

        while (true) {
            point=Point.fromXY(1, y);
            go(point);

            y-=yDelta;

            if (y<=-1){
                break;
            }

            point=Point.fromXY(1, y);
            go(point);

            point=Point.fromXY(-1, y);
            go(point);

            y-=yDelta;

            if (y<=-1){
                break;
            }

            point=Point.fromXY(-1, y);
            go(point);
        }

        point=Point.fromXY(1, y);
        go(point);
    }

    private void squareV(int numRows){
        double x=-1;

        double xDelta=2.0/numRows;

        Point point;

        point=Point.fromXY(x, 1);
        go(point);

        while (true) {
            point=Point.fromXY(x, -1);
            go(point);

            x+=xDelta;

            if (x>=1){
                break;
            }

            point=Point.fromXY(x, -1);
            go(point);

            point=Point.fromXY(x, +1);
            go(point);

            x+=xDelta;

            if (x>=1){
                break;
            }

            point=Point.fromXY(x, +1);
            go(point);
        }

        point=Point.fromXY(x, -1);
        go(point);
    }

    private void go(Point point){
        double x=point.x;
        double y=point.y;

        if (isBackwardsX()){
            x=-x;
        }

        if (isBackwardsY()){
            y=-y;
        }

        x=(x+1)/scale - 1 + gridX*gridLength;
        y=(y-1)/scale + 1 - gridY*gridLength;

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y)));
    }

    private boolean isBackwardsX(){
        return gridY % 2 == 1;
    }

    private boolean isBackwardsY(){
        return gridX % 2 == 1;
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
        TestFiller4 me = new TestFiller4();
    }
}