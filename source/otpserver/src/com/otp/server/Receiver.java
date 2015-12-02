package com.otp.server;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.otp.cipher.OneTimePadCipher;

/**
 * Class used to receive encrypted messages using sockets
 * 
 * @author Alejandro García
 *
 */
public class Receiver {

	/** Cipher */
	private static OneTimePadCipher otp = new OneTimePadCipher();

	/**
	 * <pre>
	 * Instructions
	 * 1.- Open a command shell.
	 * 2.- Go to the folder where the server jar (optserver.jar) was unzipped, using the cd command
	 * 3.- Execute the following command:
	 * java -jar optserver.jar -p port-number -k key
	 * </pre>
	 * 
	 * @param args
	 *            Arguments
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String args[]) throws Exception {
		int port = 0;
		String key = "";
		if (args.length != 4) {
			System.out.println("Error: Missing paramerers.");
			System.out.println("Usage: java -p <port-number> -k <key>");
			System.exit(1);
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("Error: Unable to parse port number");
			System.exit(1);
		}
		key = args[3];
		if (key.length() != 8 || !key.matches("-?[0-9a-fA-F]+")) {
			System.out.println("Error: Key must be a 32-bit hexadecimal");
			System.exit(1);
		}

		// loop
		while (true) {
			receiveMessage(port, key);
		}
	}

	/**
	 * Receive an encrypted message from a client
	 * 
	 * @param port
	 *            Port
	 * @param key
	 *            Hex key
	 * @throws Exception
	 *             Exception
	 */
	public static void receiveMessage(int port, String key) throws Exception {
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Listening:");
		Socket s = ss.accept();
		byte[] message = null;
		DataInputStream dIn = new DataInputStream(s.getInputStream());
		s.getRemoteSocketAddress().toString();
		int length = dIn.readInt(); // read length of incoming message
		if (length > 0) {
			message = new byte[length];
			dIn.readFully(message, 0, message.length); // read the message
			String decrypted = otp.decrypt(key, message);
			System.out.println("Incoming Message:");
			System.out.println("Remote Address: "
					+ s.getRemoteSocketAddress().toString());
			System.out.println("Encrypted Message: " + message);
			System.out.println("Decryted Message: " + decrypted);

		}
		s.close();
		ss.close();
	}
}
