package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.DrawingContext;
import com.slightlyloony.jsisyphus.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class TestFiller3 {

    double eraseSpace=0.0125;

    public TestFiller3() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\question-table.png");

                Image img = icon.getImage();

                int width = img.getWidth(null)-200;
                int height = img.getHeight(null)-200;
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
                int pixels[][] = new int[height][width];
                for (int y=0; y<height; y++){
                    for (int x=0; x<width; x++){
                        int val=pixels1D[y*width+x];

                        int r=val >> 16 & 0xFF;
                        int g=val >> 8 & 0xFF;
                        int b=val & 0xFF;

                        if (r==0 && g==0 && b==0){
                            pixels[y][x]=1;
                        }
                    }
                }


//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double x=0;
                double y=1;

                double xDelta=.02;
                double yDelta=.02;

                Point tempPoint=Point.fromXY(x, y);

                dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));

                while (tempPoint.y>=-1) {
                    tempPoint = lineItUp(dc, tempPoint, xDelta, yDelta, -1, pixels, width, height);

                    if (tempPoint.y >= -1) {
                        tempPoint = lineItUp(dc, tempPoint, xDelta, yDelta, +1, pixels, width, height);
                    }
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
                dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
            }
        }.trace();
    }

    private Point lineItUp(DrawingContext dc, Point tempPoint, double xDelta, double yDelta, double multipler, int pixels[][], int width, int height){
        tempPoint = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));

        tempPoint = Point.fromXY(tempPoint.x, tempPoint.y-yDelta);

        tempPoint = Point.fromXY(multipler*Math.sqrt(1-tempPoint.y*tempPoint.y), tempPoint.y);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(tempPoint));

        return tempPoint;
    }

    private void eraseAndCircle(DrawingContext dc, double size){
        double curRho=dc.getCurrentPosition().getRho();
        double curTheta=dc.getCurrentPosition().getTheta();

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(curRho-eraseSpace*2, curTheta)));
        dc.arcAroundRT(size, curTheta+Math.PI, Math.PI*2);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(curRho, curTheta)));
    }

    private int getFill(double rho, double theta, int[][] pixels, int height, int width){
        Point point = Point.fromRT(rho, theta);

        double x=point.x;
        double y=point.y;

        int pictY=(int)Math.round(height-height*(y/2+.5));
        int pictX=(int)Math.round(width*(x/2+.5));

        if (rho>1 || pictX<0 || pictY<0 || pictX>=width || pictY>=height){
            return 0;
        }

        return pixels[pictY][pictX];
    }

    public static void main(String args[]) throws Exception {
        TestFiller3 me = new TestFiller3();
    }
}