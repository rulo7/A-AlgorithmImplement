package modelo;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author raul-portatil
 */
public class DataConstants {

    public static String log = "";
    public static  int FILAS = 20;
    public static  int COLUMNAS = 20;
    public static final String imgPlayer = "/images/player.png";
    public static final String imgObstacle = "/images/obstacle.png";
    public static final String imgDestiny = "/images/destiny.png";
    public static final String imgWayPoint = "/images/wayPoint.png";
    public static final String imgRuta = "/images/way.png";
    public static final String imgRisk1 = "/images/risk1.png";
    public static final String imgRisk2 = "/images/risk2.png";
    public static final String imgRisk3 = "/images/risk3.png";
    public static final String imgNvl1 = "/images/nvl1.png";
    public static final String imgNvl2 = "/images/nvl2.png";
    public static final String imgNvl3 = "/images/nvl3.png";
    
    public static String elementToAdd = null;
    public static ArrayList<Point> obstaculos = new ArrayList<>();
    public static ArrayList<Point> wayPoints = new ArrayList<>();
    public static ArrayList<Point> riesgos1 = new ArrayList<>();
    public static ArrayList<Point> riesgos2 = new ArrayList<>();
    public static ArrayList<Point> riesgos3 = new ArrayList<>();
    public static int riesgo1 = 0;
    public static int riesgo2 = 0;
    public static int riesgo3 = 0;
    public static double reduccionRiesgoPorCorteza = 0;
    public static Point inicio = null;
    public static Point destino = null;
}
