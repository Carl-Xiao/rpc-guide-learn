package rpc.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) throws IOException {
        String server = "127.0.0.1";
        byte[] data = "Hello World!".getBytes();
        int servPort = 8088;
        Socket socket = new Socket(server, servPort);
        System.out.println("Connecting to server…");
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        out.write(data);
        int totalBytesRcvd = 0;
        int bytesRcvd;
        while (totalBytesRcvd < data.length) {
            bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd);
            totalBytesRcvd += bytesRcvd;
        }
        System.out.println("Received: " + new String(data));
        socket.close();
    }
}
