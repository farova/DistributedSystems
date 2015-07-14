#!/bin/sh
export HADOOP_CLASSPATH=$(pwd)/Part3.jar
hdfs dfs -rm -r -skipTrash /user/mfarova/output
hadoop jar Part3.jar Part3 /user/mfarova/input/ /user/mfarova/output
