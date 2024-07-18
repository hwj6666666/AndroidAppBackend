#!/bin/bash

# 进入应用程序目录
cd /www/wwwroot/jiaoji || exit

# 查找绑定到指定端口的进程并结束它
pid=$(lsof -ti:6981)
if [ -n "$pid" ]; then
    kill $pid
    echo "Stopped process on port 6981."
else
    echo "No process found on port 6981."
fi

sleep 5  # Wait for 5 seconds to ensure the process has been terminated
if lsof -ti:6981; then
    echo "Port 6981 is still in use."
else
    echo "Port 6981 is now free."
fi

# 如果需要详细调试信息，可以取消以下注释以查看当前运行的Java进程
# echo "Current Java processes:"
# ps aux | grep java

# 备份旧版本的JAR文件
# if [ -f "jiaoji-0.0.1-SNAPSHOT.jar" ]; then
#   mv jiaoji-0.0.1-SNAPSHOT.jar jiaoji.jar.bak_$(date +%Y%m%d%H%M%S)
# fi
