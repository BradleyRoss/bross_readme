package bradleyross.dcm4che3.samples;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
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
		public int SliceLocation = dictionary.tagForKeyword("SliceLocation");
		public int AcquisitionTime = dictionary.tagForKeyword("AcquisitionTime");
		public int AcquisitionDate = dictionary.tagForKeyword("AcquisitionDate");
		public int AcquisitionDateTime = dictionary.tagForKeyword("AcquisitiionDateTime");
		public int AcquisitionNumber = dictionary.tagForKeyword("AcquisitionNumber");
		public int SeriesDate = dictionary.tagForKeyword("SeriesDate");
		public int SeriesTime = dictionary.tagForKeyword("SeriesTime");
		public int SeriesDescription = dictionary.tagForKeyword("SeriesDescription");
		public int StudyDescription = dictionary.tagForKeyword("StudyDescription");
		public int PatientName = dictionary.tagForKeyword("PatientName");
	}
	protected Tags Tag;
	/**
	 * Contains information about a Dicom study.
	 * @author Bradley Ross
	 *
	 */
	protected class Study {
		protected String studyUID;
		protected String patientName;
		protected HashSet<String> seriesList;
		public Study(Instance instance) {
			seriesList = new HashSet<String>();
			studyUID = instance.getStudyInstanceUID();
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
		public String getPatientName() {
			return patientName;
		}
		protected void setPatientName(String value) {
			patientName = value;
		}
	}
	/**
	 * Contains information about a Dicom series.
	 * @author Bradley Ross
	 *
	 */
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
	/** 
	 * Contains information about a singe Dicom object.
	 * @author Bradley Ross
	 *
	 */
	protected class Instance {
		protected String studyUID;
		protected String seriesUID;
		protected String instanceUID;
		protected String forUID;
		protected String classUID;
		protected File file;
		protected float sliceLocation;
		protected int acquisitionNumber;
		protected Date acquisitionTime;
		protected Date acquisitionDate;
		protected Date acquisitionDateTime;
		protected String patientName;
		protected String studyDescription;
		protected String seriesDescription;
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
			if (object.containsValue(Tag.SliceLocation)) {
				sliceLocation = object.getFloat(Tag.SliceLocation, -1.0f);
			} else {
				sliceLocation = -1.0f;
			}
			if (object.containsValue(Tag.AcquisitionNumber)) {
				acquisitionNumber = object.getInt(Tag.AcquisitionNumber, -1);
			} else {
				acquisitionNumber = -1;
			}
			if (object.containsValue(Tag.AcquisitionTime)) {
				acquisitionTime = object.getDate(Tag.AcquisitionTime);
			} else {
				acquisitionTime = null;
			}
			if (object.containsValue(Tag.AcquisitionDate)) {
				acquisitionDate = object.getDate(Tag.AcquisitionDate);
			} else {
				acquisitionDate = null;
			}
			if (object.containsValue(Tag.AcquisitionDateTime)) {
				acquisitionDateTime = object.getDate(Tag.AcquisitionDateTime);
			} else {
				acquisitionDateTime = null;
			}
			if (object.containsValue(Tag.SOPClassUID)) {
				classUID = object.getString(Tag.SOPClassUID);
			} else {
				classUID = null;
			}
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
			if (object.containsValue(Tag.PatientName)) {
				patientName = object.getString(Tag.PatientName);
			} else {
				patientName = null;
			}
			if (object.containsValue(Tag.StudyDescription)) {
				studyDescription = object.getString(Tag.StudyDescription);
			} else {
				studyDescription = null;
			}
			if (object.containsValue(Tag.SeriesDescription)) {
				seriesDescription = object.getString(Tag.SeriesDescription);
			} else {
				seriesDescription = null;
			}
			this.file = file;
			if (studyUID != null && seriesUID != null && instanceUID != null) {
				if (!studyMap.containsKey(studyUID)) {
					Study working = new Study(this);
					studyMap.put(studyUID, working);
				}
				studyMap.get(studyUID).add(this);
				if (!seriesMap.containsKey(seriesUID)) {
					Series working = new Series(this);
					seriesMap.put(seriesUID, working);
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
		public File getFile() {
			return file;
		}
		public float getSliceLocation() {
			return sliceLocation;
		}
		public Date getAcquisitionTime() {
			return acquisitionTime;
		}
		public int getAcquisitionNumber() {
			return acquisitionNumber;
		}
		public String getStudyDescription() {
			return studyDescription;
		}
		public String getSeriesDescription() {
			return seriesDescription;
		}
		public String getPatientName() {
			return patientName;
		}
	}
	/**
	 * Contains {@link Study} objects indexed by
	 * StudyInstanceUID.
	 */
	protected HashMap<String,Study> studyMap;
	/**
	 * Contains {@link Series} objects indexed by
	 * SeriesInstanceUID.
	 */
	protected HashMap<String,Series> seriesMap;
	/**
	 * Contains {@link Instance} objects indexed
	 * by SOPInstanceUID.
	 */
	protected HashMap<String,Instance> instanceMap;
	/**
	 * Return a list of studies.
	 * @return list of studies
	 */
	public Collection<Study> studyIterator() {
		return studyMap.values();
		
	}
	/**
	 * Return a list of series.
	 * @return list of series
	 */
	public Collection<Series> seriesIterator() {
		return seriesMap.values();
	}
	/**
	 * Return a list of series belonging to a study.
	 * @param studyUID study instance UID
	 * @return list of series
	 */
	public Collection<Series> seriesIterator(String studyUID) {
		Study study = studyMap.get(studyUID);
		Collection<Series> working = new ArrayList<Series>();
		for (String item : study.values()) {
			working.add(seriesMap.get(item));
		}
		return working;
	}
	/**
	 * Return a list of SOP instances.
	 * @return list of instances
	 */
	public Collection<Instance> instanceIterator() {
		return instanceMap.values();
	}
	/**
	 * Return a list of SOP instances belonging to a series.
	 * @param seriesUID series instance UID
	 * @return list of instances
	 */
	public Collection<Instance> instanceIterator(String seriesUID) {
		Series series = seriesMap.get(seriesUID);
		Collection<Instance> working = new ArrayList<Instance>();
		for (String item : series.values()) {
			working.add(instanceMap.get(item));
		}
		return working;
	}
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

}
