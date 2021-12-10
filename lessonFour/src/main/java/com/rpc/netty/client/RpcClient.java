package com.rpc.netty.client;

import com.rpc.model.RpcRequest;

public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
