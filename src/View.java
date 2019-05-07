import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class View extends JFrame implements ActionListener{

    private class MyPanel extends JPanel {
        private final Controller controller;

        MyPanel(Controller c) {
            controller = c;
            addMouseListener(c);
        }

        public void paintComponent(Graphics g) {
            controller.update(g);
            revalidate();
        }
    }

    public View(Controller c) {
        // Define the viewing window and viewing properties
        setTitle(Definitions.gameName);
        setSize(1500, 800);
        getContentPane().add(new MyPanel(c));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        addKeyListener(c);

        // Populate the Definitions class dynamically with canvas info
        Definitions.setWorldHeight(getContentPane().getSize().height);
        Definitions.setWorldWidth(getContentPane().getSize().width);

        // Populate the menu bar
        JMenuBar mainMenu = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem pause = new JMenuItem("Pause/Resume");
        pause.addActionListener(c);
        pause.setAccelerator(KeyStroke.getKeyStroke('p'));

        JMenuItem save = new JMenuItem("Save Game");
        save.addActionListener(c);

        JMenuItem load = new JMenuItem("Load Game");
        load.addActionListener(c);


        JMenuItem instructions = new JMenuItem("Instructions");
        instructions.addActionListener(c);
        instructions.setAccelerator(KeyStroke.getKeyStroke('i'));

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(c);

        optionsMenu.add(pause);
        optionsMenu.add(save);
        optionsMenu.add(load);
        optionsMenu.add(instructions);
        optionsMenu.add(exit);
        mainMenu.add(optionsMenu);
        this.setJMenuBar(mainMenu);
    }

    public void actionPerformed(ActionEvent evt) {
            repaint();
    }
}

