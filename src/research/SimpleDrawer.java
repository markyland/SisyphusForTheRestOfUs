package research;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class SimpleDrawer extends ATrack {

    private String file;

    public SimpleDrawer(String file) throws Exception {
        super("");

        this.file=file;

        trace();
    }

    protected void trace() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line= in.readLine();
        while (line!=null){
            String tokens[] = line.split(" ");

            double theta=Double.parseDouble(tokens[0]);
            double rho=Double.parseDouble(tokens[1]);

            dc.lineTo(dc.getCurrentRelativePosition().vectorTo(Point.fromRT(rho, theta)));

            line=in.readLine();
        }

        dc.renderPNG( "c:\\users\\mark\\desktop\\fill.png" );
        dc.write( "c:\\users\\mark\\desktop\\fill.thr" );

        Runtime.getRuntime().exec("cmd /C start c:\\users\\mark\\desktop\\fill.png");
    }

    public static void main(String args[]) throws Exception {
        SimpleDrawer me = new SimpleDrawer(args[0]);
    }
}