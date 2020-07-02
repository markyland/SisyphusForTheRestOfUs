package v1; /**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/24/18
 * Time: 8:10 PM
 */

import com.slightlyloony.jsisyphus.*;
import research.VectorCreator;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class GraphSolver {

    int            N;                // number of vertices

    int            delta[];          // deltas of vertices

    int            neg[], pos[];     // unbalanced vertices

    int            arcs[][];         // adjacency matrix, counts arcs between

    // vertices

    Vector<String> label[][];        // vectors of labels of arcs (for each

    // vertex

    // pair)

    int            f[][];            // repeated arcs in CPT

    float          c[][];            // costs of cheapest arcs or paths

    String         cheapestLabel[][]; // labels of cheapest arcs

    boolean        defined[][];      // whether path cost is defined between

    // vertices

    int            path[][];         // spanning tree of the graph

    float          basicCost;        // total cost of traversing each arc once



    void solve()

    {

        leastCostPaths();

        checkValid();

        findUnbalanced();

        findFeasible();

        while (improvements())

            ;

    }



    // allocate array memory, and instantiate graph object

    @SuppressWarnings("unchecked")

    GraphSolver(int vertices){

        if ((N = vertices) <= 0)

            throw new Error("Graph is empty");

        delta = new int[N];

        defined = new boolean[N][N];

        label = new Vector[N][N];

        c = new float[N][N];

        f = new int[N][N];

        arcs = new int[N][N];

        cheapestLabel = new String[N][N];

        path = new int[N][N];

        basicCost = 0;

    }



    GraphSolver addArc(String lab, int u, int v, float cost)

    {

        if (!defined[u][v])

            label[u][v] = new Vector<String>();

        label[u][v].addElement(lab);

        basicCost += cost;

        if (!defined[u][v] || c[u][v] > cost)

        {

            c[u][v] = cost;

            cheapestLabel[u][v] = lab;

            defined[u][v] = true;

            path[u][v] = v;

        }

        arcs[u][v]++;

        delta[u]++;

        delta[v]--;

        return this;

    }



    void leastCostPaths()

    {
        int lastPercentage=-1;

        for (int k = 0; k < N; k++){
            int percentage =(int)(k*100.0/N);

            if (percentage!=lastPercentage){
                System.out.println(percentage + "%");
                progressBar.setValue(percentage);

                lastPercentage=percentage;
            }

            for (int i = 0; i < N; i++)

                if (defined[i][k])

                    for (int j = 0; j < N; j++)

                        if (defined[k][j]

                                && (!defined[i][j] || c[i][j] > c[i][k]

                                + c[k][j]))

                        {

                            path[i][j] = path[i][k];

                            c[i][j] = c[i][k] + c[k][j];

                            defined[i][j] = true;

                            if (i == j && c[i][j] < 0)

                                return; // stop on negative cycle

                        }
        }

    }



    void checkValid()

    {

        for (int i = 0; i < N; i++)

        {

            for (int j = 0; j < N; j++)

                if (!defined[i][j])

                    throw new Error("Graph is not strongly connected");

            if (c[i][i] < 0)

                throw new Error("Graph has a negative cycle");

        }

    }



    float cost()

    {

        return basicCost + phi();

    }



    float phi()

    {

        float phi = 0;

        for (int i = 0; i < N; i++)

            for (int j = 0; j < N; j++)

                phi += c[i][j] * f[i][j];

        return phi;

    }



    void findUnbalanced()

    {

        int nn = 0, np = 0; // number of vertices of negative/positive delta

        for (int i = 0; i < N; i++)

            if (delta[i] < 0)

                nn++;

            else if (delta[i] > 0)

                np++;

        neg = new int[nn];

        pos = new int[np];

        nn = np = 0;

        for (int i = 0; i < N; i++)

            // initialise sets

            if (delta[i] < 0)

                neg[nn++] = i;

            else if (delta[i] > 0)

                pos[np++] = i;

    }



    void findFeasible()

    {   // delete next 3 lines to be faster, but non-reentrant

        int delta[] = new int[N];

        for (int i = 0; i < N; i++)

            delta[i] = this.delta[i];

        for (int u = 0; u < neg.length; u++)

        {

            int i = neg[u];

            for (int v = 0; v < pos.length; v++)

            {

                int j = pos[v];

                f[i][j] = -delta[i] < delta[j] ? -delta[i] : delta[j];

                delta[i] += f[i][j];

                delta[j] -= f[i][j];

            }

        }

    }



    boolean improvements()

    {

        GraphSolver residual = new GraphSolver(N);

        for (int u = 0; u < neg.length; u++)

        {

            int i = neg[u];

            for (int v = 0; v < pos.length; v++)

            {

                int j = pos[v];

                residual.addArc(null, i, j, c[i][j]);

                if (f[i][j] != 0)

                    residual.addArc(null, j, i, -c[i][j]);

            }

        }

        residual.leastCostPaths(); // find a negative cycle

        for (int i = 0; i < N; i++)

            if (residual.c[i][i] < 0) // cancel the cycle (if any)

            {

                int k = 0, u, v;

                boolean kunset = true;

                u = i;

                do // find k to cancel

                {

                    v = residual.path[u][i];

                    if (residual.c[u][v] < 0 && (kunset || k > f[v][u]))

                    {

                        k = f[v][u];

                        kunset = false;

                    }

                }

                while ((u = v) != i);

                u = i;

                do // cancel k along the cycle

                {

                    v = residual.path[u][i];

                    if (residual.c[u][v] < 0)

                        f[v][u] -= k;

                    else

                        f[u][v] += k;

                }

                while ((u = v) != i);

                return true; // have another go

            }

        return false; // no improvements found

    }



    static final int NONE = -1; // anything < 0



    int findPath(int from, int f[][]) // find a path between unbalanced vertices

    {

        for (int i = 0; i < N; i++)

            if (f[from][i] > 0)

                return i;

        return NONE;

    }



    void printCPT(int startVertex)

    {

        int v = startVertex;

        // delete next 7 lines to be faster, but non-reentrant

        int arcs[][] = new int[N][N];

        int f[][] = new int[N][N];

        for (int i = 0; i < N; i++)

            for (int j = 0; j < N; j++)

            {

                arcs[i][j] = this.arcs[i][j];

                f[i][j] = this.f[i][j];

            }

        while (true)

        {

            int u = v;

            if ((v = findPath(u, f)) != NONE)

            {

                f[u][v]--; // remove path

                for (int p; u != v; u = p) // break down path into its arcs

                {

                    p = path[u][v];

//                    System.out.println("Take arc " + cheapestLabel[u][p]
//
//                            + " from " + u + " to " + p);

                    Path path=pathList.get(cheapestLabel[u][p]);

                    answerPoints.addAll(path.pathVertices);
                }

            }

            else

            {

                int bridgeVertex = path[u][startVertex];

                if (arcs[u][bridgeVertex] == 0)

                    break; // finished if bridge already used

                v = bridgeVertex;

                for (int i = 0; i < N; i++)

                    // find an unused arc, using bridge last

                    if (i != bridgeVertex && arcs[u][i] > 0)

                    {

                        v = i;

                        break;

                    }

                arcs[u][v]--; // decrement count of parallel arcs

//                System.out.println("Take arc "
//
//                        + label[u][v].elementAt(arcs[u][v]) + " from " + u
//
//                        + " to " + v); // use each arc label in turn

                Path path=pathList.get(label[u][v].elementAt(arcs[u][v]));

                answerPoints.addAll(path.pathVertices);
                //     answerPoints.add(new Point(0,0));
            }
        }
    }

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

        System.out.println("Creating paths");
        GraphSolver.progressBar=progressBar;

        VectorCreator vectorCreator = new VectorCreator();

        ArrayList<ArrayList<VectorCreator.Point>> vectorCreatorPaths = vectorCreator.getPaths(inputFile);

        int lastPath=-1;

        int lastVerticeId=-1;

        ArrayList<Point> pathVertices=new ArrayList<>();

        double lastX=-1, lastY=-1;

        int sz=vectorCreatorPaths.size();

        for (int pathId=0; pathId<sz; pathId++) {
            ArrayList<VectorCreator.Point> vectorCreatorPath = vectorCreatorPaths.get(pathId);

            for (VectorCreator.Point point : vectorCreatorPath) {
                double x = point.x;
                double y = point.y;

                if (pathId != lastPath) {
                    Integer verticeId = getVerticeId(x, y);

                    if (lastVerticeId != -1) {
                        Path path = new Path("" + (pathList.size() + 1), lastVerticeId, getVerticeId(lastX, lastY), new ArrayList<>(pathVertices));
                        pathList.put("" + (pathList.size() + 1), path);
                        //System.err.println("added : " + path);

                        List<Point> reversePathVertices = new ArrayList<>(pathVertices);
                        Collections.reverse(reversePathVertices);
                        path = new Path("" + (pathList.size() + 1), getVerticeId(lastX, lastY), lastVerticeId, reversePathVertices);
                        pathList.put("" + (pathList.size() + 1), path);
                        //System.err.println("added : " + path);
                    }

                    lastPath = pathId;
                    lastVerticeId = verticeId;

                    pathVertices.clear();
                }

                lastX = x;
                lastY = y;

                pathVertices.add(new Point(x, y));
            }
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

        Path maxHypotPath=null;
        int maxHypotPointIndex=-1;
        for (Path path : pathList.values()){
            for (int i=0; i<path.pathVertices.size(); i++){
                Point point = path.pathVertices.get(i);

                if (Math.hypot(point.x, point.y) > maxHypot){
                    maxHypot=Math.hypot(point.x, point.y);
                    maxHypotPath=path;
                    maxHypotPointIndex=i;
                }
            }
        }

        for (Path path : pathList.values()){
            for (Point point : path.pathVertices){
                point.x/=maxHypot;
                point.y/=maxHypot;
            }
        }

//        if (maxHypotPointIndex!=0 && maxHypotPointIndex!=maxHypotPath.pathVertices.size()-1){
//            //split it
//            pathList.remove(maxHypotPath.index);
//
//            Point maxHypotPoint = maxHypotPath.pathVertices.get(maxHypotPointIndex);
//
//            Path path = new Path("start", getVerticeId(maxHypotPoint.x, maxHypotPoint.y), maxHypotPath.toV, maxHypotPath.pathVertices.subList(maxHypotPointIndex, maxHypotPath.pathVertices.size()));
//            pathList.put(path.index, path);
//
//            path = new Path("blah", getVerticeId(maxHypotPoint.x, maxHypotPoint.y), maxHypotPath.toV, maxHypotPath.pathVertices.subList(maxHypotPointIndex, maxHypotPath.pathVertices.size()));
//            pathList.put(path.index, path);
//        }

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println("Finding optimal path");
        GraphSolver G = new GraphSolver(vertices.size() + 1);

        int verticeId;

        pathVertices = new ArrayList<>();

        if (maxHypotPointIndex==0){
            verticeId=maxHypotPath.toV;

            pathVertices.addAll(maxHypotPath.pathVertices);
        }
        else{
            verticeId=maxHypotPath.fromV;

            for (int i=maxHypotPointIndex; i>=0; i--){
                pathVertices.add(maxHypotPath.pathVertices.get(i));
            }
        }

        pathList.put("start", new Path("start", 0, verticeId, pathVertices));

        reversePathVertices=new ArrayList<>(pathVertices);
        Collections.reverse(reversePathVertices);
        pathList.put("revStart", new Path("revStart", verticeId, 0, reversePathVertices));

        for (Path path : pathList.values()){
            //System.err.println(path);

            G.addArc(path.index, path.fromV, path.toV, path.cost);
        }

        G.solve(); // find the CPT

        G.printCPT(0); // print it, starting from vertex 0

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
                    dc.eraseTo(com.slightlyloony.jsisyphus.Point.fromXY(points.get(0).x, points.get(0).y));
                }

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


    // Print arcs and f

    void debugarcf()

    {

        for (int i = 0; i < N; i++)

        {

            System.out.print("f[" + i + "]= ");

            for (int j = 0; j < N; j++)

                System.out.print(f[i][j] + " ");

            System.out.print("  arcs[" + i + "]= ");

            for (int j = 0; j < N; j++)

                System.out.print(arcs[i][j] + " ");

            System.out.println();

        }

    }



    // Print out most of the matrices: defined, path and f

    void debug()

    {

        for (int i = 0; i < N; i++)

        {

            System.out.print(i + " ");

            for (int j = 0; j < N; j++)

                System.out

                        .print(j + ":" + (defined[i][j] ? "T" : "F") + " "

                                + c[i][j] + " p=" + path[i][j] + " f="

                                + f[i][j] + "; ");

            System.out.println();

        }

    }



    // Print out non zero f elements, and phi

    void debugf()

    {

        float sum = 0;

        for (int i = 0; i < N; i++)

        {

            boolean any = false;

            for (int j = 0; j < N; j++)

                if (f[i][j] != 0)

                {

                    any = true;

                    System.out.print("f(" + i + "," + j + ":" + label[i][j]

                            + ")=" + f[i][j] + "@" + c[i][j] + "  ");

                    sum += f[i][j] * c[i][j];

                }

            if (any)

                System.out.println();

        }

        System.out.println("-->phi=" + sum);

    }



    // Print out cost matrix.

    void debugc()

    {

        for (int i = 0; i < N; i++)

        {

            boolean any = false;

            for (int j = 0; j < N; j++)

                if (c[i][j] != 0)

                {

                    any = true;

                    System.out.print("c(" + i + "," + j + ":" + label[i][j]

                            + ")=" + c[i][j] + "  ");

                }

            if (any)

                System.out.println();

        }

    }

}



class OpenCPP

{

    class Arc

    {

        String lab;

        int    u, v;

        float  cost;



        Arc(String lab, int u, int v, float cost)

        {

            this.lab = lab;

            this.u = u;

            this.v = v;

            this.cost = cost;

        }

    }



    Vector<Arc> arcs = new Vector<Arc>();

    int         N;



    OpenCPP(int vertices)

    {

        N = vertices;

    }



    OpenCPP addArc(String lab, int u, int v, float cost)

    {

        if (cost < 0)

            throw new Error("Graph has negative costs");

        arcs.addElement(new Arc(lab, u, v, cost));

        return this;

    }



    float printCPT(int startVertex)

    {

        GraphSolver bestGraph = null, g;

        float bestCost = 0, cost;

        int i = 0;

        do

        {

            g = new GraphSolver(N + 1);

            for (int j = 0; j < arcs.size(); j++)

            {

                Arc it = arcs.elementAt(j);

                g.addArc(it.lab, it.u, it.v, it.cost);

            }

            cost = g.basicCost;

            g.findUnbalanced(); // initialise g.neg on original graph

            g.addArc("'virtual start'", N, startVertex, cost);

            g.addArc("'virtual end'",

                    // graph is Eulerian if neg.length=0

                    g.neg.length == 0 ? startVertex : g.neg[i], N, cost);

            g.solve();

            if (bestGraph == null || bestCost > g.cost())

            {

                bestCost = g.cost();

                bestGraph = g;

            }

        }

        while (++i < g.neg.length);

        System.out.println("Open CPT from " + startVertex

                + " (ignore virtual arcs)");

        bestGraph.printCPT(N);

        return cost + bestGraph.phi();

    }



    public static void test()

    {

        OpenCPP G = new OpenCPP(4); // create a graph of four vertices

        // add the arcs for the example graph

        G.addArc("a", 0, 1, 1).addArc("b", 0, 2, 1).addArc("c", 1, 2, 1)

                .addArc("d", 1, 3, 1).addArc("e", 2, 3, 1).addArc("f", 3, 0, 1);

        int besti = 0;

        float bestCost = 0;

        for (int i = 0; i < 4; i++)

        {

            System.out.println("Solve from " + i);

            float c = G.printCPT(i);

            System.out.println("Cost = " + c);

            if (i == 0 || c < bestCost)

            {

                bestCost = c;

                besti = i;

            }

        }

        G.printCPT(besti);

        System.out.println("Cost = " + bestCost);

    }

}
