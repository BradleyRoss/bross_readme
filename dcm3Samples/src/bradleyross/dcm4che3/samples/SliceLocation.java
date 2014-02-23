package bradleyross.dcm4che3.samples;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;
import org.dcm4che.data.ElementDictionary;
import bradleyross.dcm4che3.samples.DicomIndex;
/**
 * List the slice locations for the objects in a directory.
 * @author Bradley Ross
 *
 */
public class SliceLocation extends ProcessDirectory implements ActionListener {
	public int sliceLocation = ElementDictionary.getStandardElementDictionary().tagForKeyword("SliceLocation");
	public void extraButtonItems(JPanel panel, GridBagLayout layout) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 3.0f;
		JButton slices = new JButton("Get Slices");
		layout.setConstraints(slices, c);
		slices.setActionCommand("SLICES");
		slices.addActionListener(this);
		panel.add(slices);
	}
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equalsIgnoreCase("SLICES")) {
			TreeMap<Float,DicomIndex.Instance> sorting = new TreeMap<Float,DicomIndex.Instance>();
			TreeMap<String,DicomIndex.Instance>uids = new TreeMap<String,DicomIndex.Instance>(); 
			for (DicomIndex.Instance item : index.instanceIterator()) {
				sorting.put(new Float(item.getSliceLocation()), item);
				uids.put(item.getSOPInstanceUID(), item);
			}
			log("Instances sorted by slice location");
			for(DicomIndex.Instance item : sorting.values()) {
				try {
					log ("*****");
					log(item.getFile().getCanonicalPath());
					log("SOPInstanceUID: " + item.getSOPInstanceUID());
					log("SOPClassUID: " + item.getSOPClassUID());
					log("Slice Location:  " + Float.toString(item.getSliceLocation()) + " mm");
					log("AcquisitionNumber: " + Integer.toString(item.getAcquisitionNumber()));
					Date temp = item.getAcquisitionTime();
					if (temp != null) {
						log("AcquistionTime: " + temp.toString());
					}

				} catch (IOException e) {
					log("Error processing item");
				}
			}
			log("Sorted list of SOPInstanceUID values");
			for (DicomIndex.Instance item : uids.values()) {
				log(item.getSOPInstanceUID());
			}
		} else if (command.equalsIgnoreCase("BIGLIST")) {

		}

	}
	/**
	 * Main driver.
	 * @param args not used
	 */
	public static void main(String[] args) {
		SliceLocation instance = new SliceLocation();
		instance.buildFrame();
	}

}
