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
public class PhotoPrinter {

    public PhotoPrinter() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\marilyn.png");

                Image img = icon.getImage();

                int width = img.getWidth(null);
                int height = img.getHeight(null);
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
                int pixels[][] = new int[height][width];
                for (int y=0; y<height; y++){
                    for (int x=0; x<width; x++){
                        int val=pixels1D[y*width+x];

                        int r=val >> 16 & 0xFF;
                        int g=val >> 8 & 0xFF;
                        int b=val & 0xFF;

                        pixels[y][x]=(r+g+b)/3;
                    }
                }

                //------------------------------------------------------------------------------------------------------------------------------------------------------------------


//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double eraseSpace=0.008;

                double rho=0;
                double theta=0;//dc.getCurrentPosition().getTheta();

                while (rho+eraseSpace<1){
                    theta+=.01;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    Point point = Point.fromRT(rho, theta);

                    double x=point.x;
                    double y=point.y;

                    //System.err.println(Math.sin(theta));

                    //wiggle=1, freq=100, eraseSpace=0.0125  //nice medium color

                    double wiggleAmount=(1-(pixels[(int)Math.round(500-500*(y/2+.5))][(int)Math.round(500*(x/2+.5))] / 255.0))*4;
                    double frequency=100;

                    double additionalRho=wiggleAmount * Math.sin(theta*frequency*rho) * eraseSpace;

                    Point dest = Point.fromRT(rho+additionalRho, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\photo.png" );
                dc.write( "c:\\users\\mark\\desktop\\photo.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\photo.png");
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        PhotoPrinter me = new PhotoPrinter();
    }
}