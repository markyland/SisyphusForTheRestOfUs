package research;

import util.Path;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:44 AM
 */
public class VectorCreator {

    public ArrayList<ArrayList<Point>> getPaths(String inputPath) {
        ImageIcon icon = new ImageIcon(inputPath);

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

//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                System.err.print(pixels[i][j] == 0 ? "." : pixels[i][j]);
//            }
//            System.err.println("");
//        }
//
//        System.err.println("");

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

//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                System.err.print(pixels[i][j] == 0 ? "." : pixels[i][j]);
//            }
//            System.err.println("");
//        }

  //      System.err.println("");
        //---------------------------------------------------------------------------------------------------------

        ArrayList<ArrayList<Point>> paths = getPaths(pixels, xStart, yStart);

        //---------------------------------------------------------------------------------------------------------

//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\mark\\Desktop\\flower.asc"));
//
//            int sz=paths.size();
//            for (int i=0; i<sz; i++){
//                ArrayList<Point> path = paths.get(i);
//
//                for (Point point : path) {
//                    out.write(","+point.x + "," + (height-point.y-1) + ",0," + (i+1) + "\n");
//                }
//            }
//
//            out.flush();
//            out.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        for (ArrayList<Point> path : paths) {
            for (Point point : path) {
                point.y=height-point.y-1;
            }
        }

//        for (ArrayList<Point> path : paths){
//            for (Point point : path) {
//                System.err.print(point.x+","+point.y + " ");
//            }
//            System.err.println("");
//        }

        return paths;
    }

    private ArrayList<ArrayList<Point>> getPaths(int pixels[][], int x, int y){
        ArrayList<ArrayList<Point>> paths = new ArrayList<>();

        Stack<PathAndPoint> stack = new Stack<>();
        stack.add(new PathAndPoint(new ArrayList<>(), new Point(x, y)));

        while (stack.size()>0) {
            PathAndPoint pathAndPoint = stack.pop();

            ArrayList<Point> currentPath = pathAndPoint.path;
            Point point = pathAndPoint.point;

            x=point.x;
            y=point.y;

            currentPath.add(new Point(x, y));

//            if (pixels[y][x]==2){    //jfkdjfldkfjlkfjds
//                continue;
//            }
//
            pixels[y][x]=2;

            int right = getPixel(pixels, x + 1, y);
            int left = getPixel(pixels, x - 1, y);
            int down = getPixel(pixels, x, y + 1);
            int up = getPixel(pixels, x, y - 1);

            int count = (right==1 ? 1 : 0) +
                    (left==1 ? 1 : 0) +
                    (down==1 ? 1 : 0) +
                    (up==1 ? 1 : 0);

            if (count == 1) {       //easy case to next one
                if (right == 1) {
                    stack.add(new PathAndPoint(currentPath, new Point(x+1, y)));
                }
                else if (left == 1) {
                    stack.add(new PathAndPoint(currentPath, new Point(x-1, y)));
                }
                else if (down == 1) {
                    stack.add(new PathAndPoint(currentPath, new Point(x, y+1)));
                }
                else {  //isUp
                    stack.add(new PathAndPoint(currentPath, new Point(x, y-1)));
                }
            }
            else if (count == 0){  //end of the road
//                int count2 = (right==2 ? 1 : 0) +
//                        (left==2 ? 1 : 0) +
//                        (down==2 ? 1 : 0) +
//                        (up==2 ? 1 : 0);
//
//                System.err.println("count2 = " + count2);
//
//                if (count2==1){
//                    if (right == 2) {
//                        currentPath.add(new Point(x+1, y));
//                    }
//                    else if (left == 2) {
//                        currentPath.add(new Point(x-1, y));
//                    }
//                    else if (down == 2) {
//                        currentPath.add(new Point(x, y+1));
//                    }
//                    else {  //isUp
//                        currentPath.add(new Point(x, y-1));
//                    }
//                }

                if (currentPath.size()>1) {
                    paths.add(currentPath);
                }
            }
            else {          //fork
                if (currentPath.size()>1) {
                    paths.add(currentPath);
                }

                if (right == 1) {
                    currentPath = new ArrayList<>();
                    currentPath.add(new Point(x, y));
                    stack.add(new PathAndPoint(currentPath, new Point(x + 1, y)));
                }

                if (left == 1) {
                    currentPath = new ArrayList<>();
                    currentPath.add(new Point(x, y));
                    stack.add(new PathAndPoint(currentPath, new Point(x - 1, y)));
                }

                if (down == 1) {
                    currentPath = new ArrayList<>();
                    currentPath.add(new Point(x, y));
                    stack.add(new PathAndPoint(currentPath, new Point(x, y + 1)));
                }

                if (up == 1) {  //isUp
                    currentPath = new ArrayList<>();
                    currentPath.add(new Point(x, y));
                    stack.add(new PathAndPoint(currentPath, new Point(x, y - 1)));
                }
            }
        }

        return paths;
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

    public static class Point{
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class PathAndPoint {
        ArrayList<Point> path;
        Point point;

        public PathAndPoint(ArrayList<Point> path, Point point) {
            this.path = path;
            this.point = point;
        }
    }

    public static void main(String args[]){
        VectorCreator me = new VectorCreator();

        me.getPaths("C:\\Users\\mark\\Desktop\\dreidel.png");
    }
}
