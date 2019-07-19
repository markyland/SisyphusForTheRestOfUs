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
public class TestFiller14 extends ATrack {
    private static double maxPointDistance = 0.01;  // approximately 2mm on A16 table...

    private static final double yDelta=.02;
    private static final double HEIGHT=.09;
    private static final double LOOKAHEAD=.04;

    private int pixels[][];
    private int width;
    private int height;

    public TestFiller14() throws Exception {
        super("");

        loadImage();
        trace();
    }

    private void loadImage(){
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\empty.png");

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
        Point point=Point.fromXY(-1.1, 1);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));

        squareH();

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(){
        Point point;

        point=Point.fromXY(-1.1, 1);
        go(point);

        double y=1;

        while (y>=-1.1){
            line(y, true);

            y-=yDelta;

            point=Point.fromXY(1.1, y);
            go(point);

            line(y, false);

            y-=yDelta;

            point=Point.fromXY(-1.1, y);
            go(point);
        }

        line(-1.1, true);
    }

    private void line(double y, boolean isRight) {
        double yOffset=0;

        boolean lastNowIn=false;

        double x = isRight ? -1.1 : 1.1;

        while (isRight ? (x <= 1.1) : (x>=-1.1)) {
            Point point = Point.fromXY(x, y);

            Point point2=point;

            //-----------effect on point 3-----------
            boolean nowIn = in(point.x, point.y);
            boolean lookaheadIn = in(point.x + (isRight ? LOOKAHEAD : -LOOKAHEAD), point.y);

            if (isRight) {
                if (!nowIn && lookaheadIn) {
                    yOffset = HEIGHT;
                    x = point.x + (isRight ? LOOKAHEAD : -LOOKAHEAD);
                } else if (!nowIn && lastNowIn) {
                    yOffset = 0;
                    x = point.x + (isRight ? LOOKAHEAD : -LOOKAHEAD);
                }
            }

            point2 = Point.fromXY(x, point2.y + yOffset);
            //---------------------------------------

            go(point2);

            x += (isRight ? 1.1 : -1.1) * maxPointDistance;

            lastNowIn=nowIn;
        }
    }

    private boolean in(double x, double y){
        return getFill(Point.fromXY(x, y))==0;

        //return x>-.4 && x<.4 && y>-.4 && y<.4;
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
        TestFiller14 me = new TestFiller14();
    }
}