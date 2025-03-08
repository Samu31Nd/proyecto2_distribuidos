import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.text.NumberFormatter;

enum Resolution {
  _1280x720 {
    @Override
    public String toString() {
      return"1280x720";
    }

    public Dimension toDimension() {
      return new Dimension(1280,720);
    }

  public int[] toIntArr(){
      return new int[]{1280,720}; 
    }
  },
  _800x600 {
    @Override
    public String toString() {
      return"800x600";
    }

    public Dimension toDimension() {
      return new Dimension(800,600);
    }
    public int[] toIntArr(){
      return new int[]{800,600}; 
    }
  };

  Dimension toDimension() {
    return new Dimension(1280, 720);
  }

  int[] toIntArr() {
    return new int[] { 1280, 720 };
  }
}

public class MainMenuPanel extends JFrame {
  static boolean exit = true;

  private MainMenuOptions mensaje;

  public MainMenuOptions getMensaje() {
    return mensaje;
  }

  public MainMenuPanel() {
    setTitle("Menú Principal");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel infoLabel = new JLabel("Haz la selección de opciones para empezar");
    infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(infoLabel);

    JLabel resLabel = new JLabel("Resolución:");
    resLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(resLabel);

    Resolution[] availableResolutions = { Resolution._1280x720, Resolution._800x600 };
    JComboBox<Resolution> resolutionBox = new JComboBox<>(availableResolutions);
    resolutionBox.setMaximumSize(new Dimension(200, 30));
    panel.add(resolutionBox);

    JLabel noPlLabel = new JLabel("Número de aviones:");
    noPlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(noPlLabel);

    SpinnerNumberModel model = new SpinnerNumberModel(2, 1, 7, 1);
    JSpinner numSelector = new JSpinner(model);
    numSelector.setMaximumSize(new Dimension(200, 30));
    panel.add(numSelector);

    JLabel speedLabel = new JLabel("Velocidad:");
    speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(speedLabel);

    NumberFormat format = new DecimalFormat("#.###");
    format.setGroupingUsed(false);

    NumberFormatter formatter = new NumberFormatter(format);
    formatter.setValueClass(Double.class);
    formatter.setMinimum(0.0);
    formatter.setAllowsInvalid(false);
    formatter.setCommitsOnValidEdit(true);

    JFormattedTextField speedField = new JFormattedTextField(formatter);
    speedField.setText("1");
    speedField.setMaximumSize(new Dimension(200, 30));
    panel.add(speedField);

    // Botón de inicio
    JButton startButton = new JButton("Start");
    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(startButton);
    startButton.addActionListener(e -> {
      exit = false;
      mensaje = new MainMenuOptions(exit, (double) speedField.getValue(), (int) numSelector.getValue(),
          (Resolution) resolutionBox.getSelectedItem());
      dispose();
    });

    add(panel, BorderLayout.CENTER);
    pack();
    setVisible(true);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        mensaje = new MainMenuOptions(exit, (double) speedField.getValue(), (int) numSelector.getValue(),
            (Resolution) resolutionBox.getSelectedItem());
      }
    });
  }
}

class MainMenuOptions {
  boolean exit;
  double velocity;
  int noPersecutors;
  Resolution selectedResolution;

  public MainMenuOptions(boolean e, double v, int no_p, Resolution res) {
    this.exit = e;
    this.velocity = v;
    this.noPersecutors = no_p;
    this.selectedResolution = res;
  }

  @Override
  public String toString() {
    return "Exit: " + exit + "\n" +
        "Velocidad: " + velocity + "\n" +
        "Número de aviones: " + noPersecutors + "\n" +
        "Resolución: " + selectedResolution;
  }
}
