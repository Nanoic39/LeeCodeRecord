class RecentCounter {
    // 定义队列
    private Queue<Integer> queue;

    public RecentCounter() {
        queue = new LinkedList<>();
    }

    public int ping(int t) {
        // 将当前时间加入队尾
        queue.offer(t);

        // 清理队首的过期请求
        while(queue.peek() < t - 3000) {
            // 弹出队首元素
            queue.poll();
        }

        // 返回队列长度
        return queue.size();
    }
}

/**
 * Your RecentCounter object will be instantiated and called as such:
 * RecentCounter obj = new RecentCounter();
 * int param_1 = obj.ping(t);
 */