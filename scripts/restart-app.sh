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
    fi
fi

# 进入应用程序目录
cd /www/wwwroot/jiaoji || { echo "Failed to change directory"; exit 1; }

# 启动新的进程
echo "Starting application..."
nohup java -jar jiaoji.jar \
  --spring.datasource.url=$SPRING_DATASOURCE_URL \
  --spring.datasource.username=$SPRING_DATASOURCE_USERNAME \
  --spring.datasource.password=$SPRING_DATASOURCE_PASSWORD \
  --server.port=$SERVER_PORT > jiaoji.log 2>&1 &

# 等待几秒钟以确保应用程序有足够时间启动
sleep 10

# 检查应用程序是否成功启动
if lsof -ti:$SERVER_PORT; then
    echo "Application started successfully on port $SERVER_PORT."
else
    echo "Failed to start application on port $SERVER_PORT. Check jiaoji.log for details."
    exit 1
fi
