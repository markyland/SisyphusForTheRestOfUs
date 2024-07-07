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
public class New3DGrid extends ATrack {
    private static double maxPointDistance = 0.001;  // approximately 2mm on A16 table...

    private static final double yDelta=.024;
    private static final double xDelta=.016;

    public New3DGrid() throws Exception {
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

        double y=1.1 ;

        while (y>=-1.1){
            line(y, true, effect);

            y-=getYDelta(y);

            point=Point.fromXY(1.1, y);
            go(point);

            line(y, false, effect);

            y-=getYDelta(y);

            point=Point.fromXY(-1.1, y);
            go(point);
        }

        line(1.1, true, effect);
    }

    private double getYDelta(double y){
        if (y>0){
            return yDelta*(Math.abs(y)+1);
        }
        else{
            return yDelta/((Math.abs(y*3)+1));
        }
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

    private Point effect(Point p){
        double r=.8;

        // System.err.println(x + " " + y + " " " + Math.sqrt(r*r-x*x-y*y));
        double z=0;

        double rho=Point.fromXY(p.x, p.y).rho;

        if (rho<.4) {
            z = .09;
        }
        if (rho<.5) {
            z = .08;
        }
        else if (rho<.6) {
            z = .06;
        }
        else if (rho<.7) {
            z = .04;
        }
        else if (rho<.78) {
            z = .02;
        }

        //  z=Math.min(.3, z);

//        if (p.x*p.x+p.y*p.y>r*r){
//            z=0;
//        }

        Point3D p3d=new Point3D(p.x, z, p.y);

//        p3d=rotX(p3d, -.1);

        return Point.fromXY(.8*p3d.x/(p3d.z+1.15), (p3d.y-.4)/(p3d.z+1.15)+.4);
    }

    private Point3D rotX(Point3D p, double deg){
        return new Point3D(p.x, p.y*Math.cos(deg)-p.z*Math.sin(deg), p.y*Math.sin(deg) + p.z*Math.cos(deg));
    }

    private Point3D rotY(Point3D p, double deg){
        return new Point3D(p.x*Math.cos(deg) + p.z*Math.sin(deg), p.y, -p.x*Math.sin(deg)+p.z*Math.cos(deg));
    }

    class Point3D{
        public double x, y, z;

        public Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private void go(Point point){
        point=effect(point);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        New3DGrid me = new New3DGrid();
    }
}