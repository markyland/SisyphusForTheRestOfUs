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
public class SphereFiller extends ATrack {
    double eraseSpace=0.0125;

    private static double BALL_SIZE=.1;

    public SphereFiller() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        int numBalls=(int)Math.ceil(2/BALL_SIZE)+1;

        for (int j=0; j<numBalls; j++) {
            go(Point.fromXY(-1+j*2.14*BALL_SIZE, -1+((j%2==0) ? -BALL_SIZE : 0)));

            for (int i = 0; i < numBalls; i++) {
                reverseSphere();
                sphere();

                double x = dc.getCurrentPosition().getX();
                double y = dc.getCurrentPosition().getY();
                go(Point.fromXY(x, y+.039));
            }

            go(Point.fromXY(-1, 1));
            go(Point.fromXY(-1, -1));
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
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

    private void sphere(){
        double x = dc.getCurrentPosition().getX();
        double y = dc.getCurrentPosition().getY();

        Point3D p = new Point3D(0, -BALL_SIZE, 0);

        double xRot=0;
        double yRot=0;

        while (xRot<Math.PI){
            Point3D p2 = rotX(p, xRot);
            p2 = rotY(p2, yRot);
            p2 = rotX(p2, -.3);

            Point point = Point.fromXY(x+p2.x/2*(p2.z+2), y+BALL_SIZE+p2.y/2*(p2.z+2));

            go(point);

            xRot+=.005;
            yRot+=.2;
        }
    }

    private void reverseSphere(){
        double x = dc.getCurrentPosition().getX();
        double y = dc.getCurrentPosition().getY();

        Point3D p = new Point3D(0, -BALL_SIZE, 0);

        double xRot=0.005*200;
        double yRot=.2*200;

        while (xRot>=0){
            Point3D p2 = rotX(p, xRot);
            p2 = rotY(p2, yRot);
            p2 = rotX(p2, -.3);

            Point point = Point.fromXY(x+p2.x/2*(p2.z+2), y+BALL_SIZE+p2.y/2*(p2.z+2));

            go(point);

            xRot-=.005;
            yRot-=.2;
        }
    }

    private Point3D rotX(Point3D p, double deg){
        return new Point3D(p.x, p.y*Math.cos(deg)-p.z*Math.sin(deg), p.y*Math.sin(deg) + p.z*Math.cos(deg));
    }

    private Point3D rotY(Point3D p, double deg){
        return new Point3D(p.x*Math.cos(deg) + p.z*Math.sin(deg), p.y, -p.x*Math.sin(deg)+p.z*Math.cos(deg));
    }

    public static void main(String args[]) throws Exception {
        SphereFiller me = new SphereFiller();
    }
}