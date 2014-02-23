package bradleyross.dcm4che3.samples;
import java.io.IOException;
import org.dcm4che.net.ApplicationEntity;
import org.dcm4che.net.Connection;
import org.dcm4che.net.Device;
import org.dcm4che.net.DimseRSPHandler;
import org.dcm4che.net.pdu.AAssociateRQ;
import org.dcm4che.tool.findscu.FindSCU;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.dcm4che.tool.storescu.StoreSCU;
/**
 * Start of code using Dicom connection based on
 * {@link StoreSCU} and {@link FindSCU}.
 * <ul>
 * <li>DICOMSERVER@www.dicomserver.co.uk:104</li>
 * <li>AWSPIXELMEDPUB@184.73.255.26:11112</li>
 * <li>BRADLEYROSS@127.0.0.1:11112</li>
 * </ul>
 * @author Bradley Ross
 *
 */
public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 Device device = new Device("storescu");
         Connection conn = new Connection();
         device.addConnection(conn);
         ApplicationEntity ae = new ApplicationEntity("STORESCU");
         device.addApplicationEntity(ae);
         ae.addConnection(conn);
         try {
         StoreSCU main = new StoreSCU(ae);
         } catch (IOException e) {
        	 
         }
         */
		String targetAE = "DICOMSERVER";
		String targetAddress = "www.dicomserver.co.uk";
		int targetPort = 104;
		 Device device = new Device("local");
		 ApplicationEntity localAE = new ApplicationEntity("LOCAL");
		 Connection conn = new Connection();
		 Connection remote = new Connection();
		 AAssociateRQ rq = new AAssociateRQ();
		 /*
		  * configureConnect - set remote
		  * 
		  * Following is from dcm4che 2 tools org.acr.Dicom.Utility#createAssociation
		  *      remoteAE = new NetworkApplicationEntity();
          *      localAE = new NetworkApplicationEntity();
          *      localAE.setAETitle(getLocalAETitle());
          *      NetworkConnection remoteConn = new NetworkConnection();
          *      remoteConn.setHostname(remote.getAddress());
          *      remoteConn.setPort(remote.getPort());
          *      remoteAE.setAETitle(remote.getTitle());
          *      remoteAE.setInstalled(true);
          *      remoteAE.setAssociationAcceptor(true);
          *      remoteAE.setNetworkConnection(new NetworkConnection[] { remoteConn });
          *      NetworkConnection localConn = new NetworkConnection();
          *      localAE.setAETitle(Configuration.getLocalTitle());
          *      localAE.setAssociationInitiator(true);
          *      localAE.setNetworkConnection(localConn);
          *      device.setNetworkApplicationEntity(localAE);
          *      device.setNetworkConnection(localConn);
          *      
          *      
          *      from connect
          *      
          *      assoc = localAE.connec(remoteAE, executor);
          *      
          *      
          *      from close
          *      
          *      assoc.release(true);
		  */
		 rq.setCalledAET(targetAE);
		 remote.setHostname(targetAddress);
		 remote.setPort(targetPort);
		 /*
		  * configureBind - set local
		  */
		 localAE.setAETitle("LOCAL");
		 conn.setHostname("127.0.0.1");
		 conn.setPort(11112);
		 /*
		  * configure
		  */
	}

}
