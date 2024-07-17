#!/bin/bash

# 进入应用程序目录
cd /www/wwwroot/jiaoji

# 停止旧的进程
pkill -f 'java -jar jiaoji-0.0.1-SNAPSHOT.jar' || echo "No old application process found to stop."

# 如果需要详细调试信息，可以取消以下注释以查看当前运行的Java进程
# echo "Current Java processes:"
# ps aux | grep java

# 备份旧版本的JAR文件
# if [ -f "jiaoji-0.0.1-SNAPSHOT.jar" ]; then
#   mv jiaoji-0.0.1-SNAPSHOT.jar jiaoji.jar.bak_$(date +%Y%m%d%H%M%S)
# fi
