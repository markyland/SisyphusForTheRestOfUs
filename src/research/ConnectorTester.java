package research;

import com.slightlyloony.jsisyphus.Point;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ConnectorTester {

    public static void main(String args[]) throws Exception {
        ArrayList<Point> background = loadPoints(args[0]);

        for (int inputFileIndex=1; inputFileIndex<args.length; inputFileIndex++) {
            ArrayList<Point> foreground = loadPoints(args[inputFileIndex]);

            int foregroundSize = foreground.size();

            Point maxPoint = null;
            int maxIndex = -1;

            for (int i = 0; i < foregroundSize; i++) {
                Point point = foreground.get(i);

                if (maxPoint == null || point.y < maxPoint.y || (point.y == maxPoint.y && point.x > maxPoint.x)) {
                    maxPoint = point;

                    maxIndex = i;
                }
            }

            //----------------------------------------------------------------------------------------------------------------------------

            int backgroundSize = background.size();

            for (int i = backgroundSize - 1; i >= 0; i--) {
                Point backgroundPoint = background.get(i);

                double hypot=Math.hypot(backgroundPoint.x - maxPoint.x, backgroundPoint.y - maxPoint.y);

                if (hypot < .03 ) {
                    ArrayList<Point> temp = new ArrayList<>();

                    double diff1 = (int) ((backgroundPoint.theta - maxPoint.theta) / (Math.PI * 2)) * (Math.PI * 2) - (2 * Math.PI);
                    double diff2 = (int) ((backgroundPoint.theta - maxPoint.theta) / (Math.PI * 2)) * (Math.PI * 2);
                    double diff3 = (int) ((backgroundPoint.theta - maxPoint.theta) / (Math.PI * 2)) * (Math.PI * 2) + (2 * Math.PI);

                    double diff;
                    if ((Math.abs(maxPoint.theta+diff1-backgroundPoint.theta) < Math.abs(maxPoint.theta+diff2-backgroundPoint.theta)) &&
                            (Math.abs(maxPoint.theta+diff1-backgroundPoint.theta) < Math.abs(maxPoint.theta+diff3-backgroundPoint.theta))){
                        diff=diff1;
                    }
                    else if (Math.abs(maxPoint.theta+diff2-backgroundPoint.theta) < Math.abs(maxPoint.theta+diff3-backgroundPoint.theta)){
                        diff=diff2;
                    }
                    else{
                        diff=diff3;
                    }

                    for (int j = maxIndex; j >= 0; j--) {
                        Point foregroundPoint = foreground.get(j);
                        foregroundPoint = Point.fromRT(foregroundPoint.rho, foregroundPoint.theta + diff);
                        temp.add(foregroundPoint);
                    }

                    for (int j = 0; j < foregroundSize; j++) {
                        Point foregroundPoint = foreground.get(j);
                        foregroundPoint = Point.fromRT(foregroundPoint.rho, foregroundPoint.theta + diff);
                        temp.add(foregroundPoint);
                    }

                    for (int j = foregroundSize - 1; j >= maxIndex; j--) {
                        Point foregroundPoint = foreground.get(j);
                        foregroundPoint = Point.fromRT(foregroundPoint.rho, foregroundPoint.theta + diff);
                        temp.add(foregroundPoint);
                    }

                    temp.add(background.get(i));

                    background.addAll(i + 1, temp);

                    break;
                }
            }
        }

        //----------------------------------------------------------------------------------------------------------------------------

        BufferedWriter out = new BufferedWriter(new FileWriter("c:/users/mark/desktop/output.thr"));

        for (Point point : background) {
            out.write(point.theta + " " + point.rho + "\n");
        }

        out.flush();
        out.close();
    }

    private static ArrayList<Point> loadPoints(String inputFile) throws Exception {
        ArrayList<Point> ret = new ArrayList<>();

        BufferedReader in = new BufferedReader(new FileReader(inputFile));

        String line=in.readLine();

        while (line!=null){
            String tokens[] = line.split(" ");

            double theta=Double.parseDouble(tokens[0]);
            double rho=Double.parseDouble(tokens[1]);

            Point point = Point.fromRT(rho, theta);

            ret.add(point);

            line=in.readLine();
        }

        in.close();

        return ret;
    }
}
