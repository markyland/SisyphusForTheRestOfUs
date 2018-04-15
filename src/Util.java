import com.slightlyloony.jsisyphus.DrawingContext;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/15/2018
 * Time: 6:18 PM
 */
public class Util {

    private static double PI2=Math.PI*2;

    public static void eraseToManually(DrawingContext dc, com.slightlyloony.jsisyphus.Point point, boolean isOut){
        double eraseSpace=dc.getEraseSpacing();

        double rho=dc.getCurrentPosition().getRho();
        double theta=dc.getCurrentPosition().getTheta();

           if (rho==0){
               rho=0.005;      //don't want divide by 0
           }

           double turnAmount=.1;

           while ((isOut ? (rho<=point.rho && rho<=1) : (rho>=point.rho && rho>=0))) {
               theta = theta + (isOut ? 1 : -1) * turnAmount;

               rho = theta / (2 * Math.PI) * eraseSpace;

               dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(rho, theta)));
           }

           double thetaLeft=theta%PI2 > point.theta ? theta%PI2-point.theta : theta%PI2-point.theta+PI2;
           thetaLeft=(int)(thetaLeft/turnAmount)*turnAmount;

           while (thetaLeft>0) {
               theta = theta + (isOut ? 1 : -1) * turnAmount;

               rho = theta / (2 * Math.PI) * eraseSpace;

               dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(rho, theta)));

               thetaLeft=theta%PI2 > point.theta ? theta%PI2-point.theta : theta%PI2-point.theta+PI2;
               thetaLeft=(int)(thetaLeft/turnAmount)*.1;
           }

           theta=(int)(theta/PI2)*PI2  + point.theta;

           rho = theta / (2 * Math.PI) * eraseSpace;

           dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(rho, theta)));
       }
}
