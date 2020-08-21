package v1;

import javax.swing.JProgressBar;

public class SFTROUCLI {
    public static void main(String[] args) {
        try {
            GraphSolver.convert(args[0], args[1] + ".thr", args[1] + ".png", args[2].equals("true"), new JProgressBar(0, 100));
        } catch (Throwable exception) {
            System.out.println("Error: " + exception.getMessage());
            System.exit(-1);
        }
    }
}