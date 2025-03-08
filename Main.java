import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Main
 */
public class Main {

  public static void main(String[] args) {
    //getOptionsFromMenu();
    getDefaultOptions();
  }

  /*
   * Just lauch with some default options to debug
   */
  public static void getDefaultOptions(){
    startSimulation(new MainMenuOptions(false, 1.0, 2, Resolution._1280x720));
  }

  public static void getOptionsFromMenu(){
    MainMenuPanel menu = new MainMenuPanel();
    menu.setVisible(true);

    menu.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        MainMenuOptions options = menu.getMensaje();
        System.out.println("Mensaje recibido:\n"+ options);
        if (options.exit) {
          return;
        }
        startSimulation(options);
      }
    });
  }

  static void startSimulation(MainMenuOptions options){
    int w = options.selectedResolution.toIntArr()[0] - 76 - 30;
    int h = options.selectedResolution.toIntArr()[1] - 97 - 30;
    GUIPrincipal gui = new GUIPrincipal(
      options.selectedResolution, 
      Persecuted.defaultPlane(w, h), 
      options.noPersecutors);
    gui.setVisible(true);

    for(int i = 0; i < options.noPersecutors; i++){
      gui.board.addPersecutor(new Persecutor(Coord.getRandCoordenada(w,h), options.velocity));
    }
    
  }
}
