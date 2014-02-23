package bradleyross.playpen;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
// import java.awt.GridBagLayout;
// import java.awt.GridBagConstraints;
import java.awt.Graphics;
import java.awt.Color;
/**
 * Allow the user to specify a graphics file and display it
 * in a panel.
 * @author Bradley Ross
 *
 */
public class ShowImage {
	protected File file;
	protected BufferedImage image;
	protected JFrame frame;
	protected JPanel panel;
	protected WritableRaster raster;
	@SuppressWarnings("serial")
	protected class PictureFrame extends JPanel {
		public PictureFrame() {
			super();
			this.setSize(300,300);
			this.setBackground(Color.RED);
		}
		public void paint(Graphics g) {
			if (image != null) {
				g.drawImage(image, 0, 0, null);
			} 
		}
	}
	protected void run() throws Exception {
		JFileChooser chooser = new JFileChooser();
		System.out.println("Opening dialog");
		int retval = chooser.showOpenDialog(panel);
		if (retval == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		}
		frame = new JFrame();
		frame.setSize(300, 500);
		panel = new PictureFrame();
		frame.add(panel);
		frame.setVisible(true);


		FileInputStream input = new FileInputStream(file);
		image = ImageIO.read(input);
		raster = image.getRaster();
		System.out.println(Integer.toString(image.getWidth()));
		System.out.println(Integer.toString(image.getHeight()));
		int[] pixel = new int[3];
		pixel = raster.getPixel(0, 0, pixel);
		display("pixel 0,0 ", pixel);http://www.hirezfox.com/km/co/d/20130429.html
		pixel = raster.getPixel(1, 0, pixel);
		display("pixel 1,0 ", pixel);
		pixel = raster.getPixel(0, 1, pixel);
		display("pixel 0,1 ", pixel);
		pixel = raster.getPixel(64, 64, pixel);
		display("pixel 64,64 ", pixel);
		panel.repaint();

	}
	protected void display(String text, int[] value) {
		StringBuilder builder = new StringBuilder(text + " ");
		for (int i = 0; i < value.length; i++) {
			builder.append(Integer.toString(value[i]) + " ");
		}
		System.out.println(builder.toString());
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ShowImage instance = new ShowImage();
			instance.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
