package research;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Connector {


    public static void main(String args[]) throws Exception {
        BufferedReader in1 = new BufferedReader(new FileReader(args[0]));
        BufferedReader in2 = new BufferedReader(new FileReader(args[1]));

        BufferedWriter out = new BufferedWriter(new FileWriter("c:/users/mark/desktop/output.thr"));

        double theta1=0;

        String line= in1.readLine();
        while (line!=null){
            out.write(line + "\n");

            theta1=Double.parseDouble(line.split(" ")[0]);

            line=in1.readLine();
        }

        line= in2.readLine();

        double theta2=Double.parseDouble(line.split(" ")[0]);

        double diff = (int)((theta1-theta2)/(Math.PI*2))*(Math.PI*2);

        while (line!=null){
            double theta=Double.parseDouble(line.split(" ")[0]);

            out.write((theta+diff) + " " + line.split(" ")[1] + "\n");

            line=in2.readLine();
        }

        out.flush();
        out.close();
    }
}
