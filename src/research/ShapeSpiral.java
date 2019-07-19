package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Delta;
import com.slightlyloony.jsisyphus.Point;
import com.slightlyloony.jsisyphus.lines.Line;
import com.slightlyloony.jsisyphus.lines.StraightLine;

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
public class ShapeSpiral extends ATrack {

    private static double maxPointDistance = 0.01;  // approximately 2mm on A16 table...

    private double incRho=.025;
    private double startRho=.025;
    private double curRho=.1;

    public ShapeSpiral() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        for (curRho=startRho; curRho<=1.0001; curRho+=incRho){
            line(Point.fromRT(1, 2*Math.PI*(1/3.0)));
            line(Point.fromRT(1, 2*Math.PI*(2/3.0)));
            line(Point.fromRT(1, 0));
        }

        dc.renderPNG("c:\\users\\mark\\desktop\\shapespiral.png");
        dc.write("c:\\users\\mark\\desktop\\shapespiral.thr");

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\shapespiral.png");
    }

    private Point curPoint=Point.fromRT(0, 0);

    private void line(Point point){
        System.err.println(curRho);

        point=Point.fromRT(point.rho*curRho, point.theta);

        Point tempPoint=curPoint.vectorTo(point);

        Line line = new StraightLine( maxPointDistance, tempPoint.x, tempPoint.y );

        Point point2=curPoint;

        for (Delta delta : line.getDeltas()){
            point2=Point.fromXY(point2.x+delta.x, point2.y+delta.y);

            double curTheta=point2.theta % (Math.PI*2);

            if (curTheta<0){
                curTheta+=Math.PI*2;
            }

            System.err.println(curTheta);

            double curRho2=curRho+(curTheta/(Math.PI*2))*incRho;

            Point point3=Point.fromRT((1-curRho2)*point2.rho+curRho2*curRho2, point2.theta);      //combine with normal spiral

            go(point3);
        }

        curPoint=point;

        //    draw( line );
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        ShapeSpiral me = new ShapeSpiral();
    }
}