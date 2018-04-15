package v2; /**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/24/18
 * Time: 8:10 PM
 */

import com.slightlyloony.jsisyphus.ATrack;
import util.SisyphusUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class GraphSolver2 {

    static HashMap<String, Integer> vertices = new HashMap<String, Integer>();
    static int currentVertice=1;

    static HashMap<String, Path> pathList = new HashMap<String, Path>();
    static ArrayList<Point> answerPoints = new ArrayList<Point>();

    static JProgressBar progressBar;

    public static void convert(String inputFile, String outputFileTHR, String outputFilePNG, boolean addErase, JProgressBar progressBar) throws Exception {
        vertices.clear();
        currentVertice=1;
        pathList.clear();
        answerPoints.clear();

        GraphSolver2.progressBar=progressBar;

        BufferedReader in = new BufferedReader(new FileReader(inputFile));

        String line=in.readLine();

        int lastPath=-1;

        int lastVerticeId=-1;

        ArrayList<Point> pathVertices=new ArrayList<Point>();

        double lastX=-1, lastY=-1;

        while (line!=null){

            String tokens[] = line.split(",");

            double x=Double.parseDouble(tokens[1]);
            double y=Double.parseDouble(tokens[2]);

            int pathId=Integer.parseInt(tokens[4]);

            if (pathId!=lastPath){
                Integer verticeId=getVerticeId(x, y);

                if (lastVerticeId!=-1){
                    Path path=new Path("" + (pathList.size()+1), lastVerticeId, getVerticeId(lastX, lastY), new ArrayList<>(pathVertices));
                    pathList.put("" + (pathList.size()+1), path);
                    //System.err.println("added : " + path);

                    List<Point> reversePathVertices=new ArrayList<>(pathVertices);
                    Collections.reverse(reversePathVertices);
                    path=new Path("" + (pathList.size()+1), getVerticeId(lastX, lastY), lastVerticeId, reversePathVertices);
                    pathList.put("" + (pathList.size()+1), path);
                    //System.err.println("added : " + path);
                }

                lastPath=pathId;
                lastVerticeId=verticeId;

                pathVertices.clear();
            }

            lastX = x;
            lastY = y;

            pathVertices.add(new Point(x, y));

            line=in.readLine();
        }

        Path endPath=new Path("" + (pathList.size()+1), lastVerticeId, getVerticeId(lastX, lastY), new ArrayList<>(pathVertices));
        pathList.put("" + (pathList.size()+1), endPath);

        List<Point> reversePathVertices=new ArrayList<>(pathVertices);
        Collections.reverse(reversePathVertices);
        endPath=new Path("" + (pathList.size()+1), getVerticeId(lastX, lastY), lastVerticeId, reversePathVertices);
        pathList.put("" + (pathList.size()+1), endPath);

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------

        double minX=Double.MAX_VALUE;
        double minY=Double.MAX_VALUE;
        double maxX=0;
        double maxY=0;

        for (Path path : pathList.values()){
            for (Point point : path.pathVertices){
                minX=Math.min(point.x, minX);
                minY=Math.min(point.y, minY);

                maxX=Math.max(point.x, maxX);
                maxY=Math.max(point.y, maxY);
            }
        }

        for (Path path : pathList.values()){
            for (Point point : path.pathVertices){
                point.x=point.x-minX-((maxX-minX)/2.0);
                point.y=point.y-minY-((maxY-minY)/2.0);
            }
        }

        double maxHypot=0;

        for (Path path : pathList.values()){
            for (int i=0; i<path.pathVertices.size(); i++){
                Point point = path.pathVertices.get(i);

                if (Math.hypot(point.x, point.y) > maxHypot){
                    maxHypot=Math.hypot(point.x, point.y);
                }
            }
        }

        for (Path path : pathList.values()){
            for (Point point : path.pathVertices){
                point.x/=maxHypot;
                point.y/=maxHypot;
            }
        }

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //split here
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------

        double minHypot=Double.MAX_VALUE;
        Path minHypotPath=null;
        int minHypotPointIndex=-1;

        for (Path path : pathList.values()){
            for (int i=0; i<path.pathVertices.size(); i++){
                Point point = path.pathVertices.get(i);

                if (Math.hypot(point.x, point.y) < minHypot){
                    minHypot=Math.hypot(point.x, point.y);
                    minHypotPath=path;
                    minHypotPointIndex=i;
                }
            }
        }

        int verticeId;

        pathVertices = new ArrayList<>();

        if (minHypotPointIndex==0){
            verticeId=minHypotPath.toV;

            pathVertices.addAll(minHypotPath.pathVertices);
        }
        else{
            verticeId=minHypotPath.fromV;

            for (int i=minHypotPointIndex; i>=0; i--){
                pathVertices.add(minHypotPath.pathVertices.get(i));
            }
        }

        pathList.put("start", new Path("start", 0, verticeId, pathVertices));

        reversePathVertices=new ArrayList<>(pathVertices);
        Collections.reverse(reversePathVertices);
        pathList.put("revStart", new Path("revStart", verticeId, 0, reversePathVertices));

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------

        CPP cpp = new CPP(vertices.size() + 1);

        for (Path path : pathList.values()){
            //System.err.println(path);

            cpp.addArc(path.index, path.fromV, path.toV, path.cost);
        }

        cpp.solve(); // find the CPT

        cpp.printCPT(0); // print it, starting from vertex 0

//        for (Point point : answerPoints){
//            System.err.println(point.x + "," + point.y);
//        }

        plot(outputFileTHR, outputFilePNG, answerPoints, addErase);

        // System.out.println("Cost = " + G.cost());
    }

    private static void plot(final String outputFileTHR, final String outputFilePNG, final List<Point> points, final boolean addErase) throws Exception {
        new ATrack(""){
            @Override
            protected void trace() throws IOException {
                dc.setEraseSpacing(0.005);

                if (addErase) {
                    SisyphusUtil.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromRT(1, 0), true);
                }

                SisyphusUtil.eraseToManually(dc, com.slightlyloony.jsisyphus.Point.fromXY(points.get(0).x, points.get(0).y), false);

                for (Point point : points){
                    com.slightlyloony.jsisyphus.Point dest = com.slightlyloony.jsisyphus.Point.fromXY(point.x, point.y);

                    //System.err.println(point.x+","+point.y);

                    dc.lineTo(dc.getCurrentRelativePosition().vectorTo(dest));
                }

                dc.renderPNG( outputFilePNG );
                dc.write( outputFileTHR );
            }
        }.trace();
    }

    private static Integer getVerticeId(double x, double y){
        Integer verticeId=vertices.get(x + "-" + y);

        if (verticeId==null){
            verticeId=currentVertice;

            vertices.put(x + "-" + y, verticeId);

            currentVertice++;
        }

        return verticeId;
    }

    static class Point{
        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point copy() {
            return new Point(x, y);
        }
    }

    static class Path{
        String index;
        int fromV;
        int toV;
        float cost=0;
        List<Point> pathVertices;

        Path(String index, int fromV, int toV, List<Point> pathVertices) {
            this.index = index;
            this.fromV = fromV;
            this.toV = toV;

            this.pathVertices=new ArrayList<>();

            for (Point point : pathVertices){
                this.pathVertices.add(point.copy());
            }

            int sz=pathVertices.size();
            for (int i=1; i<sz; i++){
                this.cost+=Math.hypot(pathVertices.get(i).x-pathVertices.get(i-1).x, pathVertices.get(i).y-pathVertices.get(i-1).y);
            }
        }

        @Override
        public String toString() {
            return "Path{" +
                    "index='" + index + '\'' +
                    ", fromV=" + fromV +
                    ", toV=" + toV +
                    ", cost=" + cost +
                    '}';
        }
    }
}



