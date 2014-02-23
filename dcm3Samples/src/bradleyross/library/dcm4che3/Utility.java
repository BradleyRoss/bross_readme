package bradleyross.library.dcm4che3;
import org.dcm4che.data.Attributes;
import org.dcm4che.net.Device;
//import org.dcm4che.net.ExtQueryTransferCapability;
//import org.dcm4che.net.ExtRetrieveTransferCapability;
//import org.dcm4che.net.ExtStorageTransferCapability;
// NetworkApplicationEntity has apparently become ApplicationEntity
import org.dcm4che.net.ApplicationEntity;
// NetworkConnection has apparently become Connection
import org.dcm4che.net.Connection;
//import org.dcm4che2.net.NewThreadExecutor;
//import org.dcm4che2.net.TransferCapability;
import org.dcm4che.net.Association;
import org.dcm4che.net.DimseRSP;
//import org.dcm4che2.net.CommandUtils;
// import org.dcm4che2.data.DicomObject;
// import org.dcm4che2.data.DicomElement;
// import org.dcm4che2.data.Tag;
// import org.dcm4che2.data.UID;
// import org.dcm4che2.net.TransferCapability;
import bradleyross.library.dcm4che3.DicomSystem;
import bradleyross.library.dcm4che3.Configuration;
import bradleyross.library.helpers.Logger;
import org.dcm4che.conf.api.ConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
// import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.lang.reflect.Field;
import java.util.Vector;
// import org.acr.Dicom.Logger;
/**
 * Miscellaneous utility classes for building and working with
 * an association (in progress).
 * 
 * <p>Originally designed for use with version 2 of the dcm4che
 *    toolkit.  Needs heavy modification to work with version 3.</p>
 * 
 * @see Association
 * @author Bradley Ross
 *
 */
public class Utility {
	protected class Tags {
		
	}
	protected class UIDs {
		String StudyRootQueryRetrieveInformationModelFIND = null;
		String PatientRootQueryRetrieveInformationModelFIND = null;
		String PatientStudyOnlyQueryRetrieveInformationModelFINDRetired = null;
		String StudyRootQueryRetrieveInformationModelGET = null;
		String PatientRootQueryRetrieveInformationModelGET = null;
		String PatientStudyOnlyQueryRetrieveInformationModelGETRetired = null;
		String CompositeInstanceRootRetrieveGET = null;
		String StudyRootQueryRetrieveInformationModelMOVE = null;
	    String PatientRootQueryRetrieveInformationModelMOVE = null;
		String PatientStudyOnlyQueryRetrieveInformationModelMOVERetired = null;
		String ComputedRadiographyImageStorage = null;
		String CTImageStorage = null;
		String MRImageStorage = null;
		String UltrasoundImageStorage = null;
		String NuclearMedicineImageStorage;
		String PositronEmissionTomographyImageStorage;
		String SecondaryCaptureImageStorage;
		String XRayAngiographicImageStorage;
		String XRayRadiofluoroscopicImageStorage;
		String DigitalXRayImageStorageForPresentation;
		String DigitalMammographyXRayImageStorageForPresentation;
		String GrayscaleSoftcopyPresentationStateStorageSOPClass;
		String KeyObjectSelectionDocumentStorage;
		String BasicTextSRStorage;
	}
	UIDs UID = new UIDs();
	/** 
	 * Classes associated with {@link org.dcm4che2.net.ExtQueryTransferCapability}.
	 */
	public String[] queryUID = {UID.StudyRootQueryRetrieveInformationModelFIND,
			UID.PatientRootQueryRetrieveInformationModelFIND,
			UID.PatientStudyOnlyQueryRetrieveInformationModelFINDRetired};
	/**
	 * These classes are associated with
	 * {@link org.dcm4che2.net.ExtRetrieveTransferCapability}.
	 */
	public String[] getUID = { UID.StudyRootQueryRetrieveInformationModelGET,
			UID.PatientRootQueryRetrieveInformationModelGET,
			UID.PatientStudyOnlyQueryRetrieveInformationModelGETRetired,
			UID.CompositeInstanceRootRetrieveGET};
	/**
	 * These classes are associated with
	 * {@link org.dcm4che2.net.ExtRetrieveTransferCapability}.
	 */
	public String[] moveUID = { UID.StudyRootQueryRetrieveInformationModelMOVE,
			UID.PatientRootQueryRetrieveInformationModelMOVE,
			UID.PatientStudyOnlyQueryRetrieveInformationModelMOVERetired};
	/**
	 * Classes associated with {@link org.dcm4che2.net.ExtStorageTransferCapability}.
	 */
	public String[] storeUID = {UID.ComputedRadiographyImageStorage,
			UID.CTImageStorage, UID.MRImageStorage,
			UID.UltrasoundImageStorage, UID.NuclearMedicineImageStorage,
			UID.PositronEmissionTomographyImageStorage,
			UID.SecondaryCaptureImageStorage,
			UID.XRayAngiographicImageStorage,
			UID.XRayRadiofluoroscopicImageStorage,
			UID.DigitalXRayImageStorageForPresentation,
			UID.DigitalMammographyXRayImageStorageForPresentation,
			UID.GrayscaleSoftcopyPresentationStateStorageSOPClass,
			UID.KeyObjectSelectionDocumentStorage,
			UID.BasicTextSRStorage};
	/**
	 * Determine if a string is contained in an array of String values.
	 * @param instance string to be checked
	 * @param array array of strings
	 * @return true if string is contained in array, false otherwise
	 */
	protected boolean containedIn(String instance, String[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			if (array[i].equalsIgnoreCase(instance))
			{
				if (array[i] == null) { continue; }
				return true;
			}
		}
		return false;
	}
	public void waitForOutstandingRSP() throws InterruptedException {
		if (getDebugLevel() > 2) {
			System.out.println("Starting Utility.waitForDimseRSP");
		}
		try {
		assoc.waitForOutstandingRSP(); }
		catch (InterruptedException e) {
			if (getDebugLevel() > 2) {
				System.out.println("Exception in Utility.waitForDimsRSP: " +
						e.getClass().getName() + " " + e.getMessage());
			}
			throw e;
		}
	}	
	/**
	 * Processes address of DICOM server.
	 * @author Bradley Ross
	 *
	 */
	protected final class Destination {
		/**
		 * Application entity title for DICOM server.
		 * @see #getTitle()
		 */
		protected String localTitle = null;
		/**
		 * Port number to be used when communicating with DICOM server.
		 * @see #getPort()
		 */
		protected int localPort = 103;
		/**
		 * Internet domain name or IP address for DICOM server.
		 * @see #getAddress()
		 */
		protected String localAddress = null;
		/**
		 * Getter for title property.
		 * @return title for DICOM server
		 * @see #localTitle
		 */
		public String getTitle()
		{
			return localTitle;
		}
		public void setTitle(String value) {
			localTitle = value;
		}
		/**
		 * Getter for address property.
		 * @return internet domain name or IP address
		 * @see #localAddress
		 */
		public String getAddress()
		{
			return localAddress;
		}
		/**
		 * Getter for port property.
		 * @return port number
		 * @see #localPort
		 */
		public int getPort()
		{
			return localPort;
		}
		/**
		 * URI prefix for WADO calls.
		 * @see #getWadoPrefix()
		 * @see #setWadoPrefix(String)
		 */
		protected String wadoPrefix = null;
		/**
		 * Getter for {@link #wadoPrefix} property.
		 * @return value of property
		 */
		public String getWadoPrefix() {
			return wadoPrefix;
		}
		/**
		 * Setter for {@link #wadoPrefix} property.
		 * @param value value for property
		 */
		public void setWadoPrefix(String value) {
			wadoPrefix = value;
		}
		/**
		 * Constructor with separate arguments for each property.
		 * 
		 * @param titleValue application entity title
		 * @param addressValue domain name or internet address
		 * @param portValue port number
		 */
		public Destination(String titleValue, String addressValue, int portValue)
		{
			localTitle = titleValue;
			localAddress = addressValue;
			localPort = portValue;
		}
		/**
		 * Constructor parsing URI for DICOM server.
		 * 
		 * @param addressValue URI for DICOM server
		 * @throws IllegalArgumentException
		 */
		public Destination(String addressValue) throws IllegalArgumentException
		{
			Logger debuglogger = Logger.getLogger(this.getClass());
			if (addressValue == null)
			{
				IllegalArgumentException error =
				 new IllegalArgumentException("Null value for DICOM server address");
				debuglogger.error(error);
				throw error;
			}
			String value = addressValue.trim();
			if (value.length() == 0)
			{
				IllegalArgumentException error = 
						new IllegalArgumentException("Empty string for DICOM server address");
				debuglogger.error(error);
				throw error;
			}
			int colonLocation = value.lastIndexOf(":");
			int atLocation = value.indexOf("@");
			if (colonLocation == 0)
			{
				IllegalArgumentException error =
				 new IllegalArgumentException("Colon may not be first character " + value);
				debuglogger.error(error);
				throw error;
			}
			if (colonLocation == value.length() - 1)
			{
				IllegalArgumentException error =
				 new IllegalArgumentException("Colon may not be last character " + value);
				debuglogger.error(error);
				throw error;
			}
			if (atLocation == 0)
			{
				IllegalArgumentException error =
				 new IllegalArgumentException("At sign may not be first character " + value);
				debuglogger.error(error);
				throw error;
			}
			if (atLocation < 1)
			{ 
				debuglogger.warn("Problem parsing Dicom URL: " + addressValue + 
						" does not contain an AE Title - DICOM used");
				localTitle = "DICOM";
			}
			else
			{ localTitle = value.substring(0, atLocation); }
			if (colonLocation < 0)
			{ localPort = 104; }
			else
			{
				String test = value.substring(colonLocation + 1);
				try
				{
					localPort = Integer.parseInt(test);
				}
				catch (Exception e)
				{
					IllegalArgumentException error =
					 new IllegalArgumentException("Illegal format for port number " + value);
					debuglogger.error(error);
					throw error;
				}
			}
			if (colonLocation < 0 && atLocation < 0)
			{ localAddress = value; }
			else if (colonLocation >= 0 && atLocation >= 0)
			{ 
				if (colonLocation < atLocation)
				{
					IllegalArgumentException error =
							new IllegalArgumentException("Colon may not be before at sign " + value);
					debuglogger.error(error);
					throw error;
				}
				localAddress = value.substring(atLocation + 1, colonLocation); 
			}
			else if (colonLocation >= 0)
			{ localAddress = value.substring(0, colonLocation); }
			else if (atLocation >= 0)
			{ localAddress = value.substring(atLocation +1); }
			else
			{ 
				IllegalArgumentException error =
						new IllegalArgumentException("Code should be unreachable " + value); 
				debuglogger.error(error);
				throw error;
			}
		}
	}
	/**
	 * Controls amount of diagnostic information printed.
	 * 
	 * <p>More positive values result in greater amounts of information.</p>
	 * 
	 * @see #getDebugLevel()
	 * @see #setDebugLevel(int)
	 */
	public static int debugLevel = 0;
	/**
	 * Getter for debugLevel.
	 * 
	 * @see #debugLevel
	 * @return value of property
	 */
	public static int getDebugLevel()
	{
		return debugLevel;
	}
	/**
	 * Setter for debugLevel property.
	 * 
	 * @see #debugLevel
	 * @param value value of property
	 */
	public static void setDebugLevel(int value)
	{
		debugLevel = value;
	}
	/**
	 * Name to use as application entity title of local system.
	 */
	protected String localAETitle = null;
	/**
	 * Getter for {@link #localAETitle} property.
	 * @return value of property
	 */
	public String getLocalAETitle() {
		return localAETitle;
	}
	/**
	 * Setter for {@link #localAETitle} property.
	 * @param value value for property
	 */
	public void setLocalAETitle(String value) {
		localAETitle = value;
	}
	/**
	 * Device name for local system.
	 */
	protected String deviceName = null;
	/**
	 * Getter for {@link #deviceName} property.
	 * @return value of property
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/**
	 * Setter for {@link #deviceName} property.
	 * @param value value for property
	 */
	protected void setDeviceName(String value) {
		deviceName = value;
	}
	/**
	 * Parse server name to create association.
	 * @param uri URI for Dicom server
	 * @param device local device name
	 * @param title local application entity title
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 */
	public void parseDicomSystemURI(String uri, String device, String title)
	throws IllegalArgumentException, IOException, InterruptedException, Exception {
		parseDicomSystemURI(uri);
		setDeviceName(device);
		setLocalAETitle(title);
	}
	/**
	 * Parse server name to create association.
	 * 
	 * @param nameValue URI for DICOM server
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 */
	public void parseDicomSystemURI(String nameValue)
	throws IllegalArgumentException, IOException, InterruptedException, Exception
	{
		if (getDebugLevel() > 2) {
			System.out.println("Running Utility.parsDicomSystemURI(String)");
			System.out.println("URI is " + nameValue);
		}
		Destination dest = new Destination(nameValue);
		DicomSystem system = new DicomSystem();
		system.setTitle(dest.getTitle());
		system.setAddress(dest.getAddress());
		system.setPort(dest.getPort());
		system.setDisplayName(dest.getTitle());
		system.setLocalName(dest.getTitle());
		if (debugLevel > 2)
		{
			System.out.println("Running Utility.parseDicomSystemURI(String)");
			System.out.println("     Name of server is " + nameValue);
			System.out.println(system.dump());
		}
		createAssociation(system);
		if (getDebugLevel() > 2) {
			System.out.println("Association has been created");
			System.out.println("Returning from Utility.parseDicomSystemURI(String)");
		}
 		return;
	}
	/**
	 * Process server data to create association.
	 * 
	 * @param server  Properties object containing Dicom server information
	 * @param names   character strings representing application entity title, domain name,
	 *                and port number
	 * @param device  Local device name
	 * @param title   Local application entity title
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 * @see DicomSystem
	 */
	public void parseDicomSystemURI(Properties server, String[] names, String device, String title)
	throws IllegalArgumentException, IOException, InterruptedException
	{
		if (getDebugLevel() > 2) {
			System.out.println("Running Utility.parsDicomSystemURI(Properties, String[])");
			// System.out.println("URI is " + nameValue);
		}
		DicomSystem system = new DicomSystem();
		system.setTitle(server.getProperty(names[0]));
		system.setAddress(server.getProperty(names[1]));
		try {
			system.setPort(Integer.parseInt(server.getProperty(names[2])));
		} catch (Exception e) {
			system.setPort(104);
		}
		system.setDisplayName(server.getProperty(names[0]));
		system.setLocalName(server.getProperty(names[0]));
		if (debugLevel > 2)
		{
			System.out.println("Running Utility.parseDicomSystemURI(Properties, String[])");
			System.out.println(system.dump());
		}
		createAssociation(system);
		if (getDebugLevel() > 2) {
			System.out.println("Association has been created");
			System.out.println("Returning from Utility.parseDicomSystemURI(Properties, String[])");
		}
 		return;
	}	
	/**
	 * Process server data to create association.
	 * 
	 * <p>Use 
	 * @param server  Properties object containing Dicom server information
	 * @param names   names of properties containing application entity title, domain name,
	 *                and port number
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 * @see DicomSystem
	 */
	public void parseDicomSystemURI(Properties server, String[] names)
	throws IllegalArgumentException, IOException, InterruptedException {
		parseDicomSystemURI(server, names, server.getProperty(names[0]), server.getProperty(names[0]));
	}
	
	/**
	 * Test method for parsing DICOM addresses in Destination class.
	 * 
	 * @param value string to be parsed
	 * @throws IllegalArgumentException
	 */
	public static void testDestination(String value) throws IllegalArgumentException
	{
		if (getDebugLevel() > 2) {
			System.out.println("Running Utility.testDestination(String");
		}
		if (value != null)
		{
			if (getDebugLevel() > 2) {
				System.out.println();
				System.out.println(value + " parameter to test program ");
			}
		}
		Utility instance = new Utility();
		Destination dest = null;
		try
		{
			dest = instance.new Destination(value);
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error instantiating object");
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException(e.getClass() + " " + e.getMessage());
		}
		System.out.println("Destination object instantiated");
		try
		{
			System.out.println(dest.getTitle() + " AE Title");
			System.out.println(dest.getAddress() + " Address");
			System.out.println(Integer.toString(dest.getPort()) + " port");
		}
		catch (IllegalArgumentException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException(e.getClass() + " " + e.getMessage());
		}
	}
	/**
	 * Creates the Association object.
	 * <p>{@link #getAssociation()} can be used to obtain the
	 *    object in the calling program.</p>
	 * @param value object containing parameters for association
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 */
	protected void createAssociation(DicomSystem value) 
	throws IllegalArgumentException, IOException, InterruptedException
	{
		/*
		DicomSystem remote = value;
		Logger debuglogger = Logger.getLogger(this.getClass());
		if (getLocalAETitle() == null) {
			debuglogger.warn("Utility.createAssociation() - application entity title for " +
					" local system not set - using DUMMY");
			setLocalAETitle("DUMMY");
		}
		if (getDebugLevel() > 2) {	
			System.out.println(remote.dump());
		}
		remoteAE = new ApplicationEntity();
		localAE = new ApplicationEntity();
		localAE.setAETitle(getLocalAETitle());
		Connection remoteConn = new Connection();
		remoteConn.setHostname(remote.getAddress());
		remoteConn.setPort(remote.getPort());
		remoteAE.setAETitle(remote.getTitle());
		remoteAE.setInstalled(true);
		remoteAE.setAssociationAcceptor(true);
		remoteAE.setConnection(new Connection[] { remoteConn });
		Connection localConn = new Connection();
		localAE.setAETitle(Configuration.getLocalTitle());
		localAE.setAssociationInitiator(true);
		localAE.setNetworkConnection(localConn);
		device.setNetworkApplicationEntity(localAE);
		device.setConnection(localConn);
		debuglogger.info("Device " + getDeviceName() + " at " + getLocalAETitle() +
				" connected to " + remote.getTitle() + "@" + remote.getAddress() +
				":" + Integer.toString(remote.getPort()));
		return;
		*/
	}
	/**
	 * Establish connection to the remote DICOM server.
	 * 
	 * <p>Does not establish a list of UID's that describe objects
	 * that can be received.</p>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 */
	public void connect() 
	throws IOException, InterruptedException
	{
		/*
		Logger debuglogger = Logger.getLogger(this.getClass());
		if (localAE == null) {
			IllegalArgumentException error =
					new IllegalArgumentException("Utility.connect - Value of localAE is null");
			debuglogger.error(error);
			throw error;
		}
		if (remoteAE == null) {
			IllegalArgumentException error =
			new IllegalArgumentException("Utility.connect - Value of remoteAE is null");
			debuglogger.error(error);
			throw error;
		}
		if (tcs == null)
		{ ; }
		else if (tcs.length == 0)
		{ ; }
		else
		{ localAE.setTransferCapability(tcs); }
		assoc = localAE.connect(remoteAE, executor);
		debuglogger.info("Utility.connect(): Association created");
		if (getDebugLevel() > 2) {
			System.out.println("Utility.connect(): Association created");
		}
		*/
	}
	/**
	 * Connect to remote server.
	 * @param cuids UIDs for classes for which local server is
	 *        capable of serving as an SCU
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ConfigurationException
	 */
	public void connect(String[] cuids)
	throws IOException, InterruptedException
	{
		/*
		Logger debuglogger = Logger.getLogger(this.getClass());
		final String[] NATIVE_LE_TS = {
				UID.ExplicitVRLittleEndian,
				UID.ImplicitVRLittleEndian,
				UID.DeflatedExplicitVRLittleEndian};

		TransferCapability[] tcs = new TransferCapability[cuids.length];
		for (int i = 0; i < cuids.length; i++)
		{
			if (containedIn(cuids[i], queryUID))
			{
				tcs[i] = new ExtQueryTransferCapability(cuids[i], NATIVE_LE_TS, TransferCapability.SCU);
			}
			else if (containedIn(cuids[i], getUID))
			{
				tcs[i] = new ExtRetrieveTransferCapability(cuids[i], NATIVE_LE_TS, TransferCapability.SCU);
			}
			else if (containedIn(cuids[i], moveUID))
			{
				tcs[i] = new ExtRetrieveTransferCapability(cuids[i], NATIVE_LE_TS, TransferCapability.SCU);
			}
			else if (containedIn(cuids[i], storeUID))
			{
				tcs[i] = new ExtStorageTransferCapability(cuids[i], NATIVE_LE_TS, TransferCapability.SCU);
			}
			if (tcs[i] == null)
			{
				System.out.println("Unable to establish transfer capability " + cuids[i] + 
						Utility.toUIDName(cuids[i]));
			}
		}
		getLocalAE().setTransferCapability(tcs);
		if (debugLevel > 0)
		{
			System.out.println("Transfer Capabilities set");
			System.out.println("Local transfer capabilities");
			TransferCapability[] test = localAE.getTransferCapability();
			System.out.println("There are " + Integer.toString(test.length) + " entries");
			for (int i = 0; i < test.length; i++)
			{
				System.out.println(Utility.toUIDName(test[i].getSopClass()) + " " +
						Boolean.toString( test[i].isSCU()));
				String[] list = test[i].getTransferSyntax();
				for (int j = 0; j < list.length; j++)
				{
					System.out.println("     " + Utility.toUIDName(list[j]));
				}
			}
		}
		assoc = localAE.connect(remoteAE, executor);
		if (getDebugLevel() > 2) {
			System.out.println("Utility.connect(String[]): Association created");
		}
		debuglogger.info("Utility.connect(String[]): Association created");
		*/
	}
	/**
	 * Represents session connecting local and remote DICOM servers.
	 * @see #getAssociation()
	 * @see #buildAssociation(String)
	 */
	private Association assoc;
	/**
	 * Getter for association property.
	 * @return Association object
	 */
	public Association getAssociation()
	{
		return assoc;
	}
	public void setAssciation(Association value) {
		assoc = value;
	}
	/**
	 * Local Network Application Entity object.
	 * @see #getLocalAE()
	 */
	private ApplicationEntity localAE = null;
	/**
	 * Getter for localAE property.
	 * @return local Network Application Entity object
	 * @see #localAE
	 */
	public ApplicationEntity getLocalAE()
	{
		return localAE;
	}
	/**
	 * Remote Network Application Entity object.
	 * @see #getRemoteAE()
	 */
	private ApplicationEntity remoteAE = null;
	/**
	 * Getter for remoteAE property.
	 * @return remote Network Application Entity object.
	 * @see #remoteAE
	 */
	public ApplicationEntity getRemoteAE()
	{
		return remoteAE;
	}
	/**
	 * Represents the device to which the local Application Entity and
	 * Network Connection belong.
	 * @see #getDevice()
	 */
	private Device device = null;
	/**
	 * Getter for device property.
	 * @return Device object in use by association
	 */
	public Device getDevice()
	{
		return device;
	}
	/**
	 * This is the runnable thread that carries out the
	 * DICOM communications.
	 * @see #getExecutor()
	 */
	/*
	private NewThreadExecutor executor = null;
	public NewThreadExecutor getExecutor()
	{
		return executor;
	}
	*/
	/**
	 * List of transfer capabilities that this entity
	 * is willing to accept from the remote entity.
	 * 
	 * <p>According to Annex H of part 15 of the Dicom specification, each Network
	 *    Application Entity has one or more transfer capabilities, with each
	 *    capability being defined by the SOP class supported by the capability, the
	 *    set of transfer syntaxes that it can support, and the mode of operation
	 *    (SCP or SCU).  This structure only contains the capabilities that the local
	 *    system supports as an SCU (service class user).</p>
	 * <p>The capabilities supported by the remote system as an SCP (service class
	 *    provider) are determined by communications with the remote system.</p>
	 */
	/*
	private TransferCapability tcs[] = null;
	/**
	 * Setter for tcs property.
	 * @param value list of transfer capabilities
	 * @see #tcs
	 */
	/*
	public void setTcs(TransferCapability[] value)
	
	{
		tcs = value;
	}
	*/
	/**
	 * Getter for tcs property
	 * @return list of transfer capabilities
	 * @see #tcs
	 */
	/*
	public TransferCapability[] getTcs()
	{
		return tcs;
	}
	*/
	/** 
	 * Actions to be taken when removing object.
	 */
	public void finalize() {
		
	}
	/**
	 * Constructor using default name for local device.
	 */
	public Utility()
	{
		if (getDebugLevel() > 2) {
			System.out.println("Running constructor Utility#Utility()");
		}
		/*
		device = new Device("DCMLOCAL");
		setDeviceName("DCMLOCAL");
		executor = new NewThreadExecutor("DCMLOCAL");
		*/
	}
	/**
	 * Constructor allowing specification of name for
	 * local device.
	 * @param value Name of local device
	 */
	/*
	public Utility(String value)
	{
		if (getDebugLevel() > 2) { 
			System.out.println("Running constructor Utility#Utility(String)");
		}
		setDeviceName(value);
		device = new Device(value);
		executor = new NewThreadExecutor(value);
	}
	*/
	/**
	 * Constructor allowing specification of name for
	 * local device and local application entity title.
	 * @param deviceValue name of local device
	 * @param titleValue application entity title for local device
	 */
	/*
	public Utility(String deviceValue, String titleValue)
	{
		if (getDebugLevel() > 2) { 
			System.out.println("Running constructor Utility#Utility(String)");
		}
		setDeviceName(deviceValue);
		setLocalAETitle(titleValue);
		device = new Device(deviceValue);
		executor = new NewThreadExecutor(deviceValue);
	}	
	*/
	/**
	 * Set up the association using information about the local
	 * and remote Network Application Entity objects.
	 * 
	 * @param system {@link DicomSystem#displayName display name}
	 *        of System as stored in {@link Configuration }
	 * 
	 */
	/*
	public void buildAssociation(String system) throws IOException, InterruptedException, ConfigurationException
	{
		DicomSystem remote = Configuration.getSystem(system);
		createAssociation(remote);
		if (tcs == null)
		{ ; }
		else if (tcs.length == 0)
		{ ; }
		else
		{ localAE.setTransferCapability(tcs); }
		assoc = localAE.connect(remoteAE, executor);
	}
	*/
	/** 
	 * Display information about an Association.
	 * @param value Association for which information is desired
	 * @return String containing information
	 * @see org.dcm4che2.net.Association
	 */
	public String dumpAssociation(Association value)
	{
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		printIfNotNull(out, "CalledAET:",assoc.getCalledAET());
		printIfNotNull(out, "CallingAET;", assoc.getCallingAET());
		printIfNotNull(out, "LocalAET:", assoc.getLocalAET());
		printIfNotNull(out, "RemoteAET:", assoc.getRemoteAET());
		return writer.toString();
	}
	/**
	 * Close the session between the DICOM servers
	 * @see Association#release(boolean)
	 * @throws InterruptedException
	 */
	public void close() throws InterruptedException
	{
		Logger debuglogger = Logger.getLogger(this.getClass());
		/*
		try {

			debuglogger.info("Running Utility#close()");
			if (getDebugLevel() > 2) {
				System.out.println("Running Utility#close()");
			}
			// assoc.release(true);
		}  catch (InterruptedException e) {
			debuglogger.warn(e);
			throw e;
		}
		*/
	}
	/**
	 * Dump contents of a DicomObject.
	 * <p>It looks like the method to return the command object
	 *    also returns the contents of the dataset object.</p>
	 * @param object Object for which data is to be dumped
	 * @return String containing information
	 */
	public static String dumpCommandObject(Attributes object)
	{
		/*
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer, true);
		Iterator<DicomElement> iter = object.excludePrivate().iterator();
		int counter = 0;
		out.println("There are " + Integer.toString(object.size()) + " command elements");
		out.println("Class type for response is " + object.getClass().getName());
		while (iter.hasNext())
		{
			counter++;
			out.println(dump("** " + Integer.toString(counter) + " ** ",
					iter.next()));
		}
		return writer.toString();
		*/
		return null;
	}

	/**
	 * Dump contents of a DicomObject.
	 * @param object Object for which data is to be dumped
	 * @return String containing information
	 */
	public static String dumpDatasetObject(Attributes object)
	{
		/*
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer, true);
		int counter = 0;
		out.println("There are " + Integer.toString(object.size()) + " dataset elements");
		counter = 0;
		Iterator<DicomElement> iter = object.excludePrivate().iterator();
		while (iter.hasNext())
		{
			counter++;
			out.println(dump("** " + Integer.toString(counter) + " ** ",
					iter.next()));
		}
		return writer.toString();
		*/
		return null;
	}

	/**
	 * Dump information contained in a response from a DICOM request.
	 * 
	 * <p>The response resulting from a DICOM transaction request can have
	 *    multiple parts.  Each of the parts can have a dataset DICOM objects
	 *    and a command DICOM object.  (The dataset objects are optional,
	 *    depending on the type of transaction.)</p>
	 * @see DimseRSP
	 * @param response request object
	 * @return String containing information from response
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String dump(DimseRSP response) throws IOException, InterruptedException
	{
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer, true);
		out.println("Starting dump of response");
		int counter = 0;
		Attributes dataset = null;
		Attributes data = null;
		while (response.next())
		{
			counter++;
			dataset = response.getDataset();
			data = response.getCommand();
			out.println("*****  *****  *****");
			out.println("Response " + Integer.toString(counter) + 
					" read with " + Integer.toString(data.size()) +
			" command elements");
			if (data != null && counter < 2)
			{
				out.println("Class for command object is " + data.getClass().getName());
				out.println(dumpCommandObject(data));
			}
			/*
			if(data.getInt(Tag.Status) == 0)
			{
				out.println("End of response reached");
				if (counter > 1)
				{
					break;
				}
			}
			*/
			if (dataset != null)
			{
				out.println("Processing dataset in response");
				out.println("Class for dataset object is " + dataset.getClass().getName());
				out.println(dumpDatasetObject(dataset));
			}
			else
			{
				out.println("No datasets in response");
			}
		}
		return writer.toString();
	}
	/**
	 * Obtain the name of the field for a unique identifier given the
	 * value of the unique identifier.
	 * @param value unique identifier value
	 * @return Name of the field
	 */
	public static String toUIDName(String value)
	{
		/*
		String test = value.trim();
		if (test.indexOf(".") < 0)
		{
			return test;
		}
		Field[] fields = UID.class.getFields();
		try
		{
			for (int i = 0; i < fields.length; i++)
			{
				if (((String) ((Field) fields[i]).get(null)).equals(test))
				{
					return ((Field) fields[i]).getName();
				}
			}
		}
		catch (IllegalAccessException e)
		{
			return "Unknown";
		}
		return "Unknown";
		*/
		return null;
	}
	/**
	 * Obtain the name of a string defining a tag given the value of the tag.
	 * 
	 * @param value value of the tag
	 * @return Name of the field in {@link org.dcm4che2.data.Tag}
	 */
	public static String toTagName(int value)
	{
		/*
		Field fields[] = Tag.class.getFields();
		try
		{
			for (int i = 0; i < fields.length; i++)
			{
				if (((Field) fields[i]).getInt(null) == value)
				{
					return ((Field) fields[i]).getName();
				}
			}
		}
		catch (IllegalAccessException e)
		{
			return "Unknown";
		}
		*/
		return "Unknown";
	}
	/**
	 * Get the name of a tag from the value using 
	 * base 16 arithmetic.
	 * 
	 * @param value String containing numeric value of tag
	 * @return Name of tag in {@link org.dcm4che2.data.Tag}
	 */
	public static String toTagName(String value)
	{
		int intValue = -1;
		try
		{
			intValue = Integer.parseInt(value.trim(), 16);
		}
		catch (NumberFormatException e)
		{
			return value.trim();
		}
		return toTagName(intValue);
	}
	/**
	 * Extract the last part of the class name.
	 * @param value class name
	 * @return last part following last period
	 */
	public static String getClassName(String value)
	{
		int pos = value.lastIndexOf(".");
		if (pos < 0)
		{
			return value;
		}
		else
		{
			return value.substring(pos + 1);
		}
	}

	/**
	 * Print a line of information if the String object is not null
	 * @param out destination for the printing
	 * @param legend descriptive information for printed line
	 * @param value value to be printed if not null
	 */
	protected static void printIfNotNull(PrintWriter out, String legend, String value)
	{
		if (value == null)
		{ ; }
		else if (value.length() == 0)
		{ ; }
		else
		{
			out.println(legend + " " + value.trim());
		}
	}
	/**
	 * Converts value of CommandField tag to a human readable description.
	 * 
	 * @param value value of CommandField tag
	 * @return Human readable description
	 * @see org.dcm4che2.net.CommandUtils
	 * <p><a href="http://www.medicalconnections.co.uk/wiki/Basic_DICOM_Operations" target="_blank">See here for more information</a></p>
	 */
	/*
	public static String parseCommandField(int value)
	{
		if (value == CommandUtils.C_CANCEL_RQ)
		{
			return "Request object for a C-CANCEL service operation";
		}
		else if (value == CommandUtils.C_ECHO_RQ)
		{
			return "Request object for a C-ECHO service operation";
		}
		else if (value == CommandUtils.C_ECHO_RSP)
		{
			return "Response object for a C-ECHO service operation";
		}
		else if (value == CommandUtils.C_FIND_RQ)
		{
			return "Request object for a C-FIND service operation";
		}
		else if (value == CommandUtils.C_FIND_RSP)
		{
			return "Response object for a C-FIND service operation";
		}
		else if (value == CommandUtils.C_GET_RQ)
		{
			return "Request object for a C-GET service operation";
		}
		else if (value == CommandUtils.C_GET_RSP)
		{
			return "Response object for a C-GET service opertaion";
		}
		else if (value == CommandUtils.C_MOVE_RQ)
		{
			return "Request object for a C-MOVE service operation";
		}
		else if (value == CommandUtils.C_MOVE_RSP)
		{
			return "Response object for a C-MOVE service operation";
		}
		else if (value == CommandUtils.C_STORE_RQ)
		{
			return "Request object for a C-STORE service operation";
		}
		else if (value == CommandUtils.C_STORE_RSP)
		{
			return "Response object for a C-STORE service operation";
		}
		else if (value == CommandUtils.N_ACTION_RQ)
		{
			return "Request object for an N-ACTION service operation";
		}
		else if (value == CommandUtils.N_ACTION_RSP)
		{
			return "Response object for an N-ACTION service operation";
		}
		else if (value == CommandUtils.N_CREATE_RQ)
		{
			return "Request object for an N-CREATE service operation";
		}
		else if (value == CommandUtils.N_CREATE_RSP)
		{
			return "Response object for an N-CREATE service operation";
		}
		else if (value == CommandUtils.N_DELETE_RQ)
		{
			return "Request object for an N-DELETE service operation";
		}
		else if (value == CommandUtils.N_DELETE_RSP)
		{
			return "Response object for an N-DELETE service operation";
		}
		else if (value == CommandUtils.N_EVENT_REPORT_RQ)
		{
			return "Request object for an N-EVENT-REPORT service operation";
		}
		else if (value == CommandUtils.N_EVENT_REPORT_RSP)
		{
			return "Response object for an N-EVENT-REPORT service operation";
		}
		else if (value == CommandUtils.N_GET_RQ)
		{
			return "Request object for an N-GET service operation";
		}
		else if (value == CommandUtils.N_GET_RSP)
		{
			return "Respobnse object for an N-GET service operation";
		}
		else if (value == CommandUtils.N_SET_RQ)
		{
			return "Request object for an N-SET service operation";
		}
		else if (value == CommandUtils.N_SET_RSP)
		{
			return "Response object for an N-SET service opertion";
		}
		return "Unknown";
	}
	*/

	/**
	 * Decode values of the {@link Tag#Status} tag.
	 * 
	 * <p>The following information was contained in document CP-602, which was a 
	 *    correction to C.4.2 and C.4.3 in Part 4 of the Dicom standard.  This
	 *    document was issued 22-August-2006.</p>
	 * @param value value of tag
	 * @return descriptive string
	 */
	public static String decodeStatus(int value) {
		/* 
		 * The following information was taken from
		 * org.dcm4che2.net.Status
		 */
		if (value == 0x0111) {
			return "0111 Duplicate SOP Instance";
		}
		else if (value == 0x0112) {
			return "0112 No Such Object Instance";
		}
		else if (value == 0x0118) {
			return "0118 No Such SOP class";
		}
		else if (value == 0x0122) {
			return "0122 SOP Class not supported";
		}
		else if (value == 0x0211) {
			return "0211 Unrecognized operation";
		}
		else if (value == 0xFF01) {
			return "FF01 Pending warning";
		}
		/*
		 * The following information was contained in document CP-602, which was a 
	     *    correction to C.4.2 and C.4.3 in Part 4 of the Dicom standard.  This
	     *    document was issued 22-August-2006.
		 */
		else if (value == 0xA701) {
			return "A701 Failure Refused: Out of Resources - Unable to calculate number of matches";
			}
		else if (value == 0xA702) { 
			return "A702 Failure - Refused: Out of Resources - Unable to perform sub-operations"; 
			}
		else if (value == 0xA900) {
			return " A900 Failure - Identifier does not match SOP Class";
		}
		else if (value >= 0xC000 && value <= 0xCFFF) {
			return Integer.toString(value, 16) + " Cxxx Failure - Unable to process";
		}
		else if (value == 0xFE00) {
			return "FE00 Cancel - Sub-operations terminated due to Cancel Indication";
		}
		else if (value == 0xB000) {
			return "B000 Warning - Sub-operations Complete - One or more Failures or Warnings";
		}
		else if (value == 0x0000) {
			return "0000 Success - Sub-operations Complete - No Failures or Warnings";
		}
		else if (value == 0xFF00) {
			return "FF00 Pending - Sub-operations are continuing";
		}
		else {
			return Integer.toString(value, 16) + "Unknown status code" ; }
		
	}
	/**
	 * Test driver
	 * @see DicomObject
	 * @param args Not used in this instance
	 */
	public static void main(String[] args) {
		/*
		Utility instance = new Utility();
		DimseRSP rsp = null;
		Configuration.testLoad();
		try
		{
			instance.buildAssociation("AcrDCM4CHEE");
			rsp = instance.getAssociation().cecho();
			System.out.println(Utility.dump(rsp));
			instance.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}
		instance = new Utility();
		try
		{
			instance.buildAssociation("BADENTRY");
			rsp = instance.getAssociation().cecho();
			System.out.println(Utility.dump(rsp));
			instance.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}
		*/
	}
}
