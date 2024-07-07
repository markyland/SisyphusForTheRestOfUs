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
public class TestSphere2 extends ATrack {
    double eraseSpace=0.0125;

    public TestSphere2() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        Point3D p = new Point3D(0, -.65, 0);

        double xRot=0;
        double yRot=0;

        while (xRot<Math.PI){
            Point3D p2 = rotX(p, xRot);
            p2 = rotY(p2, yRot);
            p2 = rotX(p2, -.4);

            Point point = Point.fromXY(p2.x/2*(p2.z+2), p2.y/2*(p2.z+2));
//            Point point = Point.fromXY(p2.x, p2.y);

            go(point);

            xRot+=.00005;
            yRot+=.01;
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
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

    public static void main(String args[]) throws Exception {
        TestSphere2 me = new TestSphere2();
    }
}