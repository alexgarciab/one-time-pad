package com.otp.client;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.otp.cipher.OneTimePadCipher;

/**
 * Class used to send encrypted messages using sockets Instructions:
 * 
 * @author Alejandro García
 *
 */
public class Sender {

	/** Cipher */
	private static OneTimePadCipher otp = new OneTimePadCipher();

	/**
	 * <pre>
	 * Instructions:
	 * 1.- Open a command shell.
	 * 2.- Go to the folder where the client jar (optclient.jar) was unzipped, using the cd command
	 * 3.- Execute the following command:
	 * 		java -jar optclient.jar -p port-number -t IP/hostname -k key -s message
	 * </pre>
	 * 
	 * @param args
	 *            Arguments
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String args[]) throws Exception {
		int port = 0;
		String host = "";
		String key = "";
		String msg = "";
		if (args.length != 8) {
			System.out.println("Error: Wrong paramerers.");
			System.out.println("Usage: java -jar -p <port-number> -t "
					+ "<IP/hostname> -k <key> -s <message>");
			System.exit(1);
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("Error: Unable to parse port number");
			System.exit(1);
		}
		host = args[3];
		key = args[5];
		if (key.length() != 8 || !key.matches("-?[0-9a-fA-F]+")) {
			System.out.println("Error: Key must be a 32-bit hexadecimal");
			System.exit(1);
		}
		msg = args[7];

		// Encrypt the message using the key
		byte[] encrypted = otp.encrypt(key, msg);
		// Send message to server
		sendMessage(encrypted, host, port);

	}

	/**
	 * Send a string message to a {@link ServerSocket}
	 * 
	 * @param message
	 *            Message
	 * @param host
	 *            Host
	 * @param port
	 *            Port
	 * @throws Exception
	 *             Exception
	 */
	public static void sendMessage(byte[] message, String host, int port)
			throws Exception {
		Socket s = new Socket(host, port);
		DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
		dOut.writeInt(message.length); // write length of the message
		dOut.write(message);
		s.close();

	}
}
