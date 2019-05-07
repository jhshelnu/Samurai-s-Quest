import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import static java.awt.event.KeyEvent.VK_ENTER;

class Controller implements MouseListener, KeyListener, ActionListener {
    private Model model;
    private final View view;
    private final Timer timer;
    private boolean paused;

    private Controller() { // only the main() method in Controller can create a controller!
        paused = false;
        view = new View(this);
        model = new Model();
        timer = new Timer(20, view);
        timer.start();
        displayInstructions();
    }

    public void update(Graphics g) {
        if (model!= null) { // on first call, model will be null, necessary to have view run first to populate Definitions class with world info
            if (model.gameIsOver()) {
                timer.stop();
                if (model.playerWon()) {
                    JOptionPane.showMessageDialog(view, Definitions.winningText, Definitions.gameName, JOptionPane.INFORMATION_MESSAGE, Definitions.shurikenIcon);
                } else {
                    JOptionPane.showMessageDialog(view, Definitions.losingText, Definitions.gameName, JOptionPane.INFORMATION_MESSAGE, Definitions.shurikenIcon);
                }
                model = new Model();
                timer.start();
            } else {
                model.update(g);
            }
        }
    }

    public void pauseResume() {
        if (!paused) {
            paused = true;
            timer.stop();
        } else {
            paused = false;
            timer.start();
        }
    }

    public void saveGame() {
//        try {
//            PrintWriter writer = new PrintWriter("save.txt", "UTF-8");
//        } catch (Exception e) {
//            System.out.println("Could not locate file save.txt, try making this file and relaunching.");
//            System.exit(1);
//        }

        model.saveImages();

        try {
            FileOutputStream fileStream =
                    new FileOutputStream("save.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileStream);
            out.writeObject(model);
            fileStream.close();
            out.close();
            //TODO: print success message
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadGame() {
        paused = true;
        timer.stop();

        try {
            FileInputStream fileStream = new FileInputStream("save.txt");
            ObjectInputStream in = new ObjectInputStream(fileStream);
            model = (Model) in.readObject();
            fileStream.close();
            in.close();
        } catch (IOException i) {
            i.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            System.exit(1);
        }

        model.loadImages();

        paused = false;
        timer.start();
    }

    public void displayInstructions() {
        JOptionPane.showMessageDialog(view, Definitions.instructionText, Definitions.gameName, JOptionPane.INFORMATION_MESSAGE, Definitions.shurikenIcon);
    }

    public void keyPressed(KeyEvent e) {
        if (!paused) {
            if (e.getKeyChar() == 'a') {
                model.setPlayerDirection(Definitions.VelocityState.LEFT);
            } else if (e.getKeyChar() == 'd') {
                model.setPlayerDirection(Definitions.VelocityState.RIGHT);
            } else if (e.getKeyChar() == ' ') {
                model.jumpPlayer();
            } else if (e.getKeyChar() == VK_ENTER) {
                model.throwShuriken();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'a') {
            model.setPlayerDirection(Definitions.VelocityState.STILL, 'a');
        } else if (e.getKeyChar() == 'd') {
            model.setPlayerDirection(Definitions.VelocityState.STILL, 'd');
        }
    }

    // unused KeyListener methods
    public void keyTyped(KeyEvent e) { }

    // unused MouseListener methods
    public void mousePressed(MouseEvent e) {
//		if (SwingUtilities.isLeftMouseButton(e)) {
//
//		} else if (SwingUtilities.isRightMouseButton(e))  {
//
//		}
//
//		view.repaint();
    }
    public void mouseReleased(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e) {    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand() == "Pause/Resume") {
            pauseResume();
        } else if (evt.getActionCommand().equals("Save Game")) {
            saveGame();
        } else if (evt.getActionCommand().equals("Load Game")) {
            loadGame();
        } else if (evt.getActionCommand().equals("Instructions")) {
            displayInstructions();
        } else if (evt.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        //  Use the following line to determine which directory your program
        //  is being executed from, since that is where the image files will
        //  need to be.
        //  System.out.println("cwd=" + System.getProperty("user.dir"));
        new Controller();
    }
}

