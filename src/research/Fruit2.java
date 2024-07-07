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
public class Fruit2 extends ATrack {
    double eraseSpace=0.0125;

    public Fruit2() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        drawSphere(0, 0, .65, 0, -.1, true, false);

        drawSphere(0, 0, .2, -.1, -.3, false, false);
        drawSphere(0, 0, .2, .3, -.3, false, false);


        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void drawSphereTest(double xRot, double yRot, double size, double xShift, double yShift, boolean isBowl, boolean isBread) {
        drawSphere(xRot, yRot, size, xShift, yShift, isBowl, isBread);
        drawSphere(xRot-.2, yRot, size, xShift, yShift, isBowl, isBread);
        drawSphere(xRot, yRot, size, xShift, yShift, isBowl, isBread);
        drawSphere(xRot, yRot, size, xShift, yShift, isBowl, isBread);
        drawSphere(xRot, yRot, size, xShift, yShift, isBowl, isBread);
    }

    private void drawSphere(double xRot, double yRot, double size, double xShift, double yShift, boolean isBowl, boolean isBread){
        Point3D p = new Point3D(0, -size, 0);

        while (xRot<Math.PI/(isBowl ? 3 : 1)){
            Point3D p2 = rotX(p, xRot);
            p2 = rotY(p2, yRot);
            p2 = rotX(p2, -.4);

            Point point = Point.fromXY((isBowl ? 1.2 : 1)*p2.x/2*(p2.z+2), (isBread ? 4 : 1)*p2.y/2*(p2.z+2));

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
        Fruit2 me = new Fruit2();
    }
}