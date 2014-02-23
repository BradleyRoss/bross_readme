package bradleyross.library.dcm4che3;
import java.util.ArrayList;
/**
 * Handles information on the local server and the remote servers
 * that can be use for processing.
 * 
 * @author Bradley Ross
 *
 */
public class Configuration {
	/**
	 * List of DICOM servers to which connections can be made.
	 */
	private static ArrayList<DicomSystem> systemList = new ArrayList<DicomSystem>();
	/**
	 * Add a DICOM server to the list of DICOM servers.
	 * @param value properties of DICOM server
	 */
	public static void addSystem(DicomSystem value)
	{
		systemList.add(value);
	}
	/**
	 * Return list of defined DICOM servers
	 * @return list of servers
	 */
	public static ArrayList<DicomSystem> getSystems()
	{
		return systemList;
	}
	/**
	 * Get the properties for a specific DICOM server
	 * @param value identifies DICOM server
	 * @return properties of server
	 */
	public static DicomSystem getSystem(String value)
	{
		for (int i = 0; i < systemList.size(); i++)
		{
			if (systemList.get(i).getDisplayName().equalsIgnoreCase(value))
			{
				return systemList.get(i);
			}
		}
		return new DicomSystem();
	}
	/**
	 * IP address or domain name to be used for
	 * local system.
	 */
	private static String localAddress = "127.0.0.1";
	/**
	 * Getter for localAddress property.
	 * @return localAddress property
	 * @see #localAddress
	 */
	public static String getLocalAddress()
	{
		return localAddress;
	}
	/**
	 * Setter for localAddress property.
	 * @param value localAddress property
	 * @see #localAddress
	 */
	public static void setLocalAddress(String value)
	{
		localAddress = value;
	}
	/**
	 * Port address to be used for local system.
	 */
	private static int localPort = -1;
	/**
	 * Getter for localPort property.
	 * @return local port number
	 * @see #localPort
	 */
	public static int getLocalPort()
	{
		return localPort;
	}
	/**
	 * Setter for localPort property.
	 * @param value localPort property
	 * @see #localPort
	 */
	public static void setLocalPort(int value)
	{
		localPort = value;
	}
	/**
	 * Application entity title to be used for local system.
	 */
	private static String localTitle = "TEST";
	/**
	 * Getter for localTitle property
	 * @return localTitle property
	 * @see #localTitle
	 */
	public static String getLocalTitle()
	{
		return localTitle;
	}
	/**
	 * Setter for localTitle property
	 * @param value localTitle property
	 * @see #localTitle
	 */
	public static void setLocalTitle(String value)
	{
		localTitle = value;
	}
	/**
	 * Set up a  DICOM servers that can be used for requests.
	 * <table border="1">
	 * <tr><td>DICOMSERVER</td><td>DICOMSERVER@www.dicomserver.co.uk:104</td></tr>
	 * </table>
	 * <p>An additional entry was set up with the name BADENTRY for testing 
	 *    purposes.  This entry deliberately points to a nonexistent
	 *    server.</p>
	 * 
	 */
	public static void testLoad()
	{
		setLocalTitle("TEST");
		DicomSystem working = null;
		working = new DicomSystem();
		working.setAddress("www.dicomserver.co.uk");
		working.setPort(104);
		working.setTitle("DICOMSERVER");
		working.setDisplayName("DICOMSERVER");
		addSystem(working);
	}
	/**
	 * Add DICOMSERVER to the system list
	 * and then print its configuration.
	 * 
	 * @param args Not used in this case
	 */
	public static void main(String[] args) {
		testLoad();

		System.out.println(getSystem("DICOMSERVER").dump());
	}
}
