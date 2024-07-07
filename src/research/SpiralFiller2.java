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
public class SpiralFiller2 extends ATrack {

    private static double BALL_SIZE=.25;

    public SpiralFiller2() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        go(Point.fromXY(-1, 1));

        int numBalls=(int)Math.ceil(2/BALL_SIZE)+1;

        for (int j=0; j<numBalls; j++) {
            for (int i = 0; i < numBalls; i++) {
                eraseAndCircle(dc, BALL_SIZE, 7, true);
            }

            double x = dc.getCurrentPosition().getX();
            double y = dc.getCurrentPosition().getY();
            go(Point.fromXY(x - BALL_SIZE / 2, y - BALL_SIZE / 2));

            for (int i = 0; i < numBalls; i++) {
                eraseAndCircle(dc, BALL_SIZE, 7, false);
            }

            x = dc.getCurrentPosition().getX();
            y = dc.getCurrentPosition().getY();
            go(Point.fromXY(x + BALL_SIZE / 2, y - BALL_SIZE / 2));
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void eraseAndCircle(DrawingContext dc, double size, int turns, boolean isRight){
        double x=dc.getCurrentPosition().getX();
        double y=dc.getCurrentPosition().getY();

        double mult=isRight ? 1 : -1;

        dc.spiralToXY(mult*size/2, 0, mult*size/2, 0, (int)(Math.round(mult*turns)));
        go(Point.fromXY(x+mult*size, y));
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }


    public static void main(String args[]) throws Exception {
        SpiralFiller2 me = new SpiralFiller2();
    }
}