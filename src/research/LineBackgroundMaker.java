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
public class LineBackgroundMaker extends ATrack {
    private static double maxPointDistance = 0.004;  // approximately 2mm on A16 table...
    private static double maxPointDistanceCenter = 0.001;  // approximately 2mm on A16 table...

    private static final double yDelta=.006;

    double degrees=90;

    private int pixels[][];
    private int width;
    private int height;

    public LineBackgroundMaker() throws Exception {
        super("");

        loadImage();
        trace();
    }

    private void loadImage(){
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\elephant\\elephant-fill.png");

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
        for (int y = 0; y< height; y++){
            for (int x = 0; x< width; x++){
                int val=pixels1D[y* width +x];

                int r=val >> 16 & 0xFF;
                int g=val >> 8 & 0xFF;
                int b=val & 0xFF;

                pixels[y][x]=val;
            }
        }
    }

    protected void trace() throws IOException {
        Point point=Point.fromXY(-1.1, 1);
        lineTo(point);

        // squareV();

        squareH(yDelta, true);

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    private void squareH(double yDelta, boolean effect){
        Point point;

        point=Point.fromXY(-1.1, 1.1);
        lineTo(point);

        double y=1.1;

        while (y>=-1.1){
            line(y, true);

            y-=yDelta;

            point=Point.fromXY(1.1, y);
            lineTo(point);

            line(y, false);

            y-=yDelta;

            point=Point.fromXY(-1.1, y);
            lineTo(point);
        }
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    private void line(double y, boolean isRight) {
        double x = isRight ? -1.1 : 1.1;

        while (isRight ? (x <= 1.1) : (x>=-1.1)) {
            Point point = Point.fromXY(x, y);

            Point point2=applyEffect(point);

            go(point2);

            x += (isRight ? 1 : -1) * (point2.rho<.1 ? maxPointDistanceCenter : maxPointDistance);
        }
    }

    private Point applyEffect(Point point){
        int color = getFill(point);

        //white and black is reserved for no effect

        //red is the background!

        double yAdjustment=0;

        if (color==Color.red.getRGB()){
            double waveHeight=.015;
            double waveFreq=40;

            yAdjustment=Math.sin(waveFreq*point.x)*waveHeight;
        }

        Point point2=Point.fromXY(point.x, point.y+yAdjustment);
        
        int color2=getFill(point2);

        if (color!=color2){ //we've got into a another fill.  lets go back to the unadjusted point
            point2=point;
        }
        
        return point2;
    }

    private int getFill(Point point){
        double x=point.x;
        double y=point.y;

        if (point.rho>1){
            return Color.red.getRGB();
        }

        return pixels[(int)Math.round((height-1)-(height-1)*(y/2+.5))][(int)Math.round((width-1)*(x/2+.5))];
    }

    public static void main(String args[]) throws Exception {
        LineBackgroundMaker me = new LineBackgroundMaker();
    }
}