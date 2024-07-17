#!/bin/bash

# 进入应用程序目录
cd /www/wwwroot/jiaoji

# 停止旧的进程
pkill -f 'java -jar jiaoji-0.0.1-SNAPSHOT.jar'
#if [ $? -ne 0 ]; then
#  echo "Failed to stop the old application process."
#  exit 1
#fi

# 备份旧版本的JAR文件
# if [ -f "jiaoji-0.0.1-SNAPSHOT.jar" ]; then
#   mv jiaoji-0.0.1-SNAPSHOT.jar jiaoji.jar.bak_$(date +%Y%m%d%H%M%S)
# fi
