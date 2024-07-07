package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.DrawingContext;
import com.slightlyloony.jsisyphus.Point;
import util.SisyphusUtil;

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
public class TurkeyFill {

    double eraseSpace=0.0125;

    public TurkeyFill() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\circlefun-table.png");

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
                        else if (r==200 && g==200 && b==200){
                            pixels[y][x]=6;
                        }
                        else if (r==150 && g==150 && b==150){
                            pixels[y][x]=7;
                        }
                        else if (r==50 && g==50 && b==50){
                            pixels[y][x]=8;
                        }
                        else if (r==250 && g==250 && b==250){
                            pixels[y][x]=9;
                        }
                    }
                }


//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double rho=0;
                double theta=0;

                boolean eyeDrawn=false;

                while (true){
                    theta+=.01;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    //System.err.println(Math.sin(theta));

                    //wiggle=1, freq=100, eraseSpace=0.0125  //nice medium color

                    double wiggleAmount=3*(1-Math.abs(2*Math.pow(rho, 1.1)-1));
                    double frequency=20;

                    double rhoRed=wiggleAmount/2 * Math.sin(theta*frequency*2.1) * eraseSpace;    //background
                    double rhoGreen=wiggleAmount/1.5 * Math.sin(theta*frequency+theta/20) * eraseSpace;            //foreground
                    double rhoBlue=wiggleAmount/2 * Math.sin(theta*frequency*1.5) * eraseSpace;            //foreground
                    double rhoWhite=0;   //wiggleAmount * Math.sin(theta*rho*frequency*2) * eraseSpace;
                    double rhoBlack=wiggleAmount/1.2 * Math.pow(Math.sin(theta*frequency*2.3),2) * eraseSpace;//wiggleAmount/1.5 * Math.sin(theta*frequency*2.3) * eraseSpace;
                    double rhoGrey100=wiggleAmount * Math.sin(theta*frequency+theta/3) * eraseSpace;;
                    double rhoGrey200=wiggleAmount*2 * Math.sin(theta*frequency % 5) * eraseSpace;;
                    double rhoGrey150=wiggleAmount/2 * Math.sin(theta*frequency/1.5) * eraseSpace;    //background
                    double rhoGrey50=wiggleAmount/2 * (Math.sin(theta*frequency)>0 ? 1 : -1) * eraseSpace;    //background
                    double rhoGrey250=wiggleAmount/1.5 * Math.sin(Math.pow(theta, 1.005)*frequency) * eraseSpace;    //background

//                    double rhoBlue=0;            //foreground

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
                    else if (fillNumber==6){
                        additionalRho=rhoGrey200;
                    }
                    else if (fillNumber==7){
                        additionalRho=rhoGrey150;
                    }
                    else if (fillNumber==8){
                        additionalRho=rhoGrey50;
                    }
                    else if (fillNumber==9){
                        additionalRho=rhoGrey250;
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

//                    if (theta>350.35+Math.PI*10 && !eyeDrawn){
//                        eraseAndCircle(dc, .015);
//                        eraseAndCircle(dc, .015);
//
//                        eyeDrawn=true;
//                    }
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

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(curRho-eraseSpace*2, curTheta)));
        dc.arcAroundRT(size, curTheta+Math.PI, Math.PI*2);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(curRho, curTheta)));
    }

    private int getFill(double rho, double theta, int[][] pixels, int height, int width){
        Point point = Point.fromRT(rho, theta);

        double x=point.x;
        double y=point.y;

        return pixels[(int)Math.round(height-height*(y/2+.5))][(int)Math.round(width*(x/2+.5))];
    }

    public static void main(String args[]) throws Exception {
        TurkeyFill me = new TurkeyFill();
    }
}