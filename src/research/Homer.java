package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Delta;
import com.slightlyloony.jsisyphus.DrawingContext;
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
public class Homer extends ATrack {

    private boolean backwards=false;

    int pixels[][];
    int width;
    int height;

    double yDelta;

    private Ball balls[] = {
            new Ball(.021, .07, .02, 1),
            new Ball(-.25, .14, .02, 1)
    };

    private static double maxPointDistance = 0.001;  // approximately 2mm on A16 table...

    public Homer() throws Exception {
        super("");

        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\homer-table.png");

        Image img = icon.getImage();

        width = img.getWidth(null)-200;
        height = img.getHeight(null)-200;
        int[] pixels1D = new int[width * height];

        PixelGrabber pg = new PixelGrabber(img, 100, 100, width, height, pixels1D, 0, width);

        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("Error: Interrupted Waiting for Pixels");
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            throw new IllegalStateException("Error: Image Fetch Aborted");
        }

        //switch to 2 dim array
        pixels = new int[height][width];
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int val=pixels1D[y*width+x];

                int r=val >> 16 & 0xFF;
                int g=val >> 8 & 0xFF;
                int b=val & 0xFF;

                if (r==255 && g==255 && b==255){
                    pixels[y][x]=0;
                }
                if (r==255 && g==0 && b==0){
                    pixels[y][x]=1;
                }
                else if (r==0 && g==255 && b==0){
                    pixels[y][x]=2;
                }
                else if (r==0 && g==0 && b==255){
                    pixels[y][x]=3;
                }
                else if (r==0 && g==0 && b==0){
                    pixels[y][x]=4;
                }
            }
        }

        trace();
    }

    protected void trace() throws IOException {
        Point point=Point.fromXY(-1.1, 1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        squareH(150);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    double y=1;

    private void squareH(int numRows){
        yDelta=2.0/numRows;

        Point point;

        point=Point.fromXY(-1.1, y);
        go(point);

        for (int i=0; i<numRows/2; i++) {
            point=Point.fromXY(1.1, y);
            line(point);

            y-=yDelta;

            point=Point.fromXY(1.1, y);
            go(point);

            point=Point.fromXY(-1.1, y);
            line(point);

            y-=yDelta;

            point=Point.fromXY(-1.1, y);
            go(point);
        }

        point=Point.fromXY(1.1, y);
        line(point);
    }

    private void line(Point point){
        Point tempPoint=dc.getCurrentRelativePosition().vectorTo(point);

        Line line = new StraightLine( maxPointDistance, tempPoint.x, tempPoint.y );

        Point point2=dc.getCurrentRelativePosition();

        for (Delta delta : line.getDeltas()){
            if (Math.abs(delta.y)>.001){
                System.err.println();
            }

            point2=Point.fromXY(point2.x+delta.x, point2.y+delta.y);

            int fill=getFill(point2);

            Point point3=point2;

            if (fill==0){
                //   point3=Point.fromXY(point3.x, point3.y+Math.sin(10*point3.x+point3.y*20)*.008);
            }
            if (fill==1){
//                boolean raised = ((int)(point3.x/.11-point3.y*9))%2==0;
//
//                point3=Point.fromXY(point3.x, point3.y+(raised ? .008 : 0));

                double length=.35;
                //boolean raised = (point3.x+1)%length<length/2;
                boolean raised = (point3.x+1+(Math.sin(-point3.y*1.3))+1)%length<length*.45;
                point3=Point.fromXY(point3.x, point3.y+(raised ? .015 : 0));

//                double blah=.02;
//                point3=Point.fromXY(point3.x, Math.round(point3.y/blah)*blah);
            }
            else if (fill==2){
                point3=Point.fromXY(point3.x, point3.y+Math.sin(10*point3.x+point3.y*20)*.008);
            }
            else if (fill==3){
                //point3=Point.fromXY(point2.x, Math.round(point2.y/(yDelta*2))*(yDelta*2));

                point3=Point.fromXY(point3.x, point3.y+Math.sin(65*point3.x)*.006);
            }

            int fill2=getFill(point3);

            if (fill!=fill2){ //we've got into a another fill.  its a tricky situation but default should be good
                point3=point2;
            }

            for (Ball ball : balls) {
                if ((point3.x >= ball.x ^ (point3.x + delta.x) >= ball.x) && (point3.y <= ball.y ^ (point3.y + yDelta <= ball.y))) {
                    eraseAndCircle(dc, ball.size, ball.turns);
                }
            }

            go(point3);
        }
    }

    private int getFill(Point point){
        double x=point.x;
        double y=point.y;

        if (x>=1 || x<=-1 || y>=1 || y<=-1){
            return 0;
        }

//        System.err.println(point.x + " " + point.y);

        return pixels[(int)Math.round((height-1)-(height-1)*(y/2+.5))][(int)Math.round((width-1)*(x/2+.5))];
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

//    private void moveTo(Point fromPoint, Point toPoint){
//        if (fromPoint.rho<=1 && toPoint.rho>1){
//            double m=(toPoint.y-fromPoint.y)/(toPoint.x-fromPoint.x);
//
//            while ()
//        }
//
//
//        tempPoint = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
//        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));
//
//        tempPoint = Point.fromXY(tempPoint.x, tempPoint.y-yDelta);
//
//        Point tempPoint2 = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
//
//        double diff=tempPoint2.theta-tempPoint.theta;
//
//        if (!Double.isNaN(diff)) {
//            dc.arcAroundTableCenter(diff);
//        }
//    }

    private void eraseAndCircle(DrawingContext dc, double size, int turns){
        double x=dc.getCurrentPosition().getX();
        double y=dc.getCurrentPosition().getY();

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+0.024+size)));

        //    dc.spiralToXY(0, size, 0, size, 15);
        dc.spiralToXY(0, -size, 0, 0, turns);
        dc.arcAround(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+0.024+size)), 2*Math.PI);
        dc.arcAround(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+0.024+size)), 2*Math.PI);
        dc.arcAround(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+0.024+size)), 2*Math.PI);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y)));

//        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y+xDelta*1.2)));
//        //  dc.arcAroundRT(size, 0, Math.PI*2);
//        dc.spiralToXY(0, size, 0, size, 15);
//        dc.spiralToXY(0, -size, 0, 0, 15);
//        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromXY(x, y)));
    }

    public class Ball {
        private double x, y, size;
        private int turns;

        public Ball(double x, double y, double size, int turns) {
            this.x = x;
            this.y = y-size;
            this.size = size;
            this.turns = turns;
        }
    }

    public static void main(String args[]) throws Exception {
        Homer me = new Homer();
    }
}