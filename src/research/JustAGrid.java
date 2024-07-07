package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class JustAGrid extends ATrack {
    private static double maxPointDistance = 0.001;  // approximately 2mm on A16 table...

    private static final double yDelta=.012;
    private static final double xDelta=.016;

    public JustAGrid() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {

        squareH(yDelta, true);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(double yDelta, boolean effect){
        Point point;

        point=Point.fromXY(-1.1, 1);
        go(point);

        double y=1;

        while (y>=-1.1){
            line(y, true, effect);

            y-=yDelta;

            point=Point.fromXY(1.1, y);
            go(point);

            line(y, false, effect);

            y-=yDelta;

            point=Point.fromXY(-1.1, y);
            go(point);
        }

        line(1.1, true, effect);
    }

    private void squareV(){
        Point point;

        point=Point.fromXY(1.1, 1.1);
        go(point);

        double x=1.1;

        while (x>=-1.1){
            point=Point.fromXY(x, -1.1);
            go(point);

            x-=xDelta;

            point=Point.fromXY(x, -1.1);
            go(point);

            point=Point.fromXY(x, 1.1);
            go(point);

            x-=xDelta;

            point=Point.fromXY(x, 1.1);
            go(point);
        }

        point=Point.fromXY(-1.1, 1.1);
        go(point);
    }

    private void line(double y, boolean isRight, boolean effect) {
        double xOffset=0;
        double yOffset=0;

        double lastY=0;

        double x = isRight ? -1.1 : 1.1;

        while (isRight ? (x <= 1.1) : (x>=-1.1)) {
            Point point = Point.fromXY(x, y);

            go(point);

            x += (isRight ? 1 : -1) * maxPointDistance;
        }
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        JustAGrid me = new JustAGrid();
    }
}