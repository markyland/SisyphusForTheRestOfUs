package research; /**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/24/18
 * Time: 8:10 PM
 */

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.DrawingContext;
import util.Util;

import java.io.IOException;

@SuppressWarnings("ConstantConditions")
public class TestErase {

    private static double PI2=Math.PI*2;

    public static void main(String args[]) throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                dc.setEraseSpacing(0.0125);

                Util.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(1, 0), true);

                eraseAndCircle(dc, .65, 330, .15);
                eraseAndCircle(dc, .65, 300, .15);
                eraseAndCircle(dc, .65, 270, .15);
                eraseAndCircle(dc, .65, 240, .15);
                eraseAndCircle(dc, .65, 210, .15);
                eraseAndCircle(dc, .65, 180, .15);
                eraseAndCircle(dc, .65, 150, .15);
                eraseAndCircle(dc, .65, 120, .15);
                eraseAndCircle(dc, .65, 90, .15);
                eraseAndCircle(dc, .65, 60, .15);
                eraseAndCircle(dc, .65, 30, .15);
                eraseAndCircle(dc, .65, 0, .15);
//
                eraseAndCircle(dc, .35, 330, .1);
                eraseAndCircle(dc, .35, 300, .1);
                eraseAndCircle(dc, .35, 270, .1);
                eraseAndCircle(dc, .35, 240, .1);
                eraseAndCircle(dc, .35, 210, .1);
                eraseAndCircle(dc, .35, 180, .1);
                eraseAndCircle(dc, .35, 150, .1);
                eraseAndCircle(dc, .35, 120, .1);
                eraseAndCircle(dc, .35, 90, .1);
                eraseAndCircle(dc, .35, 60, .1);
                eraseAndCircle(dc, .35, 30, .1);
                eraseAndCircle(dc, .35, 0, .1);

                eraseAndCircle(dc, .15, 330, .05);
                eraseAndCircle(dc, .15, 300, .05);
                eraseAndCircle(dc, .15, 270, .05);
                eraseAndCircle(dc, .15, 240, .05);
                eraseAndCircle(dc, .15, 210, .05);
                eraseAndCircle(dc, .15, 180, .05);
                eraseAndCircle(dc, .15, 150, .05);
                eraseAndCircle(dc, .15, 120, .05);
                eraseAndCircle(dc, .15, 90, .05);
                eraseAndCircle(dc, .15, 60, .05);
                eraseAndCircle(dc, .15, 30, .05);
                eraseAndCircle(dc, .15, 0, .05);

//                eraseAndCircle(dc, .4, 0, .1);
//                eraseAndCircle(dc, .3, 0, .2);
//                eraseAndCircle(dc, .2, 0, .3);

//                eraseAndCircle(dc, .4, 0, .1);
//                eraseAndCircle(dc, .39, 90, .1);
//                eraseAndCircle(dc, .3, 0, .2);
//                eraseAndCircle(dc, .29, 270, .2);
//                eraseAndCircle(dc, .2, 0, .3);
//                eraseAndCircle(dc, .19, 180, .3);

                Util.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(.005, 0), false);

//                com.slightlyloony.jsisyphus.Point dest = com.slightlyloony.jsisyphus.Point.fromXY(0, 0);
//                dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));

                dc.write( "c:/users/mark/desktop/test.thr" );
                dc.renderPNG("c:/users/mark/desktop/test.png");

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\test.png");
            }
        }.trace();
    }

    private static void eraseAndCircle(DrawingContext dc, double rho, double degrees, double size){
        Util.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(rho, Math.toRadians(degrees)), false);

        double eraseSpace=dc.getEraseSpacing();

        double curRho=dc.getCurrentPosition().getRho();
        double curTheta=dc.getCurrentPosition().getTheta();

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(curRho+eraseSpace*1.5, curTheta)));
        dc.arcAroundRT(size, Math.toRadians(degrees), PI2);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(curRho, curTheta)));
    }
}



