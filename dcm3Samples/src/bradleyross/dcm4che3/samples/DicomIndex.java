package bradleyross.dcm4che3.samples;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import org.dcm4che.data.Attributes;
import org.dcm4che.data.ElementDictionary;
import org.dcm4che.io.DicomInputStream;
/**
 * Create a structure for indexing the studies, series, and SOP instances.
 * @author Bradley Ross
 *
 */
public class DicomIndex {
	/**
	 * Provides the integer values for tags.
	 * <p>By determining the integer values for tags within this class, it is
	 *    insured that the names for the tags are correctly spelled.</p>
	 * @author Bradley Ross
	 *
	 */
	protected class Tags {
		ElementDictionary dictionary = ElementDictionary.getStandardElementDictionary();
		public int StudyInstanceUID = dictionary.tagForKeyword("StudyInstanceUID");
		public int SeriesInstanceUID = dictionary.tagForKeyword("SeriesInstanceUID");
		public int SOPInstanceUID = dictionary.tagForKeyword("SOPInstanceUID");
		public int FrameOfReferenceUID = dictionary.tagForKeyword("FrameOfReferenceUID");
		public int SOPClassUID = dictionary.tagForKeyword("SOPClassUID");
	}
	protected Tags Tag;
	protected class Study {
		protected String studyUID;
		protected HashSet<String> seriesList;
		public Study(Instance instance) {
			seriesList = new HashSet<String>();
			studyUID = instance.getStudyInstanceUID();
			seriesList = new HashSet<String>();
		}
		public void add(Instance instance) {
			if (instance.getSeriesInstanceUID() == null) { return; }
			if (!seriesList.contains(instance.getSeriesInstanceUID())) {
				seriesList.add(instance.getSeriesInstanceUID());
			}
		}
		public String getStudyInstanceUID() {
			return studyUID;
		}
		public Collection<String> values() {
			return seriesList;
		}
	}
	protected class Series {
		protected String studyUID;
		protected String seriesUID;
		protected String classUID;
		protected HashSet<String> instanceList;
		public Series(Instance instance) {
			studyUID = instance.getStudyInstanceUID();
			seriesUID = instance.getSeriesInstanceUID();
			classUID = instance.getSOPClassUID();
			instanceList = new HashSet<String>();
		}
		public void add(Instance instance) {
			// if (instance == null) { return; }
			if (instance.getSOPInstanceUID() == null) { return; }
			if (!instanceList.contains(instance.getSOPInstanceUID())) {
				instanceList.add(instance.getSOPInstanceUID());
			}
		}
		public Collection<String> values() {
			return instanceList;
		}
		public String getStudyInstanceUID() {
			return studyUID;
		}
		public String getSeriesInstanceUID() {
			return seriesUID;
		}
	}
	protected class Instance {
		protected String studyUID;
		protected String seriesUID;
		protected String instanceUID;
		protected String forUID;
		protected String classUID;
		protected File file;
		public Instance(Attributes object, File file) {
			if (object == null) {
				throw new NullPointerException("Value for Attributes object is null");
			}
			if (file == null) {
				throw new NullPointerException("Value for File object is null");
			}
			initialize(object, file);
		}
		public Instance(File file) throws IOException {
			Attributes object;
			if (file == null) {
				throw new NullPointerException("Value for File object is null");
			}
			if (!file.exists()) {
				throw new FileNotFoundException("File not found");
			}
			DicomInputStream dis = new DicomInputStream(file);
			object = dis.readDataset(-1,  -1);
			dis.close();
			initialize(object, file);
		}
		protected void initialize(Attributes object, File file) {
			if (object.containsValue(Tag.StudyInstanceUID)) {
				studyUID = object.getString(Tag.StudyInstanceUID);
			} else {
				studyUID = null;
			}
			if (object.containsValue(Tag.SeriesInstanceUID)) {
				seriesUID = object.getString(Tag.SeriesInstanceUID);
			} else {
				seriesUID = null;
			}
			if (object.containsValue(Tag.SOPInstanceUID)) {
				instanceUID = object.getString(Tag.SOPInstanceUID);
			} else {
				instanceUID = null;
			}
			if (object.containsValue(Tag.FrameOfReferenceUID)) {
				forUID = object.getString(Tag.FrameOfReferenceUID);
			} else {
				forUID = null;
			}
			this.file = file;
			if (studyUID != null && seriesUID != null && instanceUID != null) {
				if (!studyMap.containsKey(studyUID)) {
					studyMap.put(studyUID, new Study(this));
				}
				studyMap.get(studyUID).add(this);
				if (!seriesMap.containsKey(seriesUID)) {
					seriesMap.put(seriesUID, new Series(this));
				}
				seriesMap.get(seriesUID).add(this);
				if (!instanceMap.containsKey(instanceUID)) {
					instanceMap.put(instanceUID,  this);
				}
			}
		}
		public String getStudyInstanceUID() {
			return studyUID;
		}
		public String getSeriesInstanceUID() {
			return seriesUID;
		}
		public String getSOPInstanceUID() {
			return instanceUID;
		}
		public String getSOPClassUID() {
			return classUID;
		}
		public String getFrameOfReferenceUID() {
			return forUID;
		}
	}
	protected HashMap<String,Study> studyMap;
	protected HashMap<String,Series> seriesMap;
	protected HashMap<String,Instance> instanceMap;
	public DicomIndex() {
		Tag = new Tags();
		studyMap = new HashMap<String,Study>();
		seriesMap = new HashMap<String,Series>();
		instanceMap = new HashMap<String,Instance>();
	}
	public void add(File file) {
		try {
		new Instance(file);
		} catch (IOException e) {
			System.out.println("IO exception");
		}
	}
	public void list() {
		for (Study study : studyMap.values()) {
			System.out.println("Study " + study.getStudyInstanceUID());
			Collection<String> seriesList = study.values();
			for (String series : seriesList) {
				Series ser = seriesMap.get(series);
				System.out.println("Series " + ser.getStudyInstanceUID() + " "  +
				ser.getSeriesInstanceUID());
				Collection<String> instances = ser.values();
				System.out.println("There are " + Integer.toString(instances.size()) + " objects");
			}
		}
	}
	/**
	 * Test driver
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
