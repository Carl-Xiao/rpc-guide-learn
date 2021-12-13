package com.rpc.remote;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @description:
 * @author：carl
 * @date: 2021/12/4
 */
@Slf4j
public class RpcClient {
    public Object sendRpcRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.info("occur exception:", e);
        }
        return null;
    }

    public static void main(String[] args) {
        RpcProxy rpcClientProxy = new RpcProxy("127.0.0.1", 9999);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello("this is a test");
        System.out.println(hello);
    }

}
