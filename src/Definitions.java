import javax.swing.*;

public abstract class Definitions {
    public enum VelocityState {LEFT, RIGHT, STILL} // necessary to store movement state to allow for synchronous moving and jumping

    public static String gameName = "Samurai's Quest";
    public static String winningText = "\nCongratulations, you've managed to recover the ancient relic.\n\nWell, *one* of them....";
    public static String losingText = "Don't feel bad, thousands have tried and failed before you...";

    public static String instructionText = "Welcome Samurai! We need your help in recovering an ancient relic!\n" +
            "But watch out, as it's guarded by knights of the undead....\n\n" +
            "How to play:\n" +
            "A - move left\nD - move right\nSpace - Jump (double tap in air to double jump)\nEnter - Throw a shuriken (up to 3 can be thrown at once!)\n" +
            "p - Pause/Resume the game\ni - Display these instructions\n\n" +
            "Good luck and hurry, for time is running out...";

    public static int worldHeight; // to be set by the View dynamically
    public static int worldWidth;  // to be set by the View dynamically to exclude application menu bar from the world height

    public static final int playerPadding = 40; // pixels between player and edge of player image
    public static final int playerPaddingTop = 20; // pixels between player and top edge of player image
    public static final int playerDimension = 105; // 105x105

    public static final int shurikenExpireDist = 200; // shurikens expire after travelling 400px

    public static final int skeletonAttackDist = 35;
    public static final int skeletonDeathLength = 45; // Model should keep a dead skeleton for this many frames before removing, so death animation can play out.
    public static final int skeletonAttackLength = 54; // Skeleton will be in attacking animation for 54 frames.

    public static final String assetDirectory = "assets/";
    public static final ImageIcon shurikenIcon = new ImageIcon(assetDirectory + "shuriken-icon.png");

    public static void setWorldHeight(int height) {
        worldHeight = height;
    }
    public static void setWorldWidth(int width) {
        worldWidth = width;
    }

    //TODO: fix player killed by skeleton detection
    //TODO: add save/load
}
