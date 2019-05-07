import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;


class Sprite implements Serializable {
	protected double xPos;
	protected double yPos;
	protected int imageWidth;
	protected int imageHeight;
	protected boolean collidable;
	protected transient Image image;
	protected String currentImagePath; // set on a game save

	public Sprite(String imgName, int width, int height) {
		if (imgName.contains(".gif")) {
			setGif(Definitions.assetDirectory + imgName);
		} else {
			setImage(Definitions.assetDirectory + imgName);
		}
		imageWidth = width;
		imageHeight = height;
		xPos = 0;
		yPos = 0;
		collidable = false; // by default, objects are not collidable unless specified

		currentImagePath = imgName;
	}

	public Sprite(String imgName, int width, int height, boolean collidable) {
		this(imgName, width, height);
		this.collidable = collidable;
	}

	public double getX() {	return xPos; }
	public double getY() {	return yPos; }
	public int getHeight() { return imageHeight; }
	public int getWidth() { return imageWidth; }
	public Sprite setX(double x) { xPos = x; return this;}
	public Sprite setY(double y) { yPos = y; return this;}
	public boolean isCollidable() {
		return collidable;
	}
	public void setImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ioe) {
            System.out.println("Unable to load image file.");
        }
	}
	public void setGif(String imagePath) {
		image = new ImageIcon(imagePath).getImage();
	}
	public void update(Graphics g) {
		g.drawImage(image, (int)xPos, (int)yPos, imageWidth, imageHeight, null);
	}

	public boolean overlaps(Sprite s) {
		return (
				this.xPos < s.xPos + imageWidth && this.xPos + imageWidth > s.xPos &&
						this.yPos < s.yPos + imageHeight && this.yPos + imageHeight > s.yPos

				);
	}

	public void saveImage() {
		// base sprites don't need to do anything extra
	}

	public void loadImage() {
		if (currentImagePath.contains(".gif")) {
			setGif(Definitions.assetDirectory + currentImagePath);
		} else {
			setImage(Definitions.assetDirectory + currentImagePath);
		}
	}
}