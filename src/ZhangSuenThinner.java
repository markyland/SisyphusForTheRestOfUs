import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 2/28/18
 * Time: 8:47 AM
 */
public class ZhangSuenThinner {

      public static int[][] doZhangSuenThinning(final int[][] givenImage, boolean changeGivenImage) {
          int[][] binaryImage;
          if (changeGivenImage) {
              binaryImage = givenImage;
          } else {
              binaryImage = givenImage.clone();
          }
          int a, b;
          List<Point> pointsToChange = new LinkedList<>();
          boolean hasChange;
          do {
              hasChange = false;
              for (int y = 1; y + 1 < binaryImage.length; y++) {
                  for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                      a = getA(binaryImage, y, x);
                      b = getB(binaryImage, y, x);
                      if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                              && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
                              && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                          pointsToChange.add(new Point(x, y));

                          hasChange = true;
                      }
                  }
              }
              for (Point point : pointsToChange) {
                  binaryImage[point.y][point.x] = 0;
              }
              pointsToChange.clear();
              for (int y = 1; y + 1 < binaryImage.length; y++) {
                  for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                      a = getA(binaryImage, y, x);
                      b = getB(binaryImage, y, x);
                      if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                              && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
                              && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                          pointsToChange.add(new Point(x, y));
                          hasChange = true;
                      }
                  }
              }
              for (Point point : pointsToChange) {
                  binaryImage[point.y][point.x] = 0;
              }
              pointsToChange.clear();
          } while (hasChange);
          return binaryImage;
      }

      private static int getA(int[][] binaryImage, int y, int x) {
          int count = 0;
  //p2 p3
          if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
              count++;
          }
  //p3 p4
          if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
              count++;
          }
  //p4 p5
          if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
              count++;
          }
  //p5 p6
          if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
              count++;
          }
  //p6 p7
          if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
              count++;
          }
  //p7 p8
          if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
              count++;
          }
  //p8 p9
          if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
              count++;
          }
  //p9 p2
          if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
              count++;
          }
          return count;
      }

      private static int getB(int[][] binaryImage, int y, int x) {
          return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
                  + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                  + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
      }
}
