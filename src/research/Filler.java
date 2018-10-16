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
public class Filler {

    public Filler() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\pumpkinfill.png");

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

                        pixels[y][x]=(r+g+b)/3.0>128 ? 0 : 1;
                    }
                }


//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double eraseSpace=0.0125;

                double rho=0;
                double theta=0;

                while (true){
                    theta+=.01;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    //System.err.println(Math.sin(theta));

                    //wiggle=1, freq=100, eraseSpace=0.0125  //nice medium color

                    double wiggleAmount=3*(1-Math.abs(2*Math.pow(rho, 1.1)-1));
                    double frequency=20;

                    double rho1=wiggleAmount * Math.sin(theta*frequency+theta/7.5) * eraseSpace;    //background
                    double rho2=wiggleAmount * Math.sin(theta*frequency/2) * eraseSpace;            //foreground

                    //--------------------------------------------------------------------------------------------------

                    Point point1 = Point.fromRT(rho+rho1, theta);

                    double x1=point1.x;
                    double y1=point1.y;

                    boolean isActive1=pixels[(int)Math.round(height-height*(y1/2+.5))][(int)Math.round(width*(x1/2+.5))]!=0;

                    //--------------------------------------------------------------------------------------------------

                    Point point2 = Point.fromRT(rho+rho2, theta);

                    double x2=point2.x;
                    double y2=point2.y;

                    boolean isActive2=pixels[(int)Math.round(height-height*(y2/2+.5))][(int)Math.round(width*(x2/2+.5))]!=0;

                    //--------------------------------------------------------------------------------------------------

                    double additionalRho;

                    if (isActive2){
                        additionalRho=rho2;
                    }
                    else if (isActive1){
                        additionalRho=rho2;
                    }
                    else{
                        additionalRho=rho1;
                    }

                    if (rho+additionalRho>1){
                        break;
                    }

                    Point dest = Point.fromRT(rho+additionalRho, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\waky.png" );
                dc.write( "c:\\users\\mark\\desktop\\waky.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\waky.png");
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        Filler me = new Filler();
    }
}