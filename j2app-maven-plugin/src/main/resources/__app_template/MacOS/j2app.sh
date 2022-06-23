#!/bin/zsh
dateYMD=$(date +%Y-%m-%d-%H-%H-%S)
logDir=~/Library/Logs/'#{appName}'
if [ ! -d ${logDir}  ];then
  mkdir ${logDir}
else
fi
echo 'logDir:'${logDir}
logFile=${logDir}/${dateYMD}.log
echo 'logFile:'${logFile}
shDir=`dirname $0`
java -jar ${shDir}/app.jar >${logFile} 2>&1 &