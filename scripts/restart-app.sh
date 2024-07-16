#!/bin/bash

# 环境变量配置
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/jiaoji"
export SPRING_DATASOURCE_USERNAME="jiaoji"
export SPRING_DATASOURCE_PASSWORD=":ex.RSTcgF3M!Ls"
export SERVER_PORT=6981

# 进入应用程序目录
cd /www/wwwroot/jiaoji

# 停止旧的进程
pkill -f 'java -jar jiaoji.jar'

# 备份旧版本的JAR文件
if [ -f "jiaoji.jar" ]; then
  mv jiaoji.jar jiaoji.jar.bak_$(date +%Y%m%d%H%M%S)
fi

# 启动新的进程
nohup java -jar jiaoji.jar \
  --spring.datasource.url="$SPRING_DATASOURCE_URL" \
  --spring.datasource.username="$SPRING_DATASOURCE_USERNAME" \
  --spring.datasource.password="$SPRING_DATASOURCE_PASSWORD" \
  --server.port="$SERVER_PORT" > /dev/null 2>&1 &
