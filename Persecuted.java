public class Persecuted implements Plane {

    private Coord startPoint, endPoint, actualPosition;
    private double angle;
    private double speed;
    private boolean reachEnd;
  
    public Persecuted(Coord startPoint, Coord endPoint, double speed) {
      this.startPoint =  startPoint;
      this.actualPosition = Coord.Clone(startPoint);
      this.endPoint = endPoint;
      this.speed = speed;
      this.angle = 0;
      //this.angle = Math.atan2(endPoint.y-startPoint.y, endPoint.x - startPoint.x);
    }
  
    private static final double BASE_TURN_RATE = 0.05; // Giro normal
      private static final double FAST_TURN_RATE = 0.2;  // Giro rápido cerca del objetivo
      private static final double STOP_DISTANCE = 5; // Distancia para detenerse
  
      public void move() {
          if (reachEnd) return;
  
          // Calcular la distancia al objetivo
          double distanceToTarget = Math.hypot(endPoint.x - actualPosition.x, endPoint.y - actualPosition.y);
  
          // Si estamos lo suficientemente cerca, detener el avión
          if (distanceToTarget < STOP_DISTANCE) {
              reachEnd = true;
              actualPosition.x = endPoint.x;
              actualPosition.y = endPoint.y;
              return;
          }
  
          double desiredAngle = Math.atan2(endPoint.y - actualPosition.y, endPoint.x - actualPosition.x);
          
          double angleDiff = desiredAngle - angle;
          angleDiff = Math.atan2(Math.sin(angleDiff), Math.cos(angleDiff)); 
  
          // Si estamos cerca del destino, girar más rápido
          double turnRate = (distanceToTarget < 50) ? FAST_TURN_RATE : BASE_TURN_RATE;
          angle += angleDiff * turnRate;
  
          // Ajustar la velocidad cerca del destino (para frenar suavemente)
          double currentSpeed = (distanceToTarget < 30) ? speed * (distanceToTarget / 10) : speed;
  
          // Mover el avión en la dirección del ángulo actual
          actualPosition.x += Math.cos(angle) * currentSpeed;
          actualPosition.y += Math.sin(angle) * currentSpeed;
  
          System.out.println("Posición: " + actualPosition + " | Ángulo: " + Math.toDegrees(angle));
      }
  
    /*
     * getters
     */
    public Coord getActualPosition() { return actualPosition; }
    public Coord getEndPoint() { return endPoint; }
    public Coord getStartPoint() { return startPoint; }
    public static Persecuted defaultPlane(int w, int h){ return new Persecuted( Coord.getRandCoordenada(w,h), 
      Coord.getRandCoordenada(w,h), 1.00);}
  }
  