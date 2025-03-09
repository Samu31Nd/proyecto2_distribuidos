import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GUIPrincipal
 */
public class GUIPrincipal extends JFrame {
  public GUIPanel board;

  public GUIPrincipal(Resolution res, Persecuted plane, int noPersecutors, double speed) {
    setTitle("Persecución");
    setSize(res.toDimension());
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    board = new GUIPanel(res, noPersecutors, speed);
    board.addPlane(plane);
    add(board);
  }
}

class GUIPanel extends JPanel implements ActionListener {

  /*
   * Colors
   */
  static final Color background_color = new Color(222, 214, 140);
  static final Color persecuted_color = new Color(23, 23, 22);
  static final Color persecutor_color = new Color(191, 61, 65);
  static final Color table_color = new Color(255, 255, 255, 200);
  static final Color gridColor = new Color(59, 52, 52, 128);
  /*
   * Font
   */
  static final Font regularFont = new Font("Arial", Font.BOLD, 12);
  static final Font coordFont = new Font("Arial", Font.BOLD, 14);

  Timer timer;
  Graphics2D canvas;
  Persecuted persecuted;
  CircularBuffer<Coord> persecutedTrace;
  Persecutor[] persecutors;
  CircularBuffer<Coord>[] persecutorsTrace;
  public static int WIDTH, HEIGHT;
  private static final int NO_TRACE_DOTS = 10;
  private static final int TIME_TO_TRACE_NEW_DOT = 40;
  private static final int radius_trace = 10;

  private static double time_played = 0;
  private static double speed;

  static final Polygon planePersecuted = new Polygon(
      new int[] { 0, 10, 0, -10, 0 },
      new int[] { 10, -10, -5, -10, 10 },
      5);
  static final Polygon planePersecutor = new Polygon(
      new int[] { 0, 8, 0, -8, 0 },
      new int[] { 8, -8, -4, -8, 8 },
      5);

  private final long startTime;

  @SuppressWarnings("unchecked")
  public GUIPanel(Resolution res, int noPersecutors, double velocity) {
    WIDTH = res.toIntArr()[0] - 76;
    HEIGHT = res.toIntArr()[1] - 97;
    persecutors = new Persecutor[noPersecutors];
    speed = velocity;
    startTime = System.currentTimeMillis();

    timer = new Timer(10, this);
    timer.start();
    this.setPreferredSize(res.toDimension());
    this.setFocusable(true);

    this.persecutedTrace = new CircularBuffer<>(NO_TRACE_DOTS);
    this.persecutorsTrace = new CircularBuffer[noPersecutors];
    for(int i = 0; i < noPersecutors; i++)
      this.persecutorsTrace[i] = new CircularBuffer<>(NO_TRACE_DOTS);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    this.repaint();
  }

  private boolean isShowedEndWindow = false;

  public void paint(Graphics g) {

    if((persecuted.isReached() || persecuted.isReachEnd()) && !isShowedEndWindow){
      isShowedEndWindow = true;
      new EndProgramGUI(persecuted.isReached(), false, time_played*speed);
    }

    if (System.currentTimeMillis() - startTime >= 120000 && !isShowedEndWindow) {
      isShowedEndWindow = true;
      new EndProgramGUI(false, true, time_played * speed); // Perdió por tiempo
  }

    if (isShowedEndWindow) return;

    super.paint(g);
    canvas = (Graphics2D) g;
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.setFont(regularFont);
    drawBackground();
    reDrawPlane();
    reDrawPersecutors();
    reDrawTableCoords();
  }

  private void reDrawTableCoords() {
    canvas.setColor(table_color);
    canvas.fillRect(3,3, 250, 10 + (25*persecutors.length));
    canvas.setColor(Color.BLACK);
    canvas.setFont(coordFont);

    canvas.drawString("Persecuted: " + persecuted.getActualPosition(), 3, 15);
    int i = 1;
    for (Persecutor persecutor : persecutors) {
      canvas.drawString("Persecutor " + i + ": " + persecutor.getActualPosition(), 3, 15 + (20 * i));
      i++;
    }

    canvas.setFont(regularFont);
  }

  private void drawBackground() {
    canvas.setColor(background_color);
    canvas.fillRect(0, 0, WIDTH, HEIGHT);
    canvas.setColor(Color.WHITE);

    canvas.setColor(gridColor);
    for (int x = 0; x < WIDTH; x += 30)
      canvas.drawLine(x, 0, x, HEIGHT);
    for (int y = 0; y < HEIGHT; y += 30)
      canvas.drawLine(0, y, WIDTH, y);
    drawStartAndEndPoint();
  }

  private static final int radiusCircle = 25;

  private void drawStartAndEndPoint() {
    canvas.setColor(Color.BLUE);
    Coord startPoint = this.persecuted.getStartPoint();
    canvas.fillOval(startPoint.getX() - radiusCircle / 2, startPoint.getY() - radiusCircle / 2, radiusCircle,
        radiusCircle);
    Coord endPoint = this.persecuted.getEndPoint();
    canvas.setColor(Color.GREEN);
    canvas.fillOval(endPoint.getX() - radiusCircle / 2, endPoint.getY() - radiusCircle / 2, radiusCircle, radiusCircle);
    canvas.setStroke(new BasicStroke(3.0f));
    canvas.setColor(Color.BLACK);
    canvas.drawOval(startPoint.getX() - radiusCircle / 2, startPoint.getY() - radiusCircle / 2, radiusCircle,
        radiusCircle);
    canvas.drawOval(endPoint.getX() - radiusCircle / 2, endPoint.getY() - radiusCircle / 2, radiusCircle, radiusCircle);
    canvas.drawString("START", startPoint.getX() - 20, startPoint.getY() - 20);
    canvas.drawString("END", endPoint.getX() - 13, endPoint.getY() - 20);
  }

  private void reDrawPlane() {
    reDrawTracePlane();
    time_played += speed;
    canvas.setColor(persecuted_color);
    Coord actualPos = this.persecuted.getActualPosition();
    double directionPlane = this.persecuted.getAngle() - (Math.PI / 2);
    Graphics2D g2d = (Graphics2D) canvas;
    AffineTransform transform = new AffineTransform();

    transform.translate(actualPos.getX(), actualPos.getY());
    transform.rotate(directionPlane);
    Shape transformedArrow = transform.createTransformedShape(planePersecuted);
    g2d.fill(transformedArrow);

    this.persecuted.move(persecutors);

    // Actualizar la lógica de los perseguidores
    for (Persecutor p : persecutors) {
      p.setNewReachingPoint(persecuted);
    }
  }

  private static int counter = 0;
  private void reDrawTracePlane(){
    if( counter++ == TIME_TO_TRACE_NEW_DOT){
      counter = 0;
      persecutedTrace.add(Coord.Clone(persecuted.getActualPosition()));
    }
    for(int i = 0; i < persecutedTrace.size(); i++) {
      canvas.setColor(new Color(0,0,0,(i*255)/NO_TRACE_DOTS));
      Coord actualCoord = persecutedTrace.get(i);
      canvas.drawOval(actualCoord.getX() - radius_trace/2, actualCoord.getY() - radius_trace/2, radius_trace, radius_trace);
      
    }
  }

  private void reDrawPersecutors() {
    int i = 0;
    for (Persecutor persecutor : persecutors) {
      reDrawTracePersecutors(persecutor, i++);
      canvas.setColor(persecutor_color);
      Coord actualPos = persecutor.getActualPosition();
      double directionPlane = persecutor.getAngle() - (Math.PI / 2);

      Graphics2D g2d = (Graphics2D) canvas;
      AffineTransform transform = new AffineTransform();
      transform.translate(actualPos.getX(), actualPos.getY());
      transform.rotate(directionPlane);
      Shape transformedArrow = transform.createTransformedShape(planePersecutor);
      g2d.fill(transformedArrow);
      persecutor.move();
    }
  }

  private void reDrawTracePersecutors(Persecutor persecutor, int i) {
    if( counter == TIME_TO_TRACE_NEW_DOT){
      persecutorsTrace[i].add(Coord.Clone(persecutor.getActualPosition()));
    }
    for(int j = 0; j < persecutorsTrace[i].size(); j++) {
      canvas.setColor(new Color(110,33,27,(j*255)/NO_TRACE_DOTS));
      Coord actualCoord = persecutorsTrace[i].get(j);
      canvas.drawOval(actualCoord.getX() - radius_trace/2, actualCoord.getY() - radius_trace/2, radius_trace, radius_trace);
      
    }
  }

  void addPlane(Persecuted plane) {
    this.persecuted = plane;
  }

  static int aux = 0;

  void addPersecutor(Persecutor persecutor) {
    this.persecutors[aux++] = persecutor;
  }

}
