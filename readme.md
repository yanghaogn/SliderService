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
2. 通过thrift和slider-service-client通信

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


##將來
reference [SLIDER-719](https://issues.apache.org/jira/browse/SLIDER-719) 和[SLIDER-752](https://issues.apache.org/jira/browse/SLIDER-752)

需要对信息的获取进行重构，如果上面提供的API足够好的话，SliderService可能被废弃掉，使用自带的REST API的缺点是，需要知道hadoop-conf

