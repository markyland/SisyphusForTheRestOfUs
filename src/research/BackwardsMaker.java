package research;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class BackwardsMaker {


    public static void main(String args[]) throws Exception {
        BufferedReader in1 = new BufferedReader(new FileReader(args[0]));

        ArrayList<String> lines = new ArrayList<>();

        String line= in1.readLine();
        while (line!=null){
            lines.add(line);

            line=in1.readLine();
        }

        BufferedWriter out = new BufferedWriter(new FileWriter("c:/users/mark/desktop/output.thr"));

        int sz=lines.size();

        for (int i=sz-1; i>=0; i--) {
            out.write(lines.get(i) + "\n");
        }

        out.flush();
        out.close();
    }
}
