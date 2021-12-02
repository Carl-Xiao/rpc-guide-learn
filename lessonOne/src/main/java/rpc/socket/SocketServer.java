package rpc.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketServer {

    private static final int RECV_BUF_SIZE = 1024;

    public static void main(String[] args) {
        int servPort = 8088;
        ServerSocket servSock = null;
        try {
            servSock = new ServerSocket(servPort);
            int recvMsgSize;
            byte[] receiveBuf = new byte[RECV_BUF_SIZE];
            while (true) {
                Socket clntSock = servSock.accept();
                SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
                System.out.println("Handling client at " + clientAddress);
                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();
                while ((recvMsgSize = in.read(receiveBuf)) != -1) {
                    out.write(receiveBuf, 0, recvMsgSize);
                }
                clntSock.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
