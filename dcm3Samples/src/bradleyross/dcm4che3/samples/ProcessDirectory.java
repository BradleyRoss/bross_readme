package bradleyross.dcm4che3.samples;
import bradleyross.library.helpers.DirWalker;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
// import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Process the Dicom files in a directory.
 * @author Bradley Ross
 *
 */
public class ProcessDirectory {
	protected DicomIndex index = new DicomIndex();
	/**
	 * Main window of user interface.
	 */
	protected JFrame frame;
	/**
	 * Panel that will hold additional elements.
	 */
	protected JPanel buttons;
	/**
	 * Menu bar for main window.
	 */
	protected JMenuBar menuBar;
	/**
	 * Action listener listening for action events from
	 * the superclass.
	 */
	protected ActionListener basicListener;
	/**
	 * Directory from which files will be read.
	 */
	protected File directory;
	/**
	 * Name of directory from whih files will be read.
	 */
	protected String directoryName;
	/**
	 * Listens for the primary action items.
	 * @author Bradley Ross
	 *
	 */
	protected class BasicListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String command = event.getActionCommand();
			if (command.equalsIgnoreCase("READALL")) {
				readDirectory();
			} else if (command.equalsIgnoreCase("EXIT")) {
				exitProgram();
			} else {
				log (command + " is not recognized command");
			}
		}
	}
	/**
	 * Tests whether the file is a Dicom object.
	 * @author Bradley Ross
	 *
	 */
	protected class IsDicom implements FileFilter {
		public boolean accept(File value) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(value);
			} catch (FileNotFoundException e) {
				return false;
			}
			try {
				long length = in.skip(128l);
				if (length != 128l) {
					in.close();
					return false;
				}
				byte[] read = new byte[4];
				length = in.read(read);
				if (length != 4l) {
					in.close();
					return false;
				}
				String test = new String(read);
				if (test.equalsIgnoreCase("DICM")) {
					in.close();
					return true;
				} else {
					in.close();
					return false;
				}
			} catch (IOException e) {
				log("Error reading file");
				return false;
			} 
		}
	}
	protected void log(String value) {
		System.out.println(value);
	}
	/**
	 * Lay out the elements for the main window
	 */
	protected void buildFrame() {
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		frame = new JFrame();
		menuBar = frame.getJMenuBar();
		if (menuBar == null) {
			menuBar = new JMenuBar();
			frame.setJMenuBar(menuBar);
		}
		basicListener = new BasicListener();
		JMenu menu = new JMenu("File");
		JMenuItem readDirectory = new JMenuItem("Read Directory");
		readDirectory.addActionListener(basicListener);
		readDirectory.setActionCommand("READALL");
		menu.add(readDirectory);
		JMenuItem exitProgram = new JMenuItem("ExitProgram");
		exitProgram.addActionListener(basicListener);
		exitProgram.setActionCommand("EXIT");
		menu.add(exitProgram);
		menuBar.add(menu);
		JPanel panel = new JPanel();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 3.0f;
		panel.setLayout(layout);
		extraButtonItems(panel, layout);
		c.weighty = 2.0f;
		layout.setConstraints(panel, c);
		frame.add(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
	/**
	 * Can be overridden to add additional items to the main panel in the
	 * user interface.
	 * @param panel JPanel object to which items are added
	 * @param layout layout for panel
	 */
	public void extraButtonItems(JPanel panel, GridBagLayout layout) { ; }
	/**
	 * Add handlers for additional action events from user interface items.
	 * @param event action event
	 */
	public void actionPerformed(ActionEvent event) { ; }
	/**
	 * Exit the application and close the main window.
	 */
	protected void exitProgram() {
		frame.removeAll();
		frame.setVisible(false);
		frame.dispose();
	}
	/**
	 * Makes a list of the Dicom files in the directory.
	 */
	public void readDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = chooser.showOpenDialog(frame);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			directory = chooser.getSelectedFile();
			try {
				directoryName = directory.getCanonicalPath();
			} catch (IOException e) {
				log("Problem parsing directory name");
			}
			try {
				DirWalker walker = new DirWalker(directory, new IsDicom());
				while (walker.hasNext()) {
					File item = walker.next();
					// System.out.println(item.getName());
					index.add(item);
				}
				index.list();
			} catch (IOException e) {
				log("Error in directory walker");
			}
		}
	}
	/**
	 * Reset data structures to their original condition.
	 */
	public void reset() {

	}
	/**
	 * @param args not used
	 */
	public static void main(String[] args) {
		ProcessDirectory instance = new ProcessDirectory();
		instance.buildFrame();
	}
}
