package research;

import com.slightlyloony.jsisyphus.ATrack;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class Spiral1 {

    public Spiral1() throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));

                double rho=0;
                double theta=0;//dc.getCurrentPosition().getTheta();

                while (rho<1){
                    theta+=1;

                    rho=theta / (2 * Math.PI) * (0.005 / .4);

                    com.slightlyloony.jsisyphus.Point dest = com.slightlyloony.jsisyphus.Point.fromRT(rho, theta);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( "c:\\users\\mark\\desktop\\maze.png" );
                dc.write( "c:\\users\\mark\\desktop\\maze.thr" );
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        Spiral1 me = new Spiral1();
    }
}
