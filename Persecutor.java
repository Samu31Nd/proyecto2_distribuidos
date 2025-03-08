public class Persecutor{

    private Coord reachPoint, actualPosition;
    private double angle;
    private double speed;
    private boolean reachEnd;
    private boolean failed;
    public boolean isReachEnd() {
        return reachEnd;
    }

    public Persecutor(Coord startPoint, double speed) {
        this.actualPosition = startPoint;
        this.speed = speed;
        this.angle = 0;
        this.reachEnd = false;
    }

    public void setNewReachingPoint(Persecuted plane) {
        if (plane.isReachEnd()) {
            this.failed = true;
            return;
        } else if (plane.isReached()) {
            this.reachEnd = true;
            return;
        }
        this.reachPoint = plane.getActualPosition();
    }

    private static final double TURN_RATE = 0.05;

    public void move() {
        if (reachEnd || failed) return;
        double desiredAngle = Math.atan2(reachPoint.y - actualPosition.y, reachPoint.x - actualPosition.x);
        // Ajustar el ángulo suavemente
        double angleDiff = desiredAngle - angle;
        angleDiff = Math.atan2(Math.sin(angleDiff), Math.cos(angleDiff)); // Normalizar ángulo
        angle += angleDiff * TURN_RATE; // Cambio progresivo del ángulo

        // Mover el avión en la dirección del ángulo actual
        actualPosition.x += Math.cos(angle) * speed;
        actualPosition.y += Math.sin(angle) * speed;

        // Comprobar si ha llegado al destino
        if (Math.hypot(reachPoint.x - actualPosition.x, reachPoint.y - actualPosition.y) < 5) {
            reachEnd = true;
            actualPosition.x = reachPoint.x;
            actualPosition.y = reachPoint.y;
            System.out.println("El avion ha sido alcanzado!");
        }

    }

    public Coord getActualPosition() {
        return this.actualPosition;
    }

    public Coord getEndPoint() {
        return this.reachPoint;
    }
    
}
