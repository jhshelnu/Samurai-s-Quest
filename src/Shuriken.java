import java.awt.*;

public class Shuriken extends Sprite {
    private static int count;
    private boolean throwRight;
    private boolean expired;
    private double initialX;

    public Shuriken(double playerX, double playerY, boolean throwRight) {
        super("shuriken.gif", 30, 30);
        xPos = playerX + ((throwRight) ? Definitions.playerPadding - 5 : Definitions.playerPadding);
        yPos = playerY + 40; // throw from arm height, not head height
        this.throwRight = throwRight;
        this.expired = false;
        this.initialX = xPos;
        count++;
    }

    public void update(Graphics g) {
        if (throwRight) {
            xPos += 20;
        } else {
            xPos -= 20;
        }

        super.update(g);

        if (Math.abs(xPos - initialX) >= Definitions.shurikenExpireDist) {
            removeShuriken();
        }
    }

    public static int getCount() {
        return count;
    }

    public boolean isExpired() {
        return expired;
    }

    public void removeShuriken() {
        count--;
        expired = true;
    }
}
