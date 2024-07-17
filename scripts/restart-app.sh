#!/bin/bash

# 使用环境变量配置
# export SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
# export SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
# export SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
# export SERVER_PORT=$SERVER_PORT

#export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/jiaoji'
#export SPRING_DATASOURCE_USERNAME='root'
#export SPRING_DATASOURCE_PASSWORD=':ex.RSTcgF3M!Ls'
#export SERVER_PORT=6981

# 进入应用程序目录
#cd /www/wwwroot/jiaoji || exit 1

# 启动新的进程
#jiaoji-0.0.1-SNAPSHOT.jar
nohup java -jar jiaoji.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/jiaoji \
  --spring.datasource.username=jiaoji \
  --spring.datasource.password=':ex.RSTcgF3M!Ls' \
  --server.port=6981 > /dev/null 2>&1 &

if [ $? -ne 0 ]; then
  echo "Failed to start the new application process."
  exit 1
fi
