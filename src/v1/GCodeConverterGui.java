package v1;

import research.GCodeConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class GCodeConverterGui {

    private JTextField vectorFileInputTF = new JTextField(40);
    private JFrame frame;

    public GCodeConverterGui(){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel(new ImageIcon(getClass().getResource("/gcode.png"))), BorderLayout.NORTH);
        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        frame = new JFrame("GCode2Sisyphus");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(getClass().getResource("/gcode.png")).getImage());
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

        vectorFileInputButton.setFocusable(false);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.add(new JLabel("GCode File : "), BorderLayout.WEST);
        p.add(vectorFileInputTF, BorderLayout.CENTER);
        p.add(vectorFileInputButton, BorderLayout.EAST);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

        ret.add(p);

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

                    final File outputTrack;
                    final File outputPng;
                    String inputFileName = inputFile.getName();
                    if (inputFileName.endsWith(".gcode")){
                        outputTrack=new File(inputFile.getParent(), inputFileName.substring(0, inputFileName.length()-6)+".thr");
                        outputPng=new File(inputFile.getParent(), inputFileName.substring(0, inputFileName.length()-6)+"-table.png");
                    }
                    else{
                        outputTrack=new File(inputFile.getParent(), inputFileName+".thr");
                        outputPng=new File(inputFile.getParent(), inputFileName+"-table.png");
                    }

                    if (outputTrack.exists() || outputPng.exists()){
                        if (JOptionPane.showConfirmDialog(frame, "Replace existing output files?", "",  JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
                            return;
                        }
                    }

                    GCodeConverter.gCodeConvert(inputFile, outputTrack, outputPng);

                    JOptionPane.showMessageDialog(frame, "File Converted!");

                }
                catch (Throwable exception){
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
        p.add(new JLabel("   "));
        p.add(convertButton);
        p.add(exitButton);

        return p;
    }

    public static void main(String args[]){
        GCodeConverterGui me = new GCodeConverterGui();
    }
}
