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
public class HorizontalVerticalTester extends ATrack {

    private static final double delta=.03;

    private int pixels[][];
    int width;
    int height;

    public HorizontalVerticalTester() throws Exception {
        super("");

        trace();
    }

    protected void trace() throws IOException {
        loadPict();

        squareH(delta, true);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void loadPict(){
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\square.png");

        Image img = icon.getImage();

        width = img.getWidth(null);
        height = img.getHeight(null);
        int[] pixels1D = new int[width * height];

        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels1D, 0, width);

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

                pixels[y][x]=(r+g+b)/3;
            }
        }
    }

    private void squareH(double yDelta, boolean effect){
        Point point;

        point=Point.fromXY(-1.1, 1);
        go(point);

        double y=1+yDelta/2;

        while (y>=-1.1){
            line(y, true, effect);

            y-=yDelta;

            point=Point.fromXY(1.1, y);
            go(point);

            line(y, false, effect);

            y-=yDelta;

            point=Point.fromXY(-1.1, y);
            go(point);
        }
    }

    private void line(double y, boolean isRight, boolean effect) {
        double xOffset=0;
        double yOffset=0;

        double lastY=0;

        double x = isRight ? -1.1 : 1.1;

        while (isRight ? (x <= 1.1) : (x>=-1.1)) {
            Point point = Point.fromXY(x, y);
            go(point);

            double upAmount=getUpAmount(point);

            if (upAmount!=0){
                point=Point.fromXY(x, y-upAmount);
                go(point);

                point=Point.fromXY(x, y);
                go(point);
            }

            x += (isRight ? 1 : -1) * delta;
        }
    }

    private double getUpAmount(Point point){
        if (!isBlack(point)){   //if point is white
            return 0;
        }

        if (isBlack(Point.fromXY(point.x, point.y-delta))){     //its the last point
            return 0;
        }

        Point p=point;
        while (isBlack(p)){
            p=Point.fromXY(p.x, p.y+delta);
        }

        return point.y-p.y-delta;
    }

    private boolean isBlack(Point point){
        return getFill(point)==0;
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

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        HorizontalVerticalTester me = new HorizontalVerticalTester();
    }
}