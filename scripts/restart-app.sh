#!/bin/bash

# 确保 Java 在 PATH 中
export JAVA_HOME=/root/java/jdk-22.0.1
export PATH=$JAVA_HOME/bin:$PATH

# 使用环境变量配置
# export SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
# export SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
# export SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
# export SERVER_PORT=$SERVER_PORT

#export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/jiaoji
#export SPRING_DATASOURCE_USERNAME=jiaoji
#export SPRING_DATASOURCE_PASSWORD=':ex.RSTcgF3M!Ls'
#export SERVER_PORT=6981

if ! command -v java &> /dev/null; then
    echo "Java could not be found"
    exit 1
fi

# 进入应用程序目录
cd /www/wwwroot/jiaoji || { echo "Failed to change directory"; exit 1; }

# 启动新的进程
echo "Starting application..."
nohup java -jar jiaoji.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/jiaoji \
  --spring.datasource.username=jiaoji \
  --spring.datasource.password=':ex.RSTcgF3M!Ls' \
  --server.port=6981 > jiaoji.log 2>&1 &

java -jar jiaoji.jar --spring.datasource.url=jdbc:mysql://localhost:3306/jiaoji --spring.datasource.username=jiaoji --spring.datasource.password=':ex.RSTcgF3M!Ls' --server.port=6981

if [ $? -ne 0 ]; then
  echo "Failed to start the new application process. Check jiaoji.log for details."
  exit 1
fi

echo "Application started successfully."