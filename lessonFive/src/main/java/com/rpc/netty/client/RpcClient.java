package com.rpc.netty.client;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;

import java.util.concurrent.CompletableFuture;

public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);

    CompletableFuture<RpcResponse> sendFutureRpcRequest(RpcRequest rpcRequest) ;
}
