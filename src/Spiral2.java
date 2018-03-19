import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class Spiral2 {

    public Spiral2() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double eraseSpace=0.0125;

                double rho=0;
                double theta=0;//dc.getCurrentPosition().getTheta();

                while (rho+eraseSpace<1){
                    theta+=.01;

                    rho=theta / (2 * Math.PI) * (eraseSpace);

                    Point point = Point.fromRT(rho, theta);

                    double x=point.x;
                    double y=point.y;

                    //System.err.println(Math.sin(theta));

                    double wiggleAmount=.75;
                    double frequency=100;

//                    wiggleAmount=.5;
//                    frequency=50;

                    double optionAllRho=(Math.sin(theta*rho*frequency)/2*wiggleAmount+wiggleAmount/2) * eraseSpace;

                    double additionalRho=(x>-.5 && x<.5 && y>-.5 && y<.5) ? optionAllRho : 0;

                    System.err.println(additionalRho);
                  //  double additionalRho=Math.sin(theta*5) * eraseSpace;

                    Point dest = Point.fromRT(rho+additionalRho, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\waky.png" );
                dc.write( "c:\\users\\mark\\desktop\\waky.thr" );
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        Spiral2 me = new Spiral2();
    }
}
