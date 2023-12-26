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
public class Fruit extends ATrack {
    double eraseSpace=0.0125;

    public Fruit() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        drawSphere(0, 0, .8, 0, 0, true, false);

        drawSphere(0, 0, .075, .33, -.16, false, false);
        drawSphere(0, 0, .075, .4, -.1, false, false);
        drawSphere(0, 0, .075, .5, -.05, false, false);
        drawSphere(0, 0, .075, .4, .03, false, false);
        drawSphere(0, 0, .075, .51, .08, false, false);
        drawSphere(0, 0, .075, .62, .03, false, false);
        drawSphere(0, 0, .075, .6, -.11, false, false);

        go(Point.fromXY(.675, -.038));
        go(Point.fromXY(.77, .06));
        go(Point.fromXY(.675, -.038));

        drawSphere(0, 0, .075, .72, -.065, false, false);
        drawSphere(0, 0, .075, .7, -.2, false, false);
        drawSphere(0, 0, .075, .8, -.14, false, false);
        drawSphere(0, 0, .075, .8, -.24, false, false);
        drawSphere(0, 0, .075, .73, -.33, false, false);
        drawSphere(0, 0, .075, .63, -.22, false, false);
        drawSphere(0, 0, .075, .6, -.34, false, false);
        drawSphere(0, 0, .075, .46, -.36, false, false);
        drawSphere(0, 0, .075, .52, -.27, false, false);
        drawSphere(0, 0, .075, .5, -.15, false, false);
        drawSphere(0, 0, .075, .39, -.23, false, false);
        drawSphere(0, 0, .075, .33, -.355, false, false);
        drawSphere(0, 0, .075, .27, -.26, false, false);
        drawSphere(0, 0, .075, .2, -.36, false, false);

        drawSphere(0, 0, .28, 0, -.15, false, false);

        drawSphere(0, 0, .2, -.1, 0, false, true);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void drawSphere(double xRot, double yRot, double size, double xShift, double yShift, boolean isBowl, boolean isBread){
        Point3D p = new Point3D(0, -size, 0);

        while (xRot<Math.PI/(isBowl ? 3 : 1)){
            Point3D p2 = rotX(p, xRot);
            p2 = rotY(p2, yRot);
            p2 = rotX(p2, -.4);

            Point point = Point.fromXY((isBowl ? 1.1 : 1)*p2.x/2*(p2.z+2), (isBread ? 4 : 1)*p2.y/2*(p2.z+2));

            if (isBread){
                Point3D temp = rotZ(new Point3D(point.x, point. y, 0), -.6);

                point=Point.fromXY(temp.x, temp.y);
            }

            point=Point.fromXY(point.x+xShift, point.y+yShift);

            go(point);

            xRot+=0.0000225/size;
            yRot+=.01;
        }
    }

    private double in(double x, double y){
        double r=1;

        return Math.sqrt(r*r-x*x-y*y);
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    class Point3D{
        public double x, y, z;

        public Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private Point3D rotX(Point3D p, double deg){
        return new Point3D(p.x, p.y*Math.cos(deg)-p.z*Math.sin(deg), p.y*Math.sin(deg) + p.z*Math.cos(deg));
    }

    private Point3D rotY(Point3D p, double deg){
        return new Point3D(p.x*Math.cos(deg) + p.z*Math.sin(deg), p.y, -p.x*Math.sin(deg)+p.z*Math.cos(deg));
    }

    private Point3D rotZ(Point3D p, double deg){
        return new Point3D(p.x * Math.cos(deg) - p.y * Math.sin(deg), p.x * Math.sin(deg) + p.y * Math.cos(deg), p.z);
    }

    public static void main(String args[]) throws Exception {
        Fruit me = new Fruit();
    }
}