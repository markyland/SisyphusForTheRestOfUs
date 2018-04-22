package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class WakySpiral {

    public WakySpiral() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\question.png");


//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double eraseSpace=0.0125;

                double rho=0;
                double theta=0;//dc.getCurrentPosition().getTheta();

                while (true){
                    theta+=.01;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    Point point = Point.fromRT(rho, theta);

                    double x=point.x;
                    double y=point.y;

                    //System.err.println(Math.sin(theta));

                    //wiggle=1, freq=100, eraseSpace=0.0125  //nice medium color

                    double wiggleAmount=3;
                    double frequency=10;

                    //double optionAllRho=wiggleAmount * Math.sin(theta*frequency*rho) * eraseSpace;
                    double optionAllRho=wiggleAmount * Math.sin(theta*frequency) * eraseSpace;
                    //double optionAllRho=2.5 * eraseSpace;

                    boolean isActive=true;//x>-.5 && x<.5 && y>-.5 && y<.5;
                    //boolean isActive=pixels[(int)Math.round(500-500*(y/2+.5))][(int)Math.round(500*(x/2+.5))]==1;

                    double additionalRho=isActive ? optionAllRho : 0;

                    System.err.println(additionalRho);
                  //  double additionalRho=Math.sin(theta*5) * eraseSpace;

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
        WakySpiral me = new WakySpiral();
    }
}