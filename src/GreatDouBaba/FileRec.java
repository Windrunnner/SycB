package GreatDouBaba;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileRec {
	 
    private int listenPort;
 
    private String savePath;
 
    public FileRec (int listenPort, String savePath) throws IOException {
        this.listenPort = listenPort;
        this.savePath = savePath;
 
        File file = new File(savePath);
        if (!file.exists() && !file.mkdirs()) {
            throw new IOException("Unable to create " + savePath);
        }
    }
 
    public void start() {
        new ListenThread().start();
    }
 
    public static int b2i(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }
 
 
    private class ListenThread extends Thread {
 
        @Override
        public void run() {
            try {
                ServerSocket server = new ServerSocket(listenPort);
 
                while (true) {
                    Socket socket = server.accept();
                    new HandleThread(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class HandleThread extends Thread {
 
        private Socket socket;
 
        private HandleThread(Socket socket) {
            this.socket = socket;
        }
 
        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                readAndSave(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }
        private void readAndSave(InputStream is) throws IOException {
            String filename = getFileName(is);
            int file_len = readInteger(is);
            System.out.println("Receiving:" + filename + ", length: " + file_len);
 
            readAndSave0(is, savePath + filename, file_len);
 
            System.out.println("DONE: " + file_len + "byte.");
        }
 
        private void readAndSave0(InputStream is, String path, int file_len) throws IOException {
            FileOutputStream os = getFileOS(path);
            readAndWrite(is, os, file_len);
            os.close();
        }

        private void readAndWrite(InputStream is, FileOutputStream os, int size) throws IOException {
            byte[] buffer = new byte[4096];
            int count = 0;
            while (count < size) {
                int n = is.read(buffer);
                os.write(buffer, 0, n);
                count += n;
            }
        }

        private String getFileName(InputStream is) throws IOException {
            int name_len = readInteger(is);
            byte[] result = new byte[name_len];
            is.read(result);
            return new String(result);
        }

        private int readInteger(InputStream is) throws IOException {
            byte[] bytes = new byte[4];
            is.read(bytes);
            return b2i(bytes);
        }

        private FileOutputStream getFileOS(String path) throws IOException {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
 
            return new FileOutputStream(file);
        }
    }
}