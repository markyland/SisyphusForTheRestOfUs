import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class SisyphusForTheRestOfUs {

    private JTextField vectorFileInputTF = new JTextField(40);
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private JFrame frame;

    public SisyphusForTheRestOfUs(){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel(new ImageIcon(getClass().getResource("header.png"))), BorderLayout.NORTH);
        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        frame.setVisible(true);
    }

    private JPanel createMainPanel(){
        JButton vectorFileInputButton = new JButton(new AbstractAction("..."){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();

                int returnVal=fc.showOpenDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    vectorFileInputTF.setText(fc.getSelectedFile().getPath());
                }
            }
        });

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.add(new JLabel("Ascii Vector File : "), BorderLayout.WEST);
        p.add(vectorFileInputTF, BorderLayout.CENTER);
        p.add(vectorFileInputButton, BorderLayout.EAST);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

        ret.add(p);

        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        ret.add(progressBar);

        return ret;
    }

    private JPanel createButtonPanel(){
        final JButton convertButton = new JButton("Convert!");
        convertButton.setFocusable(false);

        convertButton.addActionListener(new AbstractAction("Convert!"){
            public void actionPerformed(final ActionEvent e) {
                try{
                    final File inputFile = new File(vectorFileInputTF.getText());

                    if (!inputFile.exists()){
                        throw new Exception("Input file does not exist!");
                    }

                    final String outputTrack;
                    final String outputPng;
                    String inputFileName = inputFile.getName();
                    if (inputFileName.endsWith(".csv") || inputFileName.endsWith(".asc")){
                        outputTrack=new File(inputFile.getParent(), inputFileName.substring(0, inputFileName.length()-4)+".thr").getPath();
                        outputPng=new File(inputFile.getParent(), inputFileName.substring(0, inputFileName.length()-4)+".png").getPath();
                    }
                    else{
                        outputTrack=new File(inputFile.getParent(), inputFileName+".thr").getPath();
                        outputPng=new File(inputFile.getParent(), inputFileName+".png").getPath();
                    }

                    convertButton.setEnabled(false);
                    progressBar.setVisible(true);
                    frame.pack();

                    new SwingWorker<Void, Void>(){
                        @Override
                        protected Void doInBackground() throws Exception {
                            GraphSolver.convert(inputFile.getPath(), outputTrack, outputPng, progressBar);

                            return null;
                        }

                        @Override
                        protected void done() {
                            convertButton.setEnabled(true);
                            progressBar.setVisible(false);

                            JOptionPane.showMessageDialog(frame, "File Converted!");
                        }
                    }.execute();
                }
                catch (Exception exception){
                    exception.printStackTrace();

                    JOptionPane.showMessageDialog(frame, exception.getMessage());
                }
            }
        });

        JButton exitButton = new JButton(new AbstractAction("Exit"){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        exitButton.setFocusable(false);

        JPanel p = new JPanel();
        p.add(convertButton);
        p.add(exitButton);

        return p;
    }

    public static void main(String args[]){
        SisyphusForTheRestOfUs me = new SisyphusForTheRestOfUs();
    }
}
