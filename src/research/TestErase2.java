package research; /**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/24/18
 * Time: 8:10 PM
 */

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.DrawingContext;
import util.SisyphusUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("ConstantConditions")
public class TestErase2 {

    private static double PI2=Math.PI*2;

    public static void main(String args[]) throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                dc.setEraseSpacing(0.0125);

                SisyphusUtil.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(1, 0), true);

                ArrayList<Circle> circles = new ArrayList<>();

                circles.add(new Circle(.65, 330, .14));
                circles.add(new Circle(.60, 300, .14));
                circles.add(new Circle(.65, 270, .14));
                circles.add(new Circle(.60, 240, .14));
                circles.add(new Circle(.65, 210, .14));
                circles.add(new Circle(.60, 180, .14));
                circles.add(new Circle(.65, 150, .14));
                circles.add(new Circle(.60, 120, .14));
                circles.add(new Circle(.65, 90, .14));
                circles.add(new Circle(.60, 60, .14));
                circles.add(new Circle(.65, 30, .14));
                circles.add(new Circle(.60, 0, .14));

                circles.add(new Circle(.33, 345, .13));
                circles.add(new Circle(.30, 285, .13));
                circles.add(new Circle(.33, 225, .13));
                circles.add(new Circle(.30, 165, .13));
                circles.add(new Circle(.33, 105, .13));
                circles.add(new Circle(.30, 45, .13));

                circles.add(new Circle(.05, 315, .12));
                circles.add(new Circle(.05, 195, .12));
                circles.add(new Circle(.05, 75, .12));
                Collections.shuffle(circles);   //erase me. just for testingg

                Collections.sort(circles, new Comparator<Circle>() {
                    @Override
                    public int compare(Circle o1, Circle o2) {
                        int ret = Double.compare(o1.rho, o2.rho);

                        if (ret!=0){
                            return -ret;
                        }

                        ret = Double.compare(o1.theta, o2.theta);

                        if (ret!=0){
                            return -ret;
                        }

                        return 0;
                    }
                });

                for (Circle circle : circles){
                    eraseAndCircle(dc, circle.rho, circle.theta, circle.size);
                }

                SisyphusUtil.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(.005, 0), false);

//                com.slightlyloony.jsisyphus.Point dest = com.slightlyloony.jsisyphus.Point.fromXY(0, 0);
//                dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));

                dc.write( "c:/users/mark/desktop/test.thr" );
                dc.renderPNG("c:/users/mark/desktop/test.png");

                Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\test.png");
            }
        }.trace();
    }

    private static class Circle{
        public double rho;
        public double theta;
        public double size;

        public Circle(double rho, double theta, double size) {
            this.rho = rho;
            this.theta = theta;
            this.size = size;
        }
    }

    private static void eraseAndCircle(DrawingContext dc, double rho, double degrees, double size){
        SisyphusUtil.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(rho, Math.toRadians(degrees)), false);

        double eraseSpace=dc.getEraseSpacing();

        double curRho=dc.getCurrentPosition().getRho();
        double curTheta=dc.getCurrentPosition().getTheta();

        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(curRho+eraseSpace*1.5, curTheta)));
        dc.arcAroundRT(size, Math.toRadians(degrees), PI2);
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(com.slightlyloony.jsisyphus.Point.fromRT(curRho, curTheta)));
    }
}



