package research;

import util.Path;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class VectorCreator {

    public void process(){
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\flower.png");

        Image img = icon.getImage();

        int width = img.getWidth(null);
        int height = img.getHeight(null);
        int[] pixels1D = new int[width * height];

        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels1D, 0, width);

        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("Error: Interrupted Waiting for Pixels");
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            throw new IllegalStateException("Error: Image Fetch Aborted");
        }

        //switch to 2 dim array
        int pixels[][] = new int[height][width];
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int val=pixels1D[y*width+x];

                int r=val >> 16 & 0xFF;
                int g=val >> 8 & 0xFF;
                int b=val & 0xFF;

                pixels[y][x]=(r+g+b)/3.0>128 ? 0 : 1;
            }
        }

        //thin!
        ZhangSuenThinner.doZhangSuenThinning(pixels, true);

        //print it...

        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y=0; y<height; y++){
                for (int x=0; x<width; x++){
                    image.setRGB(x, y, pixels[y][x]==0 ? Color.white.getRGB() : Color.black.getRGB());
                }
            }

            ImageIO.write(image, "PNG", new File("C:\\Users\\mark\\Desktop\\flower-thin.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++) {
                System.err.print(pixels[i][j]==0 ? "." : "O");
            }
            System.err.println("");
        }

        System.err.println("");

        //find first point

        int xStart=-1;
        int yStart=-1;
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                if (pixels[y][x]==1){
                    xStart=x;
                    yStart=y;
                }
            }
        }

        if (xStart==-1 || yStart==-1){
            throw new IllegalStateException("Error: No starting point");
        }

        ArrayList<Path> paths = getPaths(pixels);
    }

    private ArrayList<Path> getPaths(int pixels[][]){
        return null;
    }

    public static void main(String args[]){
        VectorCreator me = new VectorCreator();

        me.process();
    }
}
