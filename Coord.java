import java.util.Random;

public class Coord {

    public double x, y;
  
    public Coord(double x, double y) {
      this.x = x;
      this.y = y;
    }
  
    // Metodo getter de x
    public int getX() {
      return (int)x;
    }
  
    // Metodo getter de y
    public int getY() {
      return (int)y;
    }
  
    // Sobreescritura del método de la superclase objeto para imprimir con
    // System.out.println( )
    @Override
    public String toString() {
      return "[" + (int)x + "," + (int)y + "]";
    }

    /*
     * c1 - c2
     */
    static Coord substract(Coord c1, Coord c2){
      return new Coord(c1.x - c2.x,c1.y-c2.y);
    }

    static Random rand = new Random();
    static final int despBorder = 80;
    static Coord getRandCoordenada(int xBound, int yBound){
      int x = rand.nextInt(50,xBound-50);
      int y = rand.nextInt(50,yBound-50);
        return new Coord(x, y);
    }

    public static Coord Clone(Coord c){
      return new Coord(c.x, c.y);
    }
  
  }
  
