## 通用工具


```xml
<dependency>
    <groupId>com.hupubao</groupId>
    <artifactId>hupubao-common-utils</artifactId>
    <version>${hupubao-common-utils.version}</version>
</dependency>
```

版本自行查看：[https://mvnrepository.com/artifact/com.hupubao/hupubao-common-utils](https://mvnrepository.com/artifact/com.hupubao/hupubao-common-utils)

如需使用[http请求工具](https://github.com/ysdxz207/hupubao-common-http)，可增加：
```xml
<dependency>
    <groupId>com.hupubao</groupId>
    <artifactId>hupubao-common-http</artifactId>
    <version>${hupubao-common-http.version}</version>
</dependency>
```

- [日志记录工具](./doc/LogReqResArgs.MD)


### 历史版本

- 2020-04-28 版本：0.0.7

1. 可重复读Request直接使用spring的ContentCachingRequestWrapper
2. 私有化RSA工具的构造方法