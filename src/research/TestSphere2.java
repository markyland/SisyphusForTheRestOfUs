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
    private static double maxPointDistance = 0.01;  // approximately 2mm on A16 table...

  //  private static final double yDelta=.005;
    private static final double xDelta=.016;

    private int pixels[][];
    private int width;
    private int height;

    public TestSphere2() throws Exception {
        super("");

        loadImage();
        trace();
    }

    private void loadImage(){
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\sun.png");

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

                pixels[y][x]=(r+b+g)/3;
            }
        }
    }

    protected void trace() throws IOException {
//        Point point=Point.fromXY(-1.1, 1.1);
      //  dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        // squareV();

      //  squareH(yDelta, false);
        squareH(.005, false);

     //   squareH2(.01, true);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(double yDelta, boolean effect){
        Point point;

        point=Point.fromXY(-1, -1);
        go(point);

        double y=0;

        while (y>=-1.1){
            line(y, true, effect);

            y-=yDelta;

            yDelta+=.001;

            point=Point.fromXY(1.1, y);
            go(point);

            line(y, false, effect);

            y-=yDelta;

            yDelta+=.001;


            point=Point.fromXY(-1.1, y);
            go(point);
        }

        line(1.1, true, effect);
    }

    private void squareH2(double yDelta, boolean effect){
        Point point;

        point=Point.fromXY(-1.1, 1.5);
        go(point);

        double y=1;

        while (y>=.2){
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
        double lastY=0;

        double x = isRight ? -1.1 : 1.1;

        while (isRight ? (x <= 1.1) : (x>=-1.1)) {
            Point point = Point.fromXY(x, y);

            Point point2=point;

            //-----------effect on point 3-----------
            if (effect) {
                double yOffset = in(point.x, point.y);

                point2 = Point.fromXY(x, y+yOffset);
            }
            //---------------------------------------

            go(point2);

            x += (isRight ? 1 : -1) * maxPointDistance;
        }
    }

    private double in(double x, double y){
        int fill=255-getFill(Point.fromXY(x, y));

        return fill/255.0*.1;
    }

    private double lastGoX;
    private double lastGoY;

    private void go(Point point){

        if (Math.abs(lastGoX-point.x)<1e-6 && Math.abs(lastGoY-point.y)<1e-6){
            return;
        }

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        lastGoX=point.x;
        lastGoY=point.y;
    }

    private int getFill(Point point){
        double x=point.x;
        double y=point.y;

        if (x>=1 || x<=-1 || y>=1 || y<=-1){
            return 255;
        }

//        System.err.println(point.x + " " + point.y);

        return pixels[(int)Math.round((height-1)-(height-1)*(y/2+.5))][(int)Math.round((width-1)*(x/2+.5))];
    }

    public static void main(String args[]) throws Exception {
        TestSphere2 me = new TestSphere2();
    }
}