package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.DrawingContext;
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
public class TestFiller {

    double eraseSpace=0.0125;

    public TestFiller() throws Exception {
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

                double rho=0;
                double theta=0;

                boolean eyeDrawn=true;

                while (rho<=1){
                    theta+=.04;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    Point dest = Point.fromRT(rho, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));

                    int fillNumber=getFill(rho, theta, pixels, height, width);

                    Point tempPoint=Point.fromRT(rho, theta);

                    double adj=tempPoint.x>0 ? -.02 : .02;

                    tempPoint=Point.fromXY(tempPoint.x+adj, tempPoint.y);

                    int fillNumber2=getFill(tempPoint.rho, tempPoint.theta, pixels, height, width);

                    if (fillNumber==0 && fillNumber2==1){
                        while (fillNumber2==1){
                            tempPoint=Point.fromXY(tempPoint.x+adj, tempPoint.y);

                            fillNumber2=getFill(tempPoint.rho, tempPoint.theta, pixels, height, width);
                        }

                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(tempPoint.rho, tempPoint.theta)));
                        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho, theta)));
                    }
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
                dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
            }
        }.trace();
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

        return pixels[(int)Math.round(height-height*(y/2+.5))][(int)Math.round(width*(x/2+.5))];
    }

    public static void main(String args[]) throws Exception {
        TestFiller me = new TestFiller();
    }
}