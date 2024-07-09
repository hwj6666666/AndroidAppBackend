package org.example.jiaoji.utils;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {
    private final ConcurrentHashMap<Integer, WeakReference<Lock>> lockMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LockManager() {
        // 每30分钟执行一次清理任务
        scheduler.scheduleAtFixedRate(this::cleanup, 30, 30, TimeUnit.MINUTES);
    }

    public Lock getLock(Integer topicId) {
        return lockMap.compute(topicId, (key, weakRefLock) -> {
            Lock lock = (weakRefLock != null) ? weakRefLock.get() : null;
            if (lock == null) {
                lock = new ReentrantLock();
                weakRefLock = new WeakReference<>(lock);
            }
            return weakRefLock;
        }).get();
    }

public void cleanup() {
    int originalSize = lockMap.size(); // 获取原始大小
    lockMap.entrySet().removeIf(entry -> entry.getValue().get() == null); // 移除值为null的条目
    int newSize = lockMap.size(); // 获取移除后的大小
    int removed = originalSize - newSize; // 计算被移除的条目数量
    if (removed > 0) {
        System.out.println("清理了 " + removed + " 个不再被引用的锁");
    }
}

    // 确保在应用关闭时停止scheduler，避免内存泄漏
    public void shutdown() {
        scheduler.shutdownNow();
    }
}