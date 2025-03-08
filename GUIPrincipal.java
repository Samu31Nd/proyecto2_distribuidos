import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GUIPrincipal
 */
public class GUIPrincipal extends JFrame {
  public GUIPanel board;

  public GUIPrincipal(Resolution res, Persecuted plane, int noPersecutors) {
    setTitle("Persecución");
    setSize(res.toDimension());
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    board = new GUIPanel(res, noPersecutors);
    board.addPlane(plane);
    add(board);
  }
}

class GUIPanel extends JPanel implements ActionListener {
  Timer timer;
  Graphics2D canvas;
  Persecuted persecuted;
  Persecutor[] persecutors;
  public static int WIDTH, HEIGHT; 

  public GUIPanel(Resolution res, int noPersecutors) {
    WIDTH = res.toIntArr()[0] -76;
    HEIGHT = res.toIntArr()[1] - 97;
    persecutors = new Persecutor[noPersecutors];

    timer = new Timer(10, this);
    timer.start();
    this.setPreferredSize(res.toDimension());
    this.setFocusable(true);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    this.repaint();
  }

  public void paint(Graphics g) {
    super.paint(g);
    canvas = (Graphics2D)g;
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    drawBackground();
    reDrawPlane();
    reDrawPersecutors();
  }

  private void drawBackground() {
    canvas.setColor(Color.black);
    canvas.fillRect(0, 0, WIDTH, HEIGHT);
    canvas.setColor(Color.WHITE);
    drawStartAndEndPoint();
  }

  private void drawStartAndEndPoint(){
    canvas.setColor(Color.BLUE);
    Coord startPoint = this.persecuted.getStartPoint();
    canvas.fillOval(startPoint.getX(), startPoint.getY(), 25, 25);
    Coord endPoint = this.persecuted.getEndPoint();
    canvas.setColor(Color.RED);
    canvas.fillOval(endPoint.getX(), endPoint.getY(), 25, 25);
  }

  private void reDrawPlane(){
    canvas.setColor(Color.white);
    Coord actualPos = this.persecuted.getActualPosition();
    canvas.fillRect(actualPos.getX(),actualPos.getY(),10,10);
    this.persecuted.move();
    for (Persecutor p : persecutors){
      p.setNewReachingPoint(persecuted.getActualPosition());
    }
  }

  private void reDrawPersecutors(){
    for(Persecutor persecutor : persecutors){
      canvas.setColor(Color.WHITE);
      Coord actualPos = persecutor.getActualPosition();
      canvas.fillRect(actualPos.getX(),actualPos.getY(),20,20);
      persecutor.move();
    }
  }

  void addPlane(Persecuted plane){
    this.persecuted = plane;
  }

  static int aux = 0;
  void addPersecutor(Persecutor persecutor){
    this.persecutors[aux++] = persecutor;
  }

}
