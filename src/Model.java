import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

class Model implements Serializable {
    private final ArrayList<Sprite> sprites;
    private Player player;
    private Sprite goblet;
    private boolean gameIsOver;
    private boolean won;

    Model() {
        gameIsOver = false;
        won = false;

        sprites = new ArrayList<>();
        sprites.add(new Sprite("background.png", 1500, 800));
        sprites.add(new Platform(90, 700));
        sprites.add(new Platform(325, 700, 350));
        sprites.add(new Platform(850, 700, 250));
        sprites.add(new Platform(1200, 600, 200));
        sprites.add(new Platform(700, 475, 400));
        sprites.add(new Platform(200, 425, 400));
        sprites.add(new Platform(150, 250, 200));
        sprites.add(new Platform(500, 200, 400));
        sprites.add(new Platform(1000, 200, 200));
        sprites.add(new Platform(1350, 125, 200));

        sprites.add(new Skeleton(875, 380, 180));
        sprites.add(new Skeleton(375, 330, 180));
        sprites.add(new Skeleton(675, 105, 180));


        goblet = new Sprite("goblet.png", 35, 35).setX(1450).setY(95);
        sprites.add(goblet);
        player = new Player();
        sprites.add(player);

        Skeleton.setPlayer(player); // give skeletons the player to attack
    }

    public void update(Graphics g) {
        Iterator<Sprite> iter = sprites.iterator();
        Sprite s;
        while(iter.hasNext()) {
            s = iter.next();

            if (s instanceof Player) {
                if (s.overlaps(goblet)) {
                    gameIsOver = true;
                    won = true;
                } else {
                    ((Player) s).updateInfo();
                    fixCollisions();
                    if (s.getY() >= Definitions.worldHeight || player.isDead()) {
                        gameIsOver = true;
                    }
                }


            } else if (s instanceof Shuriken) {
                if (((Shuriken) s).isExpired()) {
                    iter.remove();
                }


            } else if (s instanceof Skeleton) {
                if (((Skeleton) s).isDead()) {
                    if (((Skeleton) s).getFramesSinceDeath() == Definitions.skeletonDeathLength) {
                        iter.remove();
                    } else {
                        ((Skeleton) s).incrementFramesSinceDeath();
                    }
                } else {
                    for (Sprite shuriken : sprites) {
                        if (shuriken instanceof Shuriken && s.overlaps(shuriken)) {
                            ((Skeleton) s).kill();
                            ((Skeleton) s).incrementFramesSinceDeath();
                            ((Shuriken) shuriken).removeShuriken();
                        }
                    }
                }
            }
            s.update(g);
        }
    }

    public void saveImages() {
        player.saveImage();
    }

    public void loadImages() {
        for (Sprite s : sprites) {
            s.loadImage();
        }
    }

    public boolean gameIsOver() {
        return gameIsOver;
    }

    public boolean playerWon() {
        return won;
    }

    public void jumpPlayer() {
        player.jump();
    }

    public void setPlayerDirection(Definitions.VelocityState state) {
        player.setDirection(state);
    }

    public void setPlayerDirection(Definitions.VelocityState state, char keyReleased) {
        if (state == Definitions.VelocityState.STILL && player.getVelocityState() == Definitions.VelocityState.LEFT && keyReleased == 'd') {return;}
        if (state == Definitions.VelocityState.STILL && player.getVelocityState() == Definitions.VelocityState.RIGHT && keyReleased == 'a') {return;}
        setPlayerDirection(state);
    }

    private void fixCollisions() {
        for (Sprite s : sprites) {
            if (!(s instanceof Player) && s.isCollidable() && player.overlaps(s)) { // then there is a collision that needs to be resolved
                if (player.getPrev_X() + player.getWidth() - Definitions.playerPadding < s.getX()) {
                    player.setX(s.getX() - player.getWidth() + Definitions.playerPadding - 1);
                } else if (player.getPrev_X() + Definitions.playerPadding > s.getX() + s.getWidth()) {
                    player.setX(s.getX() + s.getWidth() - Definitions.playerPadding + 1);
                } else if (player.getPrev_Y() + player.getHeight() < s.getY()) {
                    if (player.isFacingRight() && player.isAirborne()) {
                        player.setStandingRightImage();
                    } else if (!player.isFacingRight() && !player.isAirborne()){
                        player.setStandingLeftImage();
                    }
                    player.setY(s.getY() - player.getHeight() - 1);
                    player.setAirborne(false);
                    player.setDoubleJump(true);
                    if (player.getVelocity() > 0) {
                        player.setVelocity(0);
                    }
                } else if (player.getPrev_Y() + Definitions.playerPaddingTop > s.getY() + s.getHeight()) {
                    player.setY(s.getY() + s.getHeight() - Definitions.playerPaddingTop + 1);
                    player.setVelocity((int)(-0.6 * player.getVelocity()));
                }
            }
        }
    }

    public void throwShuriken() {
        if (Shuriken.getCount() < 3) {
            sprites.add(new Shuriken(player.getX(), player.getY(), player.isFacingRight()));
        }
    }
}

