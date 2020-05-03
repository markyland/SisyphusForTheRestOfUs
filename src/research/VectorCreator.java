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

    public void process() {
        ImageIcon icon = new ImageIcon("C:\\Users\\mark\\Desktop\\flower.jpg");

        Image img = icon.getImage();

        int width = img.getWidth(null);
        int height = img.getHeight(null);
        int[] pixels1D = new int[width * height];

        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels1D, 0, width);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Error: Interrupted Waiting for Pixels");
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            throw new IllegalStateException("Error: Image Fetch Aborted");
        }

        //switch to 2 dim array
        int pixels[][] = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = pixels1D[y * width + x];

                int r = val >> 16 & 0xFF;
                int g = val >> 8 & 0xFF;
                int b = val & 0xFF;

                pixels[y][x] = (r + g + b) / 3.0 > 128 ? 0 : 1;
            }
        }

        //thin!
        ZhangSuenThinner.doZhangSuenThinning(pixels, true);

        //print it...

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.err.print(pixels[i][j] == 0 ? "." : pixels[i][j]);
            }
            System.err.println("");
        }

        System.err.println("");

        //find first point

        int xStart = -1;
        int yStart = -1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y][x] == 1) {
                    xStart = x;
                    yStart = y;
                }
            }
        }

        if (xStart == -1 || yStart == -1) {
            throw new IllegalStateException("Error: No starting point");
        }

        //---------------------------------------------------------------------------------------------------------
        fillIn(pixels);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.err.print(pixels[i][j] == 0 ? "." : pixels[i][j]);
            }
            System.err.println("");
        }

        System.err.println("");
        //---------------------------------------------------------------------------------------------------------

        ArrayList<ArrayList<Point>> paths = new ArrayList<>();

        ArrayList<Point> currentPath = new ArrayList<>();

        getPaths(pixels, xStart, yStart, currentPath, paths);

        //---------------------------------------------------------------------------------------------------------

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\mark\\Desktop\\flower.asc"));

            int sz=paths.size();
            for (int i=0; i<sz; i++){
                ArrayList<Point> path = paths.get(i);

                for (Point point : path) {
                    out.write(","+point.x + "," + (height-point.y-1) + ",0," + (i+1) + "\n");
                }
            }

            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (ArrayList<Point> path : paths){
            for (Point point : path) {
                System.err.print(point.x+","+point.y + " ");
            }
            System.err.println("");
        }
    }

    private void getPaths(int pixels[][], int x, int y, ArrayList<Point> currentPath, ArrayList<ArrayList<Point>> paths){
        currentPath.add(new Point(x, y));

        if ( getPixel(pixels, x, y)==0) {   //been here before
            paths.add(currentPath);
            return;
        }

        pixels[y][x]=0;

        int isRight=Math.min(getPixel(pixels, x+1, y),1);
        int isLeft=Math.min(getPixel(pixels, x-1, y),1);
        int isDown=Math.min(getPixel(pixels, x, y+1),1);
        int isUp=Math.min(getPixel(pixels, x, y-1),1);

        int count=isRight+isLeft+isDown+isUp;

        if (count==1) {       //easy case to next one
            if (isRight==1) {
                getPaths(pixels, x+1, y, currentPath, paths);
            }
            else if (isLeft==1){
                getPaths(pixels, x-1, y, currentPath, paths);
            }
            else if (isDown==1){
                getPaths(pixels, x, y+1, currentPath, paths);
            }
            else{  //isUp
                getPaths(pixels, x, y-1, currentPath, paths);
            }
        }
        else {
            //we could be the end of a line or it could be a fork in the road.  either way its the end of this path

            paths.add(currentPath);

            if (isRight!=0) {
                currentPath=new ArrayList<>();
                currentPath.add(new Point(x, y));
                getPaths(pixels, x+1, y, currentPath, paths);
            }

            if (isLeft!=0){
                currentPath=new ArrayList<>();
                currentPath.add(new Point(x, y));
                getPaths(pixels, x-1, y, currentPath, paths);
            }

            if (isDown!=0){
                currentPath=new ArrayList<>();
                currentPath.add(new Point(x, y));
                getPaths(pixels, x, y+1, currentPath, paths);
            }

            if (isUp!=0){  //isUp
                currentPath=new ArrayList<>();
                currentPath.add(new Point(x, y));
                getPaths(pixels, x, y-1, currentPath, paths);
            }
        }
    }

    private void fillIn(int pixels[][]) {
        for (int y=0; y<pixels.length; y++) {
            for (int x=0; x<pixels[0].length; x++) {
                if (getPixel(pixels, x, y)==0){
                    continue;
                }

                if (getPixel(pixels, x+1, y+1)==1 && getPixel(pixels, x+1, y)==0 && getPixel(pixels, x, y+1)==0){
                    pixels[y][x+1]=1;
                }
                else if (getPixel(pixels, x-1, y-1)==1 && getPixel(pixels, x-1, y)==0 && getPixel(pixels, x, y-1)==0){
                    pixels[y][x-1]=1;
                }
                else if (getPixel(pixels, x+1, y-1)==1 && getPixel(pixels, x+1, y)==0 && getPixel(pixels, x, y-1)==0){
                    pixels[y][x+1]=1;
                }
                else if (getPixel(pixels, x-1, y+1)==1 && getPixel(pixels, x-1, y)==0 && getPixel(pixels, x, y+1)==0){
                    pixels[y][x-1]=1;
                }
            }
        }
    }

    private int getPixel(int pixels[][], int x, int y){
        if (x<0 || y<0 || x>=pixels[0].length || y>=pixels.length){
            return 0;
        }

        return pixels[y][x];
    }

    class Point{
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String args[]){
        VectorCreator me = new VectorCreator();

        me.process();
    }
}
