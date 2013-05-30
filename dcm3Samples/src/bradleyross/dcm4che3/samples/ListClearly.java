package bradleyross.dcm4che3.samples;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.io.IOException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.dcm4che.data.Attributes;
import org.dcm4che.data.ElementDictionary;
import org.dcm4che.data.Sequence;
import org.dcm4che.data.VR;
import org.dcm4che.io.DicomInputStream;
import org.dcm4che.util.TagUtils;
/**
 * Use DicomInputStream to fully read the dataset and then obtain information
 * on the objects and elements.
 * @author Bradley Ross
 *
 */
public class ListClearly implements ActionListener {
	protected JTextArea textArea;
	protected JFrame frame;
	protected JMenuBar menu;
	/**
	 * Dicom file.
	 */
	protected File dicomFile;
	/**
	 * Name of Dicom file.
	 */
	protected String fileName;
	protected Attributes dicomObject;
	/**
	 * Interface for test on tag values to see if they are in range.
	 * @author Bradley Ross
	 *
	 */
	protected interface TagSelector {
		public boolean accept(int value);
	}
	/**
	 * Test to see if tag is in range.
	 * <p>Can group numbers be greater than 0x7FFF?</p>
	 * @author Bradley Ross
	 *
	 */
	protected class GroupSelector implements TagSelector {
		public int minValue = 0x00000000;
		public int maxValue = 0x7FFFFFFF;
		public boolean accept(int value) {
			if (value >= minValue && value <= maxValue) {
				return true;
			} else {
				return false;
			}
		}
		public GroupSelector() {
			minValue = 0;
			maxValue = 0x7FFFFFFF;
		}
		public GroupSelector(int group) {
			if (group > 0x7FFF) {
				minValue = 0x7FFF0000;
			} else {
			minValue = group * 0x10000;
			}
			maxValue = minValue + 0xFFFF;
		}
		public GroupSelector(int start, int stop) {
			minValue = start;
			maxValue = stop;
		}
	}
	/**
	 * Class containing the values for a number of tags.
	 * @author Bradley Ross
	 *
	 */
	protected class Tag {
		ElementDictionary dictionary = ElementDictionary.getStandardElementDictionary();
		public int Manufacturer = dictionary.tagForKeyword("Manufacturer");
		public int PatientWeight = dictionary.tagForKeyword("PatientWeight");
		public int RadiopharmaceuticalInformationSequence =
				dictionary.tagForKeyword("RadiopharmaceuticalInformationSequence");
		public int RadiopharmaceuticalStartDateTime =
				dictionary.tagForKeyword("RadiopharmaceuticalStartDateTime");
		public int RadiopharmaceuticalStartTime =
				dictionary.tagForKeyword("RadiopharmaceuticalStartTime");
		public int RadionuclideTotalDose =
				dictionary.tagForKeyword("RadionuclideTotalDose");
		public int RadionuclideHalfLife = 
				dictionary.tagForKeyword("RadionuclideHalfLife");
		public int SeriesTime =
				dictionary.tagForKeyword("SeriesTime");
		public int AcquisitionTime =
				dictionary.tagForKeyword("AcquisitionTime");
		public int DecayCorrection =
				dictionary.tagForKeyword("DecayCorrection");
		public int Units =
				dictionary.tagForKeyword("Units");
		public int RescaleSlope = dictionary.tagForKeyword("RescaleSlope");
		public int RescaleIntercept = dictionary.tagForKeyword("RescaleIntercept");
		
	}
	protected Tag tag = new Tag();
	/**
	 * JPanel that contains the buttons in the user interface.
	 * <p>Subclasses of this class can be used to add additional 
	 *    capabilities.  If creating a subclass, override the
	 *    method {@link #run()} to use the new class.</p>
	 * @author Bradley Ross
	 *
	 */
	@SuppressWarnings("serial")
	protected class Buttons extends JPanel implements ActionListener {
		public Buttons() {
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
		JButton button1 = new JButton("List tags");
		button1.setActionCommand("LIST");
		button1.addActionListener(this);
		layout.setConstraints(button1, c);
		add(button1);
		JButton button2 = new JButton("SUV");
		button2.setActionCommand("SUV");
		button2.addActionListener(this);
		layout.setConstraints(button2, c);
		add(button2);
		}
		public void actionPerformed(ActionEvent event) {
			String command = event.getActionCommand();
			if (command.equalsIgnoreCase("LIST")) {
				processObject(dicomObject);
			} else if (command.equalsIgnoreCase("SUV")) {
				processSuv(dicomObject);
			} else {
				log(command + " is not recognized command");
			}
		}
	}
	protected void buildFrame() {
		buildFrame((JPanel) null);
	}
	/**
	 * Construct the menu bar and window contents for the user interface.
	 * @param panel JPanel to be inserted in the window
	 */
	protected void buildFrame(JPanel panel) {
		frame = new JFrame();
		menu = frame.getJMenuBar();
		if (menu == null) {
			menu = new JMenuBar();
			frame.setJMenuBar(menu);
		}
		JMenu file = new JMenu("File");
		JMenuItem readDicom = new JMenuItem("Read Dicom File");
		readDicom.addActionListener(this);
		readDicom.setActionCommand("READ");
		JMenuItem saveLog = new JMenuItem("Save Log File");
		saveLog.addActionListener(this);
		saveLog.setActionCommand("SAVE");
		JMenuItem clearWindow = new JMenuItem("Clear Window");
		clearWindow.addActionListener(this);
		clearWindow.setActionCommand("CLEAR");
		JMenuItem exitProgram = new JMenuItem("Exit Program");
		exitProgram.addActionListener(this);
		exitProgram.setActionCommand("EXIT");
		file.add(readDicom);
		file.add(saveLog);
		file.add(clearWindow);
		file.add(exitProgram);
		menu.add(file);
		textArea = new JTextArea();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		frame.setLayout(layout);
		JLabel label1 = new JLabel("Log");
		label1.setHorizontalTextPosition(SwingConstants.CENTER);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 4.0f;
		c.anchor = GridBagConstraints.CENTER;
		layout.setConstraints(label1, c);
		frame.add(label1);
		if (panel != null) {
			layout.setConstraints(panel, c);
			frame.add(panel);
		}
		c.weighty = 2.0f;
		JScrollPane scroller = new JScrollPane(textArea);
		layout.setConstraints(scroller, c);
		frame.add(scroller);
		frame.setSize(500, 400);
		frame.setLocation(100, 100);
		frame.setVisible(true);
	}
	/**
	 * Responds to actions involving elements of user interface.
	 * @param event action event
	 */
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equalsIgnoreCase("READ")) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnValue = chooser.showOpenDialog(frame);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				dicomFile = chooser.getSelectedFile();
				try {
					fileName = dicomFile.getCanonicalPath();
					log(fileName);
				} catch (IOException e) {
					System.out.println("Unable to read file name");
					e.printStackTrace();
				}
				System.out.println("Processing " + fileName);
				readFile();
			}
		} else if (command.equalsIgnoreCase("SAVE")) {
			JFileChooser chooser = new JFileChooser();
			int returnValue = chooser.showSaveDialog(frame);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				dicomFile = chooser.getSelectedFile();
				try {
					fileName = dicomFile.getCanonicalPath();
				} catch (IOException e) {
					System.out.println("Unable to read file name");
					e.printStackTrace();
				}
				System.out.println("Saving to " + fileName);	
			}
		} else if (command.equalsIgnoreCase("CLEAR")) {
			textArea.setText(new String());
		}else if (command.equalsIgnoreCase("EXIT")) {
		
			frame.setVisible(false);
			frame.removeAll();
			frame.dispose();
		} else {
			System.out.println(command + " is not recognized command");
		}
	}
	/**
	 * Number of levels of indentation to be applied when
	 * writing log messages.
	 */
	protected int indentLevel = 0;
	/**
	 * Write a log message.
	 * @param value text to be passed in message
	 */
	protected void log(String value) {
		for (int i = 0; i < indentLevel; i++) {
			textArea.append("     ");
		}
		textArea.append(value + "\r\n");
	}
	/**
	 * Read a file containing a Dicom dataset.
	 */
	protected void readFile() {
		try {

			DicomInputStream dis = new DicomInputStream(dicomFile);
			dicomObject = dis.readDataset(-1,  -1);
			dis.close();
			// processObject(dicomObject);
		} catch (IOException e) {
			log("Error while reading Dicom dataset");
		}
	}
	protected void processObject(Attributes dicomObject) {
		TagSelector use = new GroupSelector();
		processObject(dicomObject, use);
	}
	/**
	 * Print the contents of a Dicom object.
	 * @param dicomObject object whose contents are to be displayed
	 * <p>This method will recursively call itself to handle
	 *    embedded objects.</p>
	 */
	protected void processObject (Attributes dicomObject, TagSelector selector) {
		int[] tags = dicomObject.tags();
		// log("There are " + Integer.toString(tags.length) + " tags");
		for (int i = 0; i < tags.length; i++) {
			int tag = tags[i];
			if (selector != null && !selector.accept(tags[i])) { continue; }
			VR vr = dicomObject.getVR(tag);
			showValue(dicomObject, tag);
			if (vr == VR.SQ) {
				indentLevel++;
				Sequence seq = dicomObject.getSequence(tag);
				log("There are " + Integer.toString(seq.size()) + " items in the sequence");
				for (int i2 = 0; i2 < seq.size(); i2++) {
					log("Start of item " + Integer.toString(i2));
					indentLevel++;
					processObject(seq.get(i2));
					indentLevel--;
					log("End of item " + Integer.toString(i2));
				}
				indentLevel--;
			}
		}
	}
	/**
	 * Display some of the fields used in Standardized Uptake
	 * Values.
	 * <p>Current activity is in Becquerels per milliliter while
	 *    the injected dose is in Becquerels.</p>
	 *    <p>see <a href="http://www.clearcanvas.ca/dnn/Portals/0/ClearCanvasFiles/Documentation/UsersGuide/Personal/7_0/index.html?suv.htm" target="_blank">
	 *       ClearCanvas SUV discussion</a></p>
	 *    <p>Highest current radiation levels on MIM data is around 8000
	 *       Becquerels per milliliter.</p>
	 *    <p>See section C.8.9.2 PET Isotope Module in part 3 of the Dicom standards.</p>
	 * @param dicomObject Dicom SOP object that will be read
	 */
	public void processSuv(Attributes dicomObject) {
		log("*****  *****");
		log("Processing SUV fields");
		showValue(dicomObject, tag.Manufacturer);
		if (dicomObject.containsValue(tag.RadiopharmaceuticalInformationSequence)) {
			Sequence seq = dicomObject.getSequence(tag.RadiopharmaceuticalInformationSequence);
			Attributes contents = seq.get(0);
			log("Display of RadiopharmaceuticalInformationSequence");
			processObject(contents);
			log("Display of group 0x7053 - Philips only");
			processObject(dicomObject, new GroupSelector(0x7053));
			log("Display of group 0x0009 - GE only");
			processObject(dicomObject, new GroupSelector(0x0009));
			log("Display of individual tags");
			showValue(dicomObject, tag.Manufacturer);
			showValue(dicomObject, tag.RescaleIntercept);
			showValue(dicomObject, tag.RescaleSlope);
			showValue(contents, tag.RadiopharmaceuticalStartDateTime);
			showValue(contents, tag.RadiopharmaceuticalStartTime);
			showValue(dicomObject, tag.PatientWeight);
			showValue(dicomObject, tag.AcquisitionTime);
			showValue(dicomObject, tag.SeriesTime);
			showValue(contents, tag.RadionuclideTotalDose);
			showValue(contents, tag.RadionuclideHalfLife);
			showValue(dicomObject, tag.DecayCorrection);
			showValue(dicomObject, tag.Units);
			float bodyWeight; // g
			float startClock;
			float endClock;
			float halfLife;
			float totalDose; // Bq
			float attenuation;
			float halfLives;
			float suvMultiplier;
			log("Display calculations");
			if (dicomObject.containsValue(tag.AcquisitionTime)) {
				endClock = (float) dicomObject.getDate(tag.AcquisitionTime).getTime() / 1000.0f;
			} else if (dicomObject.containsValue(tag.SeriesTime)) {
				endClock = (float) dicomObject.getDate(tag.SeriesTime).getTime() / 1000.0f;
			} else {
				log("Unable to find end time");
				return;
			}
			if (contents.containsValue(tag.RadiopharmaceuticalStartTime) ) {
					startClock = (float) contents.getDate(tag.RadiopharmaceuticalStartTime).getTime() /
							1000.0f;
			} else {
				log("Unable to find start time");
				return;
			}
			log("Duration since radiation measurement is " + Float.toString(endClock - startClock) + " seconds");
			if (contents.containsValue(tag.RadionuclideHalfLife)) {
				halfLife = contents.getFloat(tag.RadionuclideHalfLife, 0.0f);
			} else {
				log ("Unable to find half life");
				return;
			}
			if (contents.containsValue(tag.RadionuclideTotalDose)) {
				totalDose = contents.getFloat(tag.RadionuclideTotalDose, 0.0f);
			} else {
				log ("Unable to find total dose");
				return;
			}
			log("Radionuclide total dose in Becquerels: " + Float.toString(totalDose));
			if (dicomObject.containsValue(tag.PatientWeight)) {
				bodyWeight = dicomObject.getFloat(tag.PatientWeight, 0.0f) * 1000.0f;
			} else {
				log ("Unable to find patient weight");
				return;
			}
			log("Body mass is " + Float.toString(bodyWeight) + " grams");
			halfLives = (endClock - startClock)/halfLife;
			log(Float.toString(halfLives) + " half lives");
			attenuation = (float) Math.pow(0.5d, halfLives);
			log("attenuation fraction = " + Float.toString(attenuation));
			suvMultiplier = bodyWeight / totalDose / attenuation;
			log("SUV multiplier is " + Float.toString(suvMultiplier));
		}
	}
	/**
	 * Write information about a tag.
	 * @param dicomObject Dicom object containing the tag
	 * @param tag integer value of the tag to be displayed
	 */
	public void showValue(Attributes dicomObject, int tag) {
		String name;
		if (TagUtils.isPrivateGroup(tag)) {
			name = " - private group - ";
		} else {
		name = ElementDictionary.keywordOf(tag, null);
		}
		VR vr = dicomObject.getVR(tag);
		if (!dicomObject.containsValue(tag)) {
			log(TagUtils.toString(tag) + " " + name + " not found");
		} else if (vr == VR.LT || vr == VR.UI || vr == VR.ST ||
				vr == VR.LO || vr == VR.SH || vr == VR.UT || vr == VR.CS) {
			String value = dicomObject.getString(tag);
			StringBuilder sb = new StringBuilder();
			sb.append(TagUtils.toString(tag) + " " + name + " " + vr.toString() + " :: ");
			if (value.length() < 60) {
				sb.append(value);
			} else {
				sb.append(value.substring(0, 60));
			}
			log(sb.toString());
		} else if (vr == VR.DS || vr == VR.FD || vr == VR.FL) {
			StringBuilder sb = new StringBuilder();
			sb.append(TagUtils.toString(tag) + " " + name);
			float[] values = dicomObject.getFloats(tag);
			if (values.length > 1) {
				sb.append(" - " + Integer.toString(values.length) + " values :: ");
			} else {
				sb.append(" :: ");
			}
			sb.append(Float.toString(values[0]));
			log(sb.toString());
		} else if (vr == VR.US || vr == VR.UL || vr == VR.SS || vr == VR.SL) {
			StringBuilder sb = new StringBuilder();
			sb.append(TagUtils.toString(tag) + " " + name);
			int[] values = dicomObject.getInts(tag);
			if (values.length > 1) {
				sb.append(" - " + Integer.toString(values.length) + " values - ");
			} else {
				sb.append(" - ");
			}
			sb.append(Integer.toString(values[0]));
			log(sb.toString());
		} else if (vr == VR.TM) {
			StringBuilder sb = new StringBuilder();
			sb.append(TagUtils.toString(tag) + " " + name + " :: ");
			float value = (float) dicomObject.getDate(tag).getTime() / 1000.0f;
			sb.append(Float.toString(value));
			log(sb.toString());
		} else if (vr == VR.DA || vr == VR.DT) {
			StringBuilder sb = new StringBuilder();
			sb.append(TagUtils.toString(tag) + " " + name + " ");
			Date value = dicomObject.getDate(tag);
			sb.append(value.toString());
			log(sb.toString());
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(TagUtils.toString(tag) + " " + name + " " + vr.toString() + " ");
			log(sb.toString());
		}
	}
	/**
	 * This method calls {@link #buildFrame(JPanel)} to 
	 * populate the user interface.
	 * <p>It can be overridden to provide different capabilities.</p>
	 */
	public void run() {
		JPanel panel = new Buttons();
		buildFrame(panel);
	}
	/**
	 * Main driver.
	 * @param args not used
	 */
	public static void main(String[] args) {	
		ListClearly instance = new ListClearly();
		instance.run();
	}
}
