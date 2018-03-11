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

    public SisyphusForTheRestOfUs(){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel(new ImageIcon(getClass().getResource("header.png"))), BorderLayout.NORTH);
        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMainPanel(){
        final JTextField vectorFileInputTF = new JTextField(40);

        JButton vectorFileInputButton = new JButton(new AbstractAction("..."){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();

                int returnVal=fc.showOpenDialog(SwingUtilities.getWindowAncestor(vectorFileInputTF));

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

        return p;
    }

    private JPanel createButtonPanel(){
        JButton convertButton = new JButton("Convert!");

        JButton exitButton = new JButton(new AbstractAction("Exit"){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel p = new JPanel();
        p.add(convertButton);
        p.add(exitButton);

        return p;
    }

    public static void main(String args[]){
        SisyphusForTheRestOfUs me = new SisyphusForTheRestOfUs();
    }
}
