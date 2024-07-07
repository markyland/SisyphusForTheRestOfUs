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
public class TestFiller17 extends ATrack {
    private static double maxPointDistance = 0.003;  // approximately 2mm on A16 table...

    private static final double yDelta=.016;
    private static final double xDelta=.016;

    double degrees=90;

    private int pixels[][];
    private int width;
    private int height;

    public TestFiller17() throws Exception {
        super("");

        loadImage();
        trace();
    }

    private void loadImage(){
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\floor.png");

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
        // squareV();

        // squareH(.01, false);
        squareH(yDelta, true);

        go(Point.fromXY(2.1, 2.1));
        go(Point.fromXY(2.1, -.2));
        go(Point.fromXY(-2.1, -.2));
        go(Point.fromXY(.23, -.2));

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(double yDelta, boolean effect){
        Point point;

        point=Point.fromXY(-2.1, 2.1);
        go(point);

        double y=2.1;

        while (y>=-2.1){
            line(y, true, effect);

            y-=yDelta;

            point=Point.fromXY(2.1, y);
            go(point);

            line(y, false, effect);

            y-=yDelta;

            point=Point.fromXY(-2.1, y);
            go(point);
        }

        line(2.1, true, effect);
    }

    private void line(double y, boolean isRight, boolean effect) {
        double x = isRight ? -2.1 : 2.1;

        while (isRight ? (x <= 2.1) : (x>=-2.1)) {
            Point point = Point.fromXY(x, y);

            Point point2=point;

            //-----------effect on point 3-----------
            if (effect) {
                double fill = getFill(point);

                double xOffset=0;
                double yOffset=0;

                if (y>-.2){
                    double freq=25;
                    double height=.015;
                    double skew=.02;

                    yOffset=Math.sin(x*freq)*height;
                    xOffset=Math.sin(x*freq)*skew;
                }
                else if (y<=-.2){
                    double freq=15;
                    double height=.03;
                    double skew=.03;

                    yOffset=Math.sin(x*freq)*height/2;
                    xOffset=Math.sin(x*freq)*skew - y*2.6 - .7;
                }

                double fillPert = getFill(Point.fromXY(point.x+xOffset, point.y));

                if (fillPert==0) {
                    yOffset += Math.sin(x*50)*.03;
                }

                point2 = Point.fromXY(x + xOffset, point2.y + yOffset);
            }
            //---------------------------------------

            go(point2);

            x += (isRight ? 1 : -1) * maxPointDistance;
        }
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
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
        TestFiller17 me = new TestFiller17();
    }
}