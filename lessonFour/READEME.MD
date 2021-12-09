rpc的核心之一
序列化的协议
- kyro？
- grpc？
- hession?
- jdk默认？

# netty
## 消息是如何传输
- 消息体如何设计
- 消息格式的约定
## pipeline的处理流过程 

*核心一定要理解正在处理流*

> 以本次demo为例
client 添加的顺序为 decoder(1) encoder(2) inhandler(3)
server 添加的顺序为 decoder(4) encoder(5) inhandler(6)

<b>一个容易忽视的细节decoder/encoder的父类 ChannelInboundHandlerAdapter/ChannelOutboundHandlerAdapter</b>

- 发起
发送方
client发送消息的时候处理逻辑  encoder (2)
接受方
server发送消息的时候处理逻辑  decoder(4) inhandler(6) 

- 响应
发送方
server回复消息 encoder(5)
接受方
client接受消息 decoder(1) inhandler(3)



## 小总结

> 本次设计中引用了netty替代传统io的socket通信, 使用压缩协议提高传输效率

> 流程图有问题,理解好像有误






