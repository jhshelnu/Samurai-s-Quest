import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Skeleton extends Sprite {
    private static Player player;
    private boolean dead;
    private boolean attackingLeft;
    private boolean attackingRight;
    private int framesSinceDeath; // used so that the model doesnt remove the skeleton until a certain number of frames have been rendered, allowing for the death animation to play out
    private int framesSinceAttack; // used to keep track of how many times attacking animation has been rendered to the screen to prevent switching back to walk animation too soon or too late
    private boolean walkingRight;
    private double maxPos;
    private double minPos;
    private double xPosBeforeAttack;
    private double yPosBeforeAttack;
    private transient static ArrayList<Image> dying_right_sequence;
    private transient static ArrayList<Image> dying_left_sequence;
    private transient static ArrayList<Image> attacking_left_sequence;
    private transient static ArrayList<Image> attacking_right_sequence;

    static {
        dying_right_sequence = new ArrayList<>();
        dying_left_sequence = new ArrayList<>();
        attacking_right_sequence = new ArrayList<>();
        attacking_left_sequence = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            dying_right_sequence.add(new ImageIcon(String.format(Definitions.assetDirectory + "dying_right/%d.png", i)).getImage());
            dying_left_sequence.add(new ImageIcon(String.format(Definitions.assetDirectory + "dying_left/%d.png", i)).getImage());
        }

        for (int i = 0; i < 18; i++) {
            attacking_right_sequence.add(new ImageIcon(String.format(Definitions.assetDirectory + "attacking_right/%d.png", i)).getImage());
            attacking_left_sequence.add(new ImageIcon(String.format(Definitions.assetDirectory + "attacking_left/%d.png", i)).getImage());
        }
    }

    public static void setPlayer(Player p) {
        player = p;
    }

    public Skeleton(int x, int y, int roamDist) {
        super("npc_walking_right.gif", 63, 95);
        xPos = x;
        yPos = y;
        maxPos = xPos + roamDist;
        minPos = xPos - roamDist;
        dead = false;
        attackingLeft = false;
        attackingRight = false;
        walkingRight = true;
        framesSinceDeath = 0;
    }

    public void kill() {
        if (isAttacking()) {
            resetFromAttack();
        }
        image = walkingRight ? new ImageIcon(Definitions.assetDirectory + "dying_right/0.png").getImage() : new ImageIcon(Definitions.assetDirectory+ "dying_left/0.png").getImage();
        dead = true;
        imageWidth += 20;
        if (walkingRight) {
            xPos -= 20;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isAttacking() {
        return attackingLeft || attackingRight;
    }

    public int getFramesSinceDeath() {
        return framesSinceDeath;
    }

    public void incrementFramesSinceDeath() {
        if (walkingRight) {
            image = dying_right_sequence.get(framesSinceDeath / 3);
        } else {
            image = dying_left_sequence.get(framesSinceDeath / 3);
        }
        framesSinceDeath++;
    }

    private boolean playerOnSameLevel() {
        // Player's head is above skeleton's head by less than 60 pixels
        return player.getY() + Definitions.playerPaddingTop - yPos > 0 && player.getY() - yPos < 60;
    }

    private boolean playerInAttackDistance() {
        return player != null && playerOnSameLevel() && (Math.abs(player.getX() + player.getWidth() - Definitions.playerPadding - xPos) <= Definitions.skeletonAttackDist
                || Math.abs(player.getX() + Definitions.playerPadding - (xPos + imageWidth)) <= Definitions.skeletonAttackDist);
    }

    private void turnLeft() {
        walkingRight = false;
        image = new ImageIcon(Definitions.assetDirectory + "npc_walking_left.gif").getImage();
    }

    private void turnRight() {
        walkingRight = true;
        image = new ImageIcon(Definitions.assetDirectory + "npc_walking_right.gif").getImage();
    }

    private void walkRight() {
        xPos += 2;
    }

    private void walkLeft() {
        xPos -= 2;
    }

    private void attackLeft() {
        xPosBeforeAttack = xPos;
        yPosBeforeAttack = yPos;


        //attack here
        attackingLeft = true;
        imageWidth = 122;
        imageHeight = 115;

        xPos -= 50;
        yPos -= 20;

    }

    private void attackRight() {
        xPosBeforeAttack = xPos;
        yPosBeforeAttack = yPos;


        //attack here
        attackingRight = true;
        imageWidth = 122;
        imageHeight = 115;

        xPos -= 5;
        yPos -= 20;


    }

    private void resetFromAttack() {
        imageWidth = 63;
        imageHeight = 95;
        xPos = xPosBeforeAttack;
        yPos = yPosBeforeAttack;

        attackingRight = false;
        attackingLeft = false;
        if (walkingRight) {
            image = new ImageIcon(Definitions.assetDirectory + "npc_walking_right.gif").getImage();
        } else {
            image = new ImageIcon(Definitions.assetDirectory + "npc_walking_left.gif").getImage();
        }
    }

    public void update(Graphics g) {
        if (!dead) {
            if (isAttacking()) {
                if (framesSinceAttack == Definitions.skeletonAttackLength) {
                    resetFromAttack(); update(g); return;
                } else {
                    if (attackingLeft) {
                        image = attacking_left_sequence.get(framesSinceAttack / 3);
                    } else {
                        image = attacking_right_sequence.get(framesSinceAttack / 3);
                    }
                    framesSinceAttack++;
                }

                if (framesSinceAttack == 23 && overlaps(player)) {
                    player.kill();
                }
            } else if (playerInAttackDistance()) {
                if (player.getX() <= xPos) {
                    attackLeft();
                } else {
                    attackRight();
                }
                framesSinceAttack = 0;
            } else if (walkingRight && xPos < maxPos) {
                walkRight();
            } else if (walkingRight && xPos >= maxPos) {
                turnLeft();
                walkLeft();
            } else if (!walkingRight && xPos > minPos) {
                walkLeft();
            } else if (!walkingRight && xPos <= minPos) {
                turnRight();
                walkRight();
            }
        }
        super.update(g);
    }

    @Override
    public void loadImage() {
        if (dead) {
            if (walkingRight) {
                image = dying_right_sequence.get(framesSinceDeath / 3);
            } else {
                image = dying_left_sequence.get(framesSinceDeath / 3);
            }
        } else if (attackingRight) {
            image = attacking_right_sequence.get(framesSinceAttack / 3);
        } else if (attackingLeft) {
            image = attacking_left_sequence.get(framesSinceAttack / 3);
        } else if (walkingRight) {
            image = new ImageIcon(Definitions.assetDirectory + "npc_walking_right.gif").getImage();
        } else {
            image = new ImageIcon(Definitions.assetDirectory + "npc_walking_left.gif").getImage();
        }
    }
}
