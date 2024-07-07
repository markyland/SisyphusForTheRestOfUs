package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Delta;
import com.slightlyloony.jsisyphus.Point;
import com.slightlyloony.jsisyphus.lines.Line;
import com.slightlyloony.jsisyphus.lines.StraightLine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class ShapeSpiral2 extends ATrack {

    private static double maxPointDistance = 0.01;  // approximately 2mm on A16 table...

    ArrayList<Point> points = new ArrayList<>();

    public ShapeSpiral2(String filename) throws Exception {
        super("");

        BufferedReader in1 = new BufferedReader(new FileReader(filename));

        String line= in1.readLine();
        while (line!=null){
            double theta=Double.parseDouble(line.split(" ")[0]);
            double rho=Double.parseDouble(line.split(" ")[1]);

            points.add(Point.fromRT(rho, theta));

            line=in1.readLine();
        }


        trace();
    }

    protected void trace() throws IOException {
        for (double pert=0; pert<=.5; pert+=.02) {
            for (Point point : points) {
                double pert2=pert*2;
                go(Point.fromRT(point.rho*pert*pert2+pert*(1-pert2), point.theta));
                //go(Point.fromRT(point.rho*pert*(1-pert)+, point.theta));
            }

          //  go(points[0]);
        }

        for (double pert=.5; pert<=1; pert+=.02) {
            for (Point point : points) {
                double pert2=(pert-.5)*2;
                go(Point.fromRT(point.rho*pert*(1-pert2)+pert*pert2, point.theta));
            }

            //  go(points[0]);
        }

        dc.renderPNG("c:\\users\\mark\\desktop\\shapespiral.png");
        dc.write("c:\\users\\mark\\desktop\\shapespiral.thr");

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\shapespiral.png");
    }

    private void go(Point point){
        dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
    }

    public static void main(String args[]) throws Exception {
        ShapeSpiral2 me = new ShapeSpiral2(args[0]);
    }
}