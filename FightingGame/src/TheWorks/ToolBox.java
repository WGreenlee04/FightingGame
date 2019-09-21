package TheWorks;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class ToolBox {

	private ImageObserver I;

	public ToolBox(ImageObserver i) {
		I = i;
	}

	// IMAGE TOOLBOX START
	public Image rotateObject(Image image, int degrees) {
		BufferedImage bufferedImage = toBufferedImage(image);
		AffineTransform tx = new AffineTransform();
		tx.rotate(Math.toRadians(degrees), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return (Image) op.filter(bufferedImage, null);
	}

	public Image loadObject(String Dir) {

		ImageIcon iIcon = new ImageIcon(Dir);
		Image i = iIcon.getImage();

		return i;
	}

	public Image lightenObject(Image image, Player player) {
		image = new ImageIcon(player.getImageDir()).getImage();
		image = scalePlayer(image, player);
		return image;
	}

	public Image darkenObject(Image image, Player player) {
		image = new ImageIcon(player.getDarkImageDir()).getImage();
		image = scalePlayer(image, player);
		return image;
	}

	public Image flipObject(Image image) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return (Image) op.filter(toBufferedImage(image), null);
	}

	public Image scaleObject(Image image, int width, int height) {
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	public Image scalePlayer(Image image, Player player) {
		return image.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_SMOOTH);

	}

	public BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
	// IMAGE TOOLBOX END
}
