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
public class FadeGrid extends ATrack {
    private static double maxPointDistance = 0.001;  // approximately 2mm on A16 table...

    public FadeGrid() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        go(Point.fromXY(-1.1, 1.1), false);

        squareH(.01, .06);
        squareY(.01, .06);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(double minDelta, double maxDelta) {
        square(minDelta, maxDelta, false);
    }

    private void squareY(double minDelta, double maxDelta) {
        square(minDelta, maxDelta, true);
    }

    private void square(double minDelta, double maxDelta, boolean flip){
        Point point;

        double y=1.1;

        while (y>=-1.1){
            double delta=((y+1)/2.0)*(maxDelta-minDelta)+minDelta;

            line(y, true, flip);

            y-=delta;

            point=Point.fromXY(1.1, y);
            go(point, flip);

            line(y, false, flip);

            y-=delta;

            point=Point.fromXY(-1.1, y);
            go(point, flip);
        }

        line(1.1, true, flip);
    }

    private void line(double y, boolean isRight, boolean flip) {
        double x = isRight ? -1.1 : 1.1;

        while (isRight ? (x <= 1.1) : (x>=-1.1)) {
            Point point = Point.fromXY(x, y);

            go(point, flip);

            x += (isRight ? 1 : -1) * maxPointDistance;
        }
    }

    private void go(Point point, boolean flip){
        if (flip){
            dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(point.y, point.x)));
        }
        else{
            dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
        }
    }

    public static void main(String args[]) throws Exception {
        FadeGrid me = new FadeGrid();
    }
}