package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class GCodeConverter {

    public static void gCodeConvert(final File inputFile, final File outputFileTHR, final File outputFilePNG) throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                BufferedReader in = new BufferedReader(new FileReader(inputFile));

                //G01 X255.000 Y250.000

                ArrayList<Point> points = new ArrayList<>();

                String line = in.readLine();

                while (line != null) {
                    if (line.trim().length()==0){  //weird that they have blank lines
                        line=in.readLine();
                        continue;
                    }

                    String tokens[] = line.split(" ");

                    Point point = Point.fromXY(Double.parseDouble(tokens[1].substring(1)), Double.parseDouble(tokens[2].substring(1)));

                    points.add(point);

                    line = in.readLine();
                }

                //------------------------------------------------------------------------------------------------------------------------------------------------------------------

                //starting point 0,0

                Point root=points.get(0);

                int sz = points.size();
                for (int i = 0; i < sz; i++) {
                    points.set(i, Point.fromXY(points.get(i).x - root.x, points.get(i).y - root.y));
                }

                //------------------------------------------------------------------------------------------------------------------------------------------------------------------

                //rho scale

                double maxRho = 0;

                for (Point point : points) {
                    maxRho = Math.max(maxRho, point.rho);
                }

                for (int i = 0; i < sz; i++) {
                    points.set(i, Point.fromRT(points.get(i).rho / maxRho, points.get(i).theta));
                }

                //------------------------------------------------------------------------------------------------------------------------------------------------------------------

                //draw!

                for (Point point : points) {
                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(point));
                }

                dc.renderPNG(outputFilePNG.getPath());
                dc.write(outputFileTHR.getPath());
            }
        }.trace();
    }

    public static void main(String args[]) throws Exception {
        GCodeConverter me = new GCodeConverter();
    }
}