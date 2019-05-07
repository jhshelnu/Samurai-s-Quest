public class Platform extends Sprite {
    public Platform(int x, int y) {
        super("platform.png", 140, 30, true);
        xPos = x;
        yPos = y;
    }

    public Platform(int x, int y, int width) {
        super("platform.png", width, 30, true);
        xPos = x;
        yPos = y;
    }
}
