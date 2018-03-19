import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class MazeMaker {

    public MazeMaker() throws Exception {
        double space=30/360.0*2*Math.PI;
        int slices=7;

        BufferedWriter out = new BufferedWriter(new FileWriter("c:\\users\\mark\\desktop\\maze.thr", true));

        double rho=0;
        double theta=0;

        for (int i=0; i<slices; i++){
            rho+=(1.0/slices);

            out.write(theta + " " + rho + "\n");

            double curSpace=space*(1-rho);

            theta+=(2*Math.PI-curSpace);

            out.write(theta + " " + rho + "\n");

            theta-=(int)(Math.random()*(2*Math.PI-curSpace*3));

            out.write(theta + " " + rho + "\n");
        }

        out.flush();
        out.close();

//        new ATrack(""){
//            @Override
//            protected void trace() throws IOException {
//                double startTheta=-Math.PI;
//                double space=35/360.0*2*Math.PI;
//                int slices=5;
//
//                dc.setEraseSpacing(0.005);
//                dc.eraseTo(Point.fromRT(1, startTheta));
//
//                dc.renderPNG( "c:\\users\\mark\\desktop\\maze.png" );
//                dc.write( "c:\\users\\mark\\desktop\\maze.thr" );
//
//                //------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//
//                BufferedWriter out = new BufferedWriter(new FileWriter("c:\\users\\mark\\desktop\\maze.thr", true));
//
//                double rho=1;
//                double theta=dc.getCurrentPosition().getTheta();
//
//                for (int i=0; i<slices; i++){
//                    theta+=(2*Math.PI-space);
//
//                    out.write(theta + " " + rho + "\n");
//
//                    theta-=(int)(Math.random()*(2*Math.PI-space*3));
//
//                    out.write(theta + " " + rho + "\n");
//
//                    rho-=(1.0/slices);
//
//                    out.write(theta + " " + rho + "\n");
//                }
//
//                out.flush();
//                out.close();
//            }
//        }.trace();
    }

    public static void main(String args[]) throws Exception {
        MazeMaker me = new MazeMaker();
    }
}
