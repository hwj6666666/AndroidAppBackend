#!/bin/bash

# 确保 Java 在 PATH 中
# which java
 export JAVA_HOME=/root/java/jdk-22.0.2
 export PATH=$JAVA_HOME/bin:$PATH

# 检查 Java 是否可用
if ! command -v java &> /dev/null; then
    echo "Java could not be found"
    exit 1
fi

# 检查端口是否已被占用
if lsof -ti:$SERVER_PORT; then
    echo "Port $SERVER_PORT is already in use. Attempting to stop the process."
    kill $(lsof -ti:$SERVER_PORT)
    sleep 5  # 等待几秒以确保进程已被结束
    if lsof -ti:$SERVER_PORT; then
        echo "Failed to free up the port $SERVER_PORT. Exiting."
        exit 1
    else
        echo "Successfully freed up the port $SERVER_PORT."
    fi
else
    echo "Port $SERVER_PORT is free. No process needs to be terminated."
fi

# 进入应用程序目录
cd /www/wwwroot/jiaoji || { echo "Failed to change directory"; exit 1; }

# 启动新的进程
echo "Starting application..."
nohup java -jar jiaoji-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=$SPRING_DATASOURCE_URL \
  --spring.datasource.username=$SPRING_DATASOURCE_USERNAME \
  --spring.datasource.password=$SPRING_DATASOURCE_PASSWORD \
  --server.port=$SERVER_PORT > jiaoji.log 2>&1 &

# 循环等待并检查应用程序是否成功启动
for i in {1..10}; do
    if lsof -ti:$SERVER_PORT; then
        echo "Application started successfully on port $SERVER_PORT."
        exit 0
    else
        echo "Waiting for application to start on port $SERVER_PORT... ($i/10)"
        sleep 2
    fi
done

# 如果循环结束后仍未启动，则报告失败
echo "Failed to start application on port $SERVER_PORT. Check jiaoji.log for details."
exit 1
