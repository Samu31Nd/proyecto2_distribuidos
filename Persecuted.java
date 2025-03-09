public class Persecuted {
  private Coord startPoint, endPoint, actualPosition;
  private double angle;
  public double getAngle() {
    return angle;
  }

  private double speed;
  private boolean reachEnd, reached;
  public boolean isReached() {
    return reached;
  }

  public boolean isReachEnd() {
    return reachEnd;
  }

  private int width, height;

  public Persecuted(Coord startPoint, Coord endPoint, double speed, int w, int h) {
      this.startPoint = startPoint;
      this.endPoint = endPoint;
      this.actualPosition = Coord.Clone(startPoint);
      this.speed = speed;
      this.angle = Math.atan2(endPoint.y - actualPosition.y, endPoint.x - actualPosition.x);
      this.width = w;
      this.height = h;
  }

  private static final double BASE_TURN_RATE = 0.08;
  private static final double FAST_TURN_RATE = 0.12;
  private static final double ESCAPE_DISTANCE = 50;   //danger dist
  private static final double SAFE_DISTANCE = 300;    // p.dist > 300 del jugador? endpoint : avoid
  private static final double STOP_DISTANCE = 8;
  private static final double BORDER_AVOIDANCE_DISTANCE = 50; // Dist evitar bordes

  public void move(Persecutor[] persecutors) {
      if (reachEnd || reached) return;

      // prom distancia perseguidores
      double avgX = 0, avgY = 0;
      for (Persecutor persecutor : persecutors) {
          if (persecutor.isReachEnd()) reached = true;
          avgX += persecutor.getActualPosition().x;
          avgY += persecutor.getActualPosition().y;
      }

      avgX /= persecutors.length;
      avgY /= persecutors.length;

      // dirección al objetivo y de escape
      double toTargetAngle = Math.atan2(endPoint.y - actualPosition.y, endPoint.x - actualPosition.x);
      double escapeAngle = Math.atan2(actualPosition.y - avgY, actualPosition.x - avgX);

      double distanceToEnemies = Math.hypot(avgX - actualPosition.x, avgY - actualPosition.y);
      double weightEscape = 1.0, weightTarget = 1.0;

      if (distanceToEnemies < ESCAPE_DISTANCE) {
          weightEscape = 3.0; // Escape prioritario
          weightTarget = 1.0;
      } else if (distanceToEnemies < SAFE_DISTANCE) {
          weightEscape = 1.6;//0.8;
          weightTarget = 1.6;//2.0;
      }

      double avoidAngle = 0;
      boolean avoidingBorder = false;

      if (actualPosition.x < BORDER_AVOIDANCE_DISTANCE) {
          avoidAngle = Math.toRadians(0); //der
          avoidingBorder = true;
      } else if (actualPosition.x > width - BORDER_AVOIDANCE_DISTANCE) {
          avoidAngle = Math.toRadians(180); // izq
          avoidingBorder = true;
      }
      if (actualPosition.y < BORDER_AVOIDANCE_DISTANCE) {
          avoidAngle = Math.toRadians(90); // down
          avoidingBorder = true;
      } else if (actualPosition.y > height - BORDER_AVOIDANCE_DISTANCE) {
          avoidAngle = Math.toRadians(-90); // up
          avoidingBorder = true;
      }

      double finalAngle = Math.atan2(
          weightEscape * Math.sin(escapeAngle) + weightTarget * Math.sin(toTargetAngle),
          weightEscape * Math.cos(escapeAngle) + weightTarget * Math.cos(toTargetAngle)
      );

      // ajustar final angulo
      if (avoidingBorder) {
          finalAngle = Math.atan2(
              0.6 * Math.sin(finalAngle) + 0.6 * Math.sin(avoidAngle),
              0.6 * Math.cos(finalAngle) + 0.6 * Math.cos(avoidAngle)
          );
      }
      double angleDiff = finalAngle - angle;
      // ajuste suave
      angleDiff = Math.atan2(Math.sin(angleDiff), Math.cos(angleDiff));
      double turnRate = (distanceToEnemies < ESCAPE_DISTANCE) ? FAST_TURN_RATE : BASE_TURN_RATE;
      angle += angleDiff * turnRate;

      actualPosition.x += Math.cos(angle) * speed;
      actualPosition.y += Math.sin(angle) * speed;

      double distanceToTarget = Math.hypot(endPoint.x - actualPosition.x, endPoint.y - actualPosition.y);
      if (distanceToTarget < STOP_DISTANCE) {
          reachEnd = true;
          actualPosition.x = endPoint.x;
          actualPosition.y = endPoint.y;
          System.out.println("¡Llegó al destino!");
      }

  }

  /*
   * Getters
   */
  public Coord getActualPosition() { return actualPosition; }
  public Coord getEndPoint() { return endPoint; }
  public Coord getStartPoint() { return startPoint; }

  public static Persecuted defaultPlane(int w, int h, double speed) {
      return new Persecuted(Coord.getRandCoordenada(w, h), Coord.getRandCoordenada(w, h), speed, w, h);
  }
}
