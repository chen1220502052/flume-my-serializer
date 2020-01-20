# flume-my-serializer

flume1.9 + hbase serializer 自定义rowkey
resources/flume 下有flume的配置文件以及flume的启动文件，properties文件放到flume/conf下，
sh文件放到flume/bin下, env.sh文件配置了habse lib的路径到flume的classpath, 这是由于flume
在使用hbase sink的时候要用hbase的lib包
使用mvn package -DskipTests 打成包，直接放到flume/lib文件下即可
注意：habase，zookeeper和flume在一台机器上， flume默认连接本地zookeeper：2181地址端口,
打包，运行startup.sh，可以对flume进行测试
