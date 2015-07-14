#!/bin/sh
export HADOOP_CLASSPATH=$(pwd)/Part2.jar
hdfs dfs -rm -r -skipTrash /user/mfarova/output
hadoop jar Part2.jar Part2 /user/mfarova/input/ /user/mfarova/output
