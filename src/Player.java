import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Sprite {
    private double prev_X;
    private double prev_Y;
    private static final int accel = 3;
    private int velocity;
    private Definitions.VelocityState velocityState;
    private boolean inAir;
    private boolean hasDoubleJump;
    private boolean facingRight;
    private boolean dead;
    private transient ArrayList<Image> player_sprites;
    private int imageIndex;

    public void loadImages() {
        player_sprites = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            try {
                player_sprites.add(ImageIO.read(new File(String.format(Definitions.assetDirectory + "player%d.png", i))));
            } catch (IOException ioe) {
                System.out.println("Unable to load image file.");
            }
        }
    }

    public Player() {
        super("player0.png", Definitions.playerDimension, Definitions.playerDimension);
        velocity = 0;
        inAir = true;
        hasDoubleJump = true;
        facingRight = true;
        dead = false;
        xPos = 90;
        yPos = 530;
        player_sprites = new ArrayList<>();
        imageIndex = 0;
        loadImages();
    }

    public int getVelocity() {
        return this.velocity;
    }

    public void setVelocity(int vel) {velocity = vel;}

    public boolean isAirborne() {
        return inAir;
    }

    public boolean hasDoubleJump() {
        return hasDoubleJump;
    }

    public void setDoubleJump(boolean doubleJump) {
        hasDoubleJump = doubleJump;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setAirborne(boolean inAir) {
        this.inAir = inAir;

        if (inAir) {
            if (velocityState == Definitions.VelocityState.LEFT) {
                image = player_sprites.get(5);
                facingRight = false;
            } else if (velocityState == Definitions.VelocityState.RIGHT) {
                image = player_sprites.get(2);
                facingRight = true;
            }
        } else {
            if (velocityState == Definitions.VelocityState.LEFT) {
                image = player_sprites.get(4);
                facingRight = false;
            } else if (velocityState == Definitions.VelocityState.RIGHT) {
                image = player_sprites.get(1);
                facingRight = true;
            }
        }
    }

    public void jump() {
        if (hasDoubleJump) {
            if (inAir) {
                hasDoubleJump = false;
            }
            velocity = -22;
            inAir = true;
            image = facingRight ? player_sprites.get(2) : player_sprites.get(5);
        }
    }

    public void setDirection(Definitions.VelocityState state) {
        if (state == Definitions.VelocityState.LEFT) {
            if (inAir) {
                image = player_sprites.get(5);
            } else {
                image = player_sprites.get(4);
            }
            facingRight = false;
        } else if (state == Definitions.VelocityState.RIGHT) {
            if (inAir) {
                image = player_sprites.get(2);
            } else {
                image = player_sprites.get(1);
            }
            facingRight = true;
        } else {
            // STILL
            if (velocityState == Definitions.VelocityState.LEFT) {
                image = player_sprites.get(3);
                facingRight = false;
            } else if (velocityState == Definitions.VelocityState.RIGHT) {
                image = player_sprites.get(0);
                facingRight = true;
            }
        }

        velocityState = state;
    }

    public void setStandingRightImage() {
        image = player_sprites.get(0);
    }

    public void setStandingLeftImage() {
        image = player_sprites.get(3);
    }

    public void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public Definitions.VelocityState getVelocityState() {
        return velocityState;
    }

    public double getPrev_X() {
        return prev_X;
    }

    public double getPrev_Y() {
        return prev_Y;
    }

    public void updateInfo() {
        // store previous coordinates for collisions logic
        prev_X = xPos;
        prev_Y = yPos;

        // update player info based on world physics
        if (velocityState == Definitions.VelocityState.LEFT) {
            xPos -= 8;
        } else if (velocityState == Definitions.VelocityState.RIGHT) {
            xPos += 8;
        }
        yPos += velocity;
        velocity += accel;

        // Keep the player from falling out of the world
//        if (yPos + imageHeight >= Definitions.worldHeight) {
//            yPos = Definitions.worldHeight - imageHeight;
//        }
    }

    public boolean overlaps(Sprite s) {
        if (xPos + imageWidth - Definitions.playerPadding < s.xPos) {return false;}
        if (xPos + Definitions.playerPadding > s.xPos + s.getWidth()) {return false;}
        if (yPos + imageHeight < s.getY()) {return false;}
        if (yPos + Definitions.playerPaddingTop > s.getY() + s.getHeight()) {return false;}
        return true;
    }

    @Override
    public void saveImage() {
        imageIndex = player_sprites.indexOf(image);
    }

    @Override
    public void loadImage() {
        loadImages();
        image = player_sprites.get(imageIndex);
    }
}
