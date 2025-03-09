import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndProgramGUI {

    public EndProgramGUI(boolean isLost, boolean timeOut, double distance) {
        JFrame frame = new JFrame("Fin del Juego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        String message;
        if (timeOut){
            message = "Han pasado 2 minutos y nadie ha ganado :(";
        } else {
            message = isLost ? "El avión fue alcanzado :("
            : "¡El avión llegó a su destino!";
        }

        message += "\nDistancia recorrida: " + distance;
        JLabel label = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>", SwingConstants.CENTER);
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }
}
