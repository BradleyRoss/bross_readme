package bradleyross.library.dcm4che3;
/**
 * Represents a remote DICOM system.
 * 
 * @author Bradley Ross
 *
 */
public class DicomSystem {
	/**
	 * Default constructor.
	 */
	public DicomSystem()
	{ ; }
	/**
	 * IP address or domain name for target system.
	 */
	private String address = null;
	/**
	 * Getter for address property
	 * @see #address
	 * @return IP address or domain name
	 */
	public String getAddress()
	{
		return address;
	}
	/**
	 * Setter for IP address or domain name.
	 * @see #address
	 * @param value IP address or domain name
	 */
	public void setAddress(String value)
	{
		address = value;
	}
	/**
	 * Port number for target system.
	 * 
	 * <p>A negative value indicates that the value 
	 *    has not been set.</p>
	 */
	private int port = -1;
	/**
	 * Getter for port number.
	 * @see #port
	 * @return port number
	 */
	public int getPort()
	{
		return port;
	}
	/**
	 * Setter for port number.
	 * @see #port
	 * @param value port number
	 */
	public void setPort(int value)
	{
		port = value;
	}
	/**
	 * Application entity title for remote system.
	 */
	private String title = null;
	/**
	 * Getter for title property.
	 * @see #title
	 * @return title property
	 */
	public String getTitle()
	{
		return title;
	}
	/** 
	 * Setter for title property
	 * @see #title
	 * @param value value for title property
	 */
	public void setTitle(String value)
	{
		title = value;
	}
	/**
	 * Display name for remote system.  
	 * 
	 * By setting the
	 * display name different from the application entity
	 * title, it is possible to connect to multiple servers
	 * with the same name.
	 */
	private String displayName = null;
	/**
	 * Getter for displayName property.
	 * @see #displayName
	 * @return displayName property
	 */
	public String getDisplayName()
	{
		return displayName;
	}
	/** 
	 * Setter for displayName property.
	 * @see #displayName
	 * @param value displayName property value
	 */
	public void setDisplayName(String value)
	{
		displayName = value;
	}
	/**
	 * This would be an alternate Application Entity Title to be used
	 * for the local system when dealing with the remote system.
	 */
	private String localName = null;
	/**
	 * Getter for localName property.
	 * @return value of localName property
	 * @see #localName
	 */
	public String getLocalName()
	{
		return localName;
	}
	/**
	 * Setter for localName property.
	 * @param value value of localName property
	 * @see #localName
	 */
	public void setLocalName(String value)
	{
		localName = value;
	}
	private String description = new String();
	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		description = value;
	}
	private String sendTransferSyntax = null;
	public String getSendTransferSyntax() {
		return sendTransferSyntax;
	}
	public void setSendTransferSyntax(String value) {
		sendTransferSyntax = value;
		if (sendTransferSyntax == null) {
			sendTransferSyntax = new String();
		}
	}
	/**
	 * Copy information on the system to a String object which
	 * can be used in diagnostic messages.
	 * 
	 * @return Information on server
	 */
	public String dump()
	{
		StringBuffer working = new StringBuffer("System information:");
		if (displayName != null)
		{
			working.append("\n   Display Name: " + displayName);
		}
		if (title != null)
		{
			working.append("\n   Title: " + title);
		}
		if (address != null)
		{
			working.append("\n   Address: " + address);
		}
		if (port >= 0)
		{
			working.append("\n   Port: " + Integer.toString(port));
		}
		return new String(working);
	}
	/**
	 * Returns the data for the Medical Connections
	 * public Dicom server.
	 * @return system description
	 */
	public DicomSystem getMedicalConnections() {
		DicomSystem working = new DicomSystem();
		working.setAddress("www.dicomserver.co.uk");
		working.setPort(104);
		working.setTitle("DICOMSERVER");
		working.setDisplayName("Medical Connections");
		return working;
	}
	/**
	 * Provides the data for the PixelMed public Dicom server.
	 * @return system description
	 */
	public DicomSystem getPixelMed() {
		DicomSystem working = new DicomSystem();
		working.setAddress("184.73.255.26");
		working.setPort(11112);
		working.setTitle("AWSPIXELMEDPUB");
		working.setDisplayName("PixelMed");
		return working;
		
	}
}
