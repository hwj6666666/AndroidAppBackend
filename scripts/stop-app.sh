#!/bin/bash

# 进入应用程序目录
cd /www/wwwroot/jiaoji || { echo "Failed to change directory to /www/wwwroot/jiaoji"; exit 1; }

# 查找绑定到指定端口的进程并尝试结束它
pid=$(lsof -ti:6981)
while [ -n "$pid" ]; do
    echo "Stopping process $pid on port 6981..."
    kill $pid
    sleep 5  # 等待5秒后重新检查端口状态

    # 再次检查端口是否释放
    pid=$(lsof -ti:6981)
    if [ -n "$pid" ]; then
        echo "Attempt to stop process $pid failed. Trying again..."
    else
        echo "Stopped process successfully. Port 6981 is now free."
    fi
done

if [ -z "$pid" ]; then
    echo "No process found on port 6981 or all processes successfully terminated."
fi

# 如果需要详细调试信息，可以取消以下注释以查看当前运行的Java进程
# echo "Current Java processes:"
# ps aux | grep java

# 备份旧版本的JAR文件
# if [ -f "jiaoji-0.0.1-SNAPSHOT.jar" ]; then
#   mv jiaoji-0.0.1-SNAPSHOT.jar jiaoji.jar.bak_$(date +%Y%m%d%H%M%S)
# fi
