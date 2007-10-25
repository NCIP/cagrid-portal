package gov.nih.nci.cagrid.introduce.statistics;

import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.IntroduceEnginePropertiesManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class StatisticsClient {
	private static final String magicNumber = "!777!";

	public static void sendCreatedServiceStat(String introduceVersion, String name, String namespace, String extensions) {
		sendStat("\n\tACTION:\t\tcreated\n\tINTRODUCE:\t" + introduceVersion + "\n\tNAME:\t\t" + name + "\n\tNAMESPACE:\t" + namespace + "\n\tEXTENSIONS:\t" + extensions);
	}


	public static void sendModifiedServiceStat(String introduceVersion, String name, String namespace) {
		sendStat("\n\tACTION:\t\tmodified\n\tINTRODUCE:\t" + introduceVersion + "\n\tNAME:\t\t" + name + "\n\tNAMESPACE:\t" + namespace);
	}


	public static void sendDeployedServiceStat(String introduceVersion, String name, String namespace, String containerType) {
		sendStat("\n\tACTION:\t\tdeployed\n\tINTRODUCE:\t" + introduceVersion + "\n\tNAME:\t\t" + name + "\n\tNAMESPACE:\t" + namespace + "\n\tCONTAINER TYPE:\t" + containerType);
	}


	public static void sendStat(String message) {
		try {
			
			message = magicNumber + message;

			if (IntroduceEnginePropertiesManager.getCollectStats()) {

				// Get the internet address of the specified host
				InetAddress address = InetAddress.getByName(IntroduceEnginePropertiesManager.getStatisticSite());

				// Initialize a datagram packet with data and address
				DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address,
				    IntroduceEnginePropertiesManager.getStatisticPort());

				// Create a datagram socket, send the packet through it, close
				// it.
				DatagramSocket dsocket = new DatagramSocket();
				dsocket.send(packet);
				dsocket.close();
			}

		} catch (Exception e) {

		}
	}
}
