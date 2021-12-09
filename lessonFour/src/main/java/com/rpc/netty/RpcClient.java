package com.rpc.netty;

import com.rpc.model.RpcRequest;

public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
