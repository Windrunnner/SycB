package GreatDouBaba;

import java.io.*;
import java.net.*;

import android.util.Log;

public class FileSender {
	public static byte[] i2b(int i) {
		return new byte[] { (byte) ((i >> 24) & 0xFF),
				(byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF),
				(byte) (i & 0xFF) };
	}

	public void sendFile(String hostname, int port, String filepath)
			throws IOException {
		File file = new File(filepath);
		FileInputStream is = new FileInputStream(filepath);
		Socket socket = new Socket(hostname, port);
		OutputStream os = socket.getOutputStream();

		int length = (int) file.length();
		Log.d("Sender", "Sending: " + file.getName() + ", Length: " + length);
		writeFileName(file, os);
		writeFileContent(is, os, length);
		is.close();
		os.close();
	}

	private void writeFileContent(InputStream is, OutputStream os, int length)
			throws IOException {
		os.write(i2b(length));
		byte[] buffer = new byte[4096];
		int size;
		while ((size = is.read(buffer)) != -1) {
			os.write(buffer, 0, size);
		}
	}

	private void writeFileName(File file, OutputStream os) throws IOException {
		byte[] fn_bytes = file.getName().getBytes();

		os.write(i2b(fn_bytes.length));
		os.write(fn_bytes);
	}
}
