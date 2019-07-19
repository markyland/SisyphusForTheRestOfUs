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
public class WakySpiral9 {

    private static double eraseSpace=0.006;
    private boolean backup=false;

    public WakySpiral9() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\milesduck.png");

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

                        pixels[y][x]=(r+g+b)/3.0>128 ? 0 : 1;
                    }
                }


//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double theta=40;

                while (true){
                    theta+=.01;

                    if (drawNextStep(dc, theta, pixels)){
                        break;
                    }

                    if (backup){
                        for  (int i=1; i<=16; i++) {
                            drawNextStep(dc, theta + .01 * i, pixels);
                        }

                        for  (int i=16; i>=1; i--) {
                            drawNextStep(dc, theta + .01 * i, pixels);
                        }

                        drawNextStep(dc, theta, pixels);

                        //experiemental
                        double rho=theta / (2 * Math.PI) * (eraseSpace);

                        Point point = Point.fromRT(rho, theta);
                        if (point.x<0){

                            dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(rho-eraseSpace*1.5, theta)));
                            dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(rho, theta)));
                        }
                    }
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\waky.png" );
                dc.write( "c:\\users\\mark\\desktop\\waky.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\waky.png");
            }
        }.trace();
    }

    //boolean isDone
    private boolean drawNextStep(DrawingContext dc, double theta, int pixels[][]){

        double rho=theta / (2 * Math.PI) * (eraseSpace);

        Point point = Point.fromRT(rho, theta);

        double x=point.x;
        double y=point.y;

        int imgX=(int)Math.round(500-500*(y/2+.5));
        int imgY=(int)Math.round(500*(x/2+.5));
        boolean isActive=imgX<500 && imgY<500 && pixels[imgX][imgY]==0;

        //---------------------------------------------------------------------------------------------------

        double previousRho=(theta-.01) / (2 * Math.PI) * (eraseSpace);

        Point previousPoint = Point.fromRT(previousRho, theta-.01);

        double previousX=previousPoint.x;
        double previousY=previousPoint.y;

        int previousImgX=(int)Math.round(500-500*(previousY/2+.5));
        int previousImgY=(int)Math.round(500*(previousX/2+.5));

        boolean previousIsActive=previousImgX<500 && previousImgY<500 && pixels[previousImgX][previousImgY]==0;

        backup=false;
        if (!isActive && previousIsActive){
            backup=true;
        }

        //---------------------------------------------------------------------------------------------------

        if (isActive){
            if (((int)(theta/(2*Math.PI/24)))%2==0) {
                rho = Math.floor(theta / 4 / (2 * Math.PI)) * (eraseSpace * 4);
            }
            else{
                rho = Math.ceil(theta / 4 / (2 * Math.PI)) * (eraseSpace * 4);
            }
        }

        if (rho>1){
            return true;
        }

        Point dest = Point.fromRT(rho, theta);

        //System.err.println(point.x+","+point.y);

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));

        return false;
    }

    public static void main(String args[]) throws Exception {
        WakySpiral9 me = new WakySpiral9();
    }
}