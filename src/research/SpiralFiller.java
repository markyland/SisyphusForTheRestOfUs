package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.DrawingContext;
import com.slightlyloony.jsisyphus.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class SpiralFiller extends ATrack {

    double xDelta=.02;
    double yDelta=.02;

    private Ball balls[] = {
            new Ball(0, .60, .3, 20),
            new Ball(0, .10, .25, 16),
            new Ball(0, -.32, .2, 15),
            new Ball(0, -.62, .15, 11),
            new Ball(0, -.85, .1, 8),
    };

    public SpiralFiller() throws Exception {
        super("");

        int numBalls=10;

        int spirals=7;
        double size=1/(double)numBalls;

        ArrayList<Ball> ballsList = new ArrayList<>();

        for (int i=0; i<numBalls; i++) {
            for (int j = 0; j < numBalls+1; j++) {
                Point point = Point.fromXY(-1 + ((j%2==0) ? 0 : size) + i * 2 * size, 1 - j * 2 * size);

                ballsList.add(new Ball(point.x, point.y, size, spirals));
            }
        }

        balls=ballsList.toArray(new Ball[0]);

        trace();
    }

    protected void trace() throws IOException {
        double x=0;
        double y=1;

        Point tempPoint=Point.fromXY(x, y);

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));

        while (tempPoint.y>=-1.5) {
            tempPoint = lineItUp(dc, tempPoint, xDelta, yDelta, -1);

            if (tempPoint.y >= -1.5) {
                tempPoint = lineItUp(dc, tempPoint, xDelta, yDelta, +1);
            }
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private Point lineItUp(DrawingContext dc, Point tempPoint, double xDelta, double yDelta, int multipler){
        List<Ball> balls = getBalls(tempPoint.y, multipler);

        for (Ball ball : balls){
            tempPoint = Point.fromXY(ball.x, tempPoint.y);
            dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));

            eraseAndCircle(dc, ball.size, ball.turns);
        }

        tempPoint = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));

        Point tempPoint2 = Point.fromXY(tempPoint.x, tempPoint.y-yDelta);

        tempPoint2 = Point.fromXY(multipler*Math.sqrt(1-tempPoint2.y*tempPoint2.y), tempPoint2.y);

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint2));

        return tempPoint2;
    }

    private List<Ball> getBalls(double y, final int multiplier){
        ArrayList<Ball> ret = new ArrayList<>();

        for (int i=0; i<balls.length; i++){
            if (balls[i]!=null && y<balls[i].y){
                ret.add(balls[i]);

                balls[i]=null;
            }
        }

        Collections.sort(ret, new Comparator<Ball>() {
            @Override
            public int compare(Ball o1, Ball o2) {
                return multiplier*Double.compare(o1.x, o2.x);
            }
        });

        return ret;
    }

    private void eraseAndCircle(DrawingContext dc, double size, int turns){
        double x=dc.getCurrentPosition().getX();
        double y=dc.getCurrentPosition().getY();

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+xDelta*1.2+size)));

        //    dc.spiralToXY(0, size, 0, size, 15);
        dc.spiralToXY(0, -size*1.15, 0, 0, turns);
        //dc.arcAround(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+xDelta*1.2+size)), 2*Math.PI);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y)));
    }

    public static void main(String args[]) throws Exception {
        SpiralFiller me = new SpiralFiller();
    }

    public class Ball {
        private double x, y, size;
        private int turns;

        public Ball(double x, double y, double size, int turns) {
            this.x = x;
            this.y = y-size-(xDelta*1.2);
            this.size = size;
            this.turns = turns;
        }
    }
}