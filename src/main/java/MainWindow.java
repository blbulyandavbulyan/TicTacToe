import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
    private final int gameFieldSize;
    final JButton[][] cells;
    private ResourceBundle menuRb = ResourceBundle.getBundle("locales/menu_text");
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setMinimumSize(new Dimension(300, 300));
        mw.setSize(mw.getSize());
        mw.pack();
        mw.setVisible(true);
    }
    public MainWindow(){
         gameFieldSize = 3;
         JPanel mainWindowPanel = new JPanel();
         mainWindowPanel.setLayout(new GridLayout(gameFieldSize, gameFieldSize));
         cells = new JButton[gameFieldSize][gameFieldSize];

         for(int i = 0; i < cells.length; i++){
             for (int j = 0; j < cells[i].length; j++) {
                 JButton cell = cells[i][j] = new JButton("X");
                 cell.setFocusPainted(false);

                 mainWindowPanel.add(cell);
             }
         }
         this.setContentPane(mainWindowPanel);
         this.setDefaultCloseOperation(EXIT_ON_CLOSE);
         JMenuBar jMenuBar = new JMenuBar();
         jMenuBar.add(createGameMenu());
         this.setJMenuBar(jMenuBar);
    }
    private JMenu createGameMenu(){
        JMenu jGameMenu = new JMenu(menuRb.getString("gameMenu"));
        JMenuItem jRestartGameMenuItem = new JMenuItem(menuRb.getString("restartGame"));
        jRestartGameMenuItem.addActionListener(e -> {
            System.out.println("Pressed");
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    JButton cell = cells[i][j];
                    cell.setText("");
                    cell.setEnabled(true);
                }
            }
        });
        jGameMenu.add(jRestartGameMenuItem);
        return jGameMenu;
    }
}
