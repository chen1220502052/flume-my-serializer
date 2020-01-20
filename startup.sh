#!/bin/sh
VERSION=1.0-SNAPSHOT
java -Xmx216m -Xms512m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -cp .:flume-serializer-my-${VERSION}.jar:./lib/* org.example.flume.client.FlumeClient