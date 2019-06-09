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
public class Filler2 {

    public Filler2() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\hotsun-table.png");

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

                        if (r==255 && g==0 && b==0){
                            pixels[y][x]=0;
                        }
                        else if (r==0 && g==255 && b==0){
                            pixels[y][x]=1;
                        }
                        else if (r==0 && g==0 && b==255){
                            pixels[y][x]=2;
                        }
                        else if (r==255 && g==255 && b==255){
                            pixels[y][x]=3;
                        }
                        else if (r==0 && g==0 && b==0){
                            pixels[y][x]=4;
                        }
                        else if (r==100 && g==100 && b==100){
                            pixels[y][x]=5;
                        }
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

                    double rhoRed=wiggleAmount/2 * Math.sin(theta*frequency*2.1) * eraseSpace;    //background
                    double rhoGreen=wiggleAmount * Math.sin(theta*frequency+theta/30) * eraseSpace;            //foreground
                    double rhoBlue=wiggleAmount/2 * Math.sin(theta*frequency*1.5) * eraseSpace;            //foreground
                    double rhoWhite=0;   //wiggleAmount * Math.sin(theta*rho*frequency*2) * eraseSpace;
                    double rhoBlack=wiggleAmount/1.5 * Math.sin(theta*frequency*2.3) * eraseSpace;
                    double rhoGrey100=wiggleAmount * Math.sin(theta*frequency+theta/3) * eraseSpace;;

//                    double rhoBlue=0;            //foreground
//
                    int fillNumber=getFill(rho, theta, pixels, height, width);

                    double additionalRho=0;

                    if (fillNumber==0){
                        additionalRho=rhoRed;
                    }
                    else if (fillNumber==1){
                        additionalRho=rhoGreen;
                    }
                    else if (fillNumber==2){
                        additionalRho=rhoBlue;
                    }
                    else if (fillNumber==3){
                        additionalRho=rhoWhite;
                    }
                    else if (fillNumber==4){
                        additionalRho=rhoBlack;
                    }
                    else if (fillNumber==5){
                        additionalRho=rhoGrey100;
                    }

                    int fillNumber2=getFill(rho+additionalRho, theta, pixels, height, width);

                    if (fillNumber!=fillNumber2){ //we've got into a another fill.  its a tricky situation but 0 should be pretty good
                        additionalRho=0;
                    }

                    if (rho+additionalRho>1){
                        break;
                    }

                    Point dest = Point.fromRT(rho+additionalRho, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
                dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
            }
        }.trace();
    }

    private int getFill(double rho, double theta, int[][] pixels, int height, int width){
        Point point = Point.fromRT(rho, theta);

        double x=point.x;
        double y=point.y;

        return pixels[(int)Math.round(height-height*(y/2+.5))][(int)Math.round(width*(x/2+.5))];
    }

    public static void main(String args[]) throws Exception {
        Filler2 me = new Filler2();
    }
}