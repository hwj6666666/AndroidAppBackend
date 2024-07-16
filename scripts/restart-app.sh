#!/bin/bash

# 使用环境变量配置
export SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
export SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
export SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
export SERVER_PORT=$SERVER_PORT

# 进入应用程序目录
cd /www/wwwroot/jiaoji

# 停止旧的进程
pkill -f 'java -jar jiaoji-0.0.1-SNAPSHOT.jar'

# 备份旧版本的JAR文件
# if [ -f "jiaoji-0.0.1-SNAPSHOT.jar" ]; then
#   mv jiaoji-0.0.1-SNAPSHOT.jar jiaoji.jar.bak_$(date +%Y%m%d%H%M%S)
# fi

# 启动新的进程
nohup java -jar jiaoji-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url="$SPRING_DATASOURCE_URL" \
  --spring.datasource.username="$SPRING_DATASOURCE_USERNAME" \
  --spring.datasource.password="$SPRING_DATASOURCE_PASSWORD" \
  --server.port="$SERVER_PORT" > /dev/null 2>&1 &
