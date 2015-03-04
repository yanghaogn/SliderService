##编译

* 需要在本地install slider工程，这样就可以使用slider对应的maven工程

```
    <dependency>  
      <groupId>org.apache.slider</groupId>  
      <artifactId>slider-core</artifactId>  
      <version>${slider.version}</version>  
    </dependency>
```

##slider-service-server
服务端
1. 响应slider-service-client的请求,现在的请求都是在cluster对应的SliderAM的url后面进行拼接，获取该url的内容，并返回给client。如client请求的内容为" type: default, user: h_yanghao3, clustername: memcached1, content: ws/v1/slider/publisher/exports/servers?proxyapproved=true"，那么slider-service-server首先解析出cluster对应的sliderAM的url:[http://10.2.201.165:15401/proxy/application_1421659132440_0011/](http://10.2.201.165:15401/proxy/application_1421659132440_0011/),然后对其进行拼接，拼接成[http://10.2.201.165:15401/proxy/application_1421659132440_0011/ws/v1/slider/publisher/exports/servers?proxyapproved=true
](http://10.2.201.165:15401/proxy/application_1421659132440_0011/ws/v1/slider/publisher/exports/servers?proxyapproved=true
),并将该将结果返回给slider-service-client
2. 通过swift和slider-service-client通信

####例子
节点: lg-hadoop-build01
$git clone git@git.n.xiaomi.com:yanghao3/SliderService.git SliderService
$cd SliderService
$git pull
$mvn package
$export JAVA_OPTS="-cp /home/yanghao3/app/slider/hadoop-conf:sliderservice-server/target/lib/*:sliderservice-server/target/SliderService.jar -Djava.security.krb5.conf=/etc/krb5-hadoop.conf"
$java ${JAVA_OPTS} Server 20050 h_yanghao3@XIAOMI.HADOOP ~/h_yanghao3.keytab

##slider-service-client
####前提
 需要指定Slider-Service服务的位置  
如`System.setProperty(SliderServiceClientConfigKey.SERVICE_ADDRESS, "lg-hadoop-build01.bj:20050");`
####使用
1. 通过类SliderServiceClient获取user、clustername对应的信息
 1.　直接获取host:port信息：你需要将host:port输出到ws/v1/slider/publisher/exports/servers才能使用，可参考[http://wiki.mioffice.cn/xmg/Metainfo.xml#](http://wiki.mioffice.cn/xmg/Metainfo.xml#)对metainfo.xml进行配置
 2. 其他使用方式，是通过在SliderAM对应的url后面进行拼接，并获取该url返回的数据
2. 用户使用简写，如h_yanghao3

##app-package
其中包括用户自己编写的客户端，需要依赖slider-service-client模块，模块中的SliderServiceClient可以获取cluster信息，如host:port列表等

构建方法可以参考memcached模块

##测试
####压力测试`TestSliderServiceClient.testPressure()`
经过下面的问题后，得出的结论是客户端不能频繁的调用slider-service-client提供的方法。因为我们创建的是短连接，这会导致同时创建大量的socket对象，由于socket连接的限制，导致程序崩溃。
#####问题一
方法：client端同时开启多个线程，和Slider-Server交互
结果：客户端的线程数太大时，超过8000时，client会出现

```
10:38:07,492 ERROR TSocket:122 - Could not configure socket.
java.net.SocketException: Too many open files
	at java.net.Socket.createImpl(Socket.java:397)
	at java.net.Socket.getImpl(Socket.java:460)
	at java.net.Socket.setSoLinger(Socket.java:900)
	at org.apache.thrift.transport.TSocket.initSocket(TSocket.java:118)
	at org.apache.thrift.transport.TSocket.<init>(TSocket.java:109)
	at org.apache.thrift.transport.TSocket.<init>(TSocket.java:94)
	at com.xiaomi.infra.slider.service.client.SliderServiceClientHandler.refreshRequestClient(SliderServiceClientHandler.java:82)
	at com.xiaomi.infra.slider.service.client.SliderServiceClientHandler.getResponse(SliderServiceClientHandler.java:53)
	at com.xiaomi.infra.slider.service.client.SliderServiceClient.getResponse(SliderServiceClient.java:91)
	at com.xiaomi.infra.slider.service.client.SliderServiceClient.isRunning(SliderServiceClient.java:24)
	at com.xiaomi.infra.slider.service.client.TestSliderServiceClient$1.run(TestSliderServiceClient.java:40)
```
原因：客户端的文件数不能打开太多
初步解决方案：[java.net.SocketException+Too+many+open+files](http://doc.nuxeo.com/display/KB/java.net.SocketException+Too+many+open+files)

这个方案暂时增大了同时打开的文件数，但还是没有从根本上解决上面的问题，所以对程序的一种建议是，在client节点上，不要同时打开过多的Socket，最好限制在8000以下
#####问题二
上面的问题解决后，可能会出现另外一个问题
```
ConnectException: 连接超时
	at java.net.PlainSocketImpl.socketConnect(Native Method)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:339)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:200)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:182)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:391)
	at java.net.Socket.connect(Socket.java:579)
	at org.apache.thrift.transport.TSocket.open(TSocket.java:180)
	... 5 more
org.apache.thrift.transport.TTransportException: java.net.ConnectException: 连接超时
	at org.apache.thrift.transport.TSocket.open(TSocket.java:185)
	at com.xiaomi.infra.slider.service.client.SliderServiceClientHandler.refreshRequestClient(SliderServiceClientHandler.java:83)
	at com.xiaomi.infra.slider.service.client.SliderServiceClientHandler.getResponse(SliderServiceClientHandler.java:53)
	at com.xiaomi.infra.slider.service.client.SliderServiceClient.getResponse(SliderServiceClient.java:91)
	at com.xiaomi.infra.slider.service.client.SliderServiceClient.isRunning(SliderServiceClient.java:24)
	at com.xiaomi.infra.slider.service.client.TestSliderServiceClient$1.run(TestSliderServiceClient.java:41)
```
原因可能是sliderservice同时响应的服务数目有限，因此导致了这个问题。
#####问题三：太多处于TIME_WAIT状态的socket导致NoRouteToHostException
连续多次压力测试时，会出现下面的问题
```
16:41:18,540 ERROR SliderServiceClientHandler:88 - org.apache.thrift.transport.TTransportException: java.net.NoRouteToHostException: 无法指定被请求的地址
16:41:18,540 ERROR SliderServiceClientHandler:63 - cannot connect to any address in [lg-hadoop-build01.bj/10.2.201.247:20050]
16:41:18,540 ERROR SliderServiceClientHandler:88 - org.apache.thrift.transport.TTransportException: java.net.NoRouteToHostException: 无法指定被请求的地址
org.apache.thrift.transport.TTransportException: java.net.　: 无法指定被请求的地址
	at org.apache.thrift.transport.TSocket.open(TSocket.java:185)
	at com.xiaomi.infra.slider.service.client.SliderServiceClientHandler.refreshRequestClient(SliderServiceClientHandler.java:83)
	at com.xiaomi.infra.slider.service.client.SliderServiceClientHandler.getResponse(SliderServiceClientHandler.java:53)
	at com.xiaomi.infra.slider.service.client.SliderServiceClient.getResponse(SliderServiceClient.java:91)
	at com.xiaomi.infra.slider.service.client.SliderServiceClient.isRunning(SliderServiceClient.java:24)
	at com.xiaomi.infra.slider.service.client.TestSliderServiceClient$1.run(TestSliderServiceClient.java:46)
Caused by: java.net.NoRouteToHostException: 无法指定被请求的地址
	at java.net.PlainSocketImpl.socketConnect(Native Method)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:339)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:200)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:182)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:391)
	at java.net.Socket.connect(Socket.java:579)
	at org.apache.thrift.transport.TSocket.open(TSocket.java:180)
	... 5 more
```
执行'$netstat -a | grep TIME_WAIT'，会发现很多socket连接处于TIME_WAIT状态
<del>**网上的一种解决方案**</del>
1. 根据TCP/IP协议，连接断开之后，端口不会立刻被释放，而是处于TIME_WAIT状态，等待两分钟半后，才会被释放掉，才能被新连接使用。
而性能测试并发了3W连接，在3W连接因超时而关闭后，grinder又迅速请求3W连接，这时，已被占用的端口号未被释放，部分新建连接因为无法分配到端口号而失败。
2. 通过配置TCP_TW_REUSE参数，来释放TIME_WAIT状态的端口号给新连接使用
TCP_TW_REUSE
This allows reusing sockets in TIME_WAIT state for new connections when it is safe from protocol viewpoint. Default value is 0 (disabled). It is generally a safer alternative to tcp_tw_recycle　
参考资料：
http://www.speedguide.net/articles/linux-tweaking-121
3. 设置参数后，重新测试，不再出现异常情况。

**缺点**
参考[http://comments.gmane.org/gmane.comp.db.cassandra.user/1765](http://comments.gmane.org/gmane.comp.db.cassandra.user/1765)
大意是说将lingo设置成０后，可能导致更大的问题，因为
```
I don't know the particulars of java implementation, but if it works the same way as Unix native socket API, then I would not recommend setting linger to zero.

SO_LINGER option with zero value will cause TCP connection to be aborted immediately as soon as the socket is closed. That is, (1) remaining data in the send buffer will be discarded, (2) no proper disconnect handshake and (3) receiving end will get TCP reset.

Sure this will avoid TIME_WAIT state, but TIME_WAIT is our friend and is there to avoid packets from old connection being delivered to new incarnation of the connection. Instead of avoiding the state, the
application should be changed so that TIME_WAIT will not be a problem. How many open files you can see when the exception happens? Might be that you're out of file descriptors.
```


##將來
reference [SLIDER-719](https://issues.apache.org/jira/browse/SLIDER-719) 和[SLIDER-752](https://issues.apache.org/jira/browse/SLIDER-752)，看评论

需要对信息的获取进行重构，如果上面提供的API足够好的话，SliderService可能被废弃掉，使用自带的REST API的缺点是，需要知道hadoop-conf

