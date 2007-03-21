package gov.nih.nci.cagrid.introduce.statistics;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StatisticsServer {
	private static final String magicNumber = "!777!";
	private static final String LOG_FILE = "introduceStatistics.log";
	private int port;
	private DatagramSocket dsocket;
	private Thread processThread;


	public StatisticsServer() {
		this.port = CommonTools.getStatisticPort();
	}


	public void start() throws SocketException {

		File logFile = new File(LOG_FILE);
		FileWriter wr = null;
		try {
			wr = new FileWriter(logFile, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		final FileWriter fw = wr;

		// Create a socket to listen on the port.
		dsocket = new DatagramSocket(port);

		this.processThread = new Thread(new Runnable() {

			public void run() {

				// Create a buffer to read datagrams into. If a
				// packet is larger than this buffer, the
				// excess will simply be discarded!
				byte[] buffer = new byte[2048];

				// Create a packet to receive data into the buffer
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				// Now loop forever, waiting to receive packets and printing
				// them.
				while (true) {
					// Wait to receive a datagram
					try {
						dsocket.receive(packet);

						// Convert the contents to a string, and display them
						// and log them
						String msg = new String(buffer, 0, packet.getLength());
						if (msg.indexOf(magicNumber) == 0) {
							msg = msg.substring(5);
							SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							Date date = new Date(System.currentTimeMillis());
							System.out.println(formatter.format(date) + "\n\tHOST:\t\t"
								+ packet.getAddress().getHostName() + msg + "\n");
							fw.append(formatter.format(date) + "\n\tHOST:\t\t" + packet.getAddress().getHostName()
								+ msg + "\n\n");
							fw.flush();
						}
						// Reset the length of the packet before reusing it.
						packet.setLength(buffer.length);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});

		this.processThread.start();

	}


	public static void main(String[] args) {
		StatisticsServer server = new StatisticsServer();
		try {
			server.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
