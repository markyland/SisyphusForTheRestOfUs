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
public class TheOne extends ATrack {

    double rho=0;
    double theta=0;

    double eraseSpace=0.01;

    double x=0;
    double y=-.76;

    public TheOne() throws Exception {
        super("");

        trace();
    }

    @Override
    protected void trace() throws IOException {
        if (true) {
            go(Point.fromXY(x, y));

            spiral(true);

            for (int i = 0; i < 16; i++) {
                theta += Math.PI * 2;

                spiral(false);

                theta += Math.PI * 2;

                spiral(true);
            }
        }

        //---------------------------------------------

        if (false) {
            Point3D p = new Point3D(0, -.47, 0);

            double xRot = 0;
            double yRot = 0;

            while (xRot < Math.PI) {
                Point3D p2 = rotX(p, xRot);
                p2 = rotY(p2, yRot);
                p2 = rotX(p2, -.65);

                Point point = Point.fromXY(p2.x / 2 * (p2.z + 2), p2.y / 2 * (p2.z + 2));
//            Point point = Point.fromXY(p2.x, p2.y);

                point = Point.fromXY(point.x, point.y - .3209645310629238);

                System.err.println(point.y);
                go(point);

                xRot += .00007;
                yRot += .01;
            }
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void spiral(boolean forward){
        int i=0;
        while (true){
            theta += forward ? .01 : -.01;

            rho = theta / (2 * Math.PI) * (eraseSpace);

            Point dest = Point.fromRT(rho, theta);

            dest=Point.fromXY(2.5*dest.x+x, dest.y+y);

            if (dest.rho<1){
                break;
            }

            if (i++>10000){
                return;
            }
        }

        while (true){
            theta+=forward ? .01 : -.01;

            rho=theta / (2 * Math.PI) * (eraseSpace);

            Point dest = Point.fromRT(rho, theta);

            dest=Point.fromXY(2.5*dest.x+x, dest.y+y);

            if (dest.rho>1.1){
                break;
            }

            go(dest);
        }
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

    private void go(Point p){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(p));

    }

    public static void main(String args[]) throws Exception {
        TheOne me = new TheOne();
    }
}