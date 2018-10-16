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
public class BackgroundMaker {

    private static final double eraseSpace=0.0125;

    private static final double PI2=2 * Math.PI;

    public BackgroundMaker() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                int pixels[][] = getPixels();


                double rho=0;
                double theta=0;

                while (true){
                    theta+=.01;

                    rho=theta / PI2 * (eraseSpace);

                    if (rho>1){
                        break;
                    }

                    Point point = Point.fromRT(rho, theta);

                    double x=point.x;
                    double y=point.y;

                    int imgX=(int)Math.round((pixels.length-1)*(x/2+.5));
                    int imgY=(int)Math.round((pixels[0].length-1)*(1-(y/2+.5)));

                    int color=pixels[imgX][imgY];

                    if (color==255){
                       rho=getSawtoothBackground(theta, 24);
                    }
                    else{
                        rho=getSinBackground(theta, 3, 20, .1);
                       // rho=getPlainBackground(theta);
                    }

                    if (rho>1){
                        break;
                    }

                    Point dest = Point.fromRT(rho, theta);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\waky.png" );
                dc.write( "c:\\users\\mark\\desktop\\waky.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\waky.png");
            }
        }.trace();
    }

    //thickness 1-4
    private double getPlainBackground(double theta){
       return theta / PI2 * (eraseSpace);
    }

    //thickness 1-4
    private double getSawtoothBackground(double theta, double numTeeth){
        if (((int)(theta/(PI2/numTeeth)))%2==0) {
            return Math.floor(theta / PI2) * eraseSpace;
        }
        else{
            return Math.ceil(theta / PI2) * eraseSpace;
        }
    }

    //twist=0 to 1
    private double getSinBackground(double theta, double wiggleAmount, double frequency, double twist){
        double rho = getPlainBackground(theta);

        double wiggleAmountFlattened=wiggleAmount*(1-Math.abs(2*Math.pow(rho, 1.1)-1));

        double addedRho=wiggleAmountFlattened * Math.sin(theta*frequency+twist*theta) * eraseSpace;

        return rho+addedRho;
    }

    private int[][] getPixels() {
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\picture.png");

        Image img = icon.getImage();

        int width = img.getWidth(null);
        int height = img.getHeight(null);
        int[] pixels1D = new int[width * height];

        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels1D, 0, width);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Error: Interrupted Waiting for Pixels");
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            throw new IllegalStateException("Error: Image Fetch Aborted");
        }

        //switch to 2 dim array
        int pixels[][] = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = pixels1D[y * width + x];

                int r = val >> 16 & 0xFF;
                int g = val >> 8 & 0xFF;
                int b = val & 0xFF;

                pixels[x][y] = (r + g + b) / 3;
            }
        }

        return pixels;
    }

    public static void main(String args[]) throws Exception {
        BackgroundMaker me = new BackgroundMaker();
    }
}