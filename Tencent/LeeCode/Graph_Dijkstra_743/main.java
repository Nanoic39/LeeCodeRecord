package Tencent.LeeCode.Graph_Dijkstra_743;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

class Solution {
    // Dijkstra算法，核心为：贪心选择当前距离源点最近的节点，逐步松弛更新其他节点的距离
    // 1. 构建邻接表：存储每个节点的出边（邻接节点 + 传递时间）
    // 2. 初始化距离数组：dist[] 表示源点到每个节点的距离，源点距离为0，其余节点初始化为最大值
    // 3. 优先队列：存储节点和当前距离，每次取出距离最小的节点
    // 4. 松弛操作：取出队首节点 u，遍历其所有邻接节点 v，若 dist[v] > dist[u] + 传递时间w，则更新 dist[v] 为
    // dist[u] + w，并将 (dist[v], v) 加入队列
    // 5. 计算结果：遍历 dist[] 数组，若存在节点距离仍为最大值（表示不可达），则返回 -1；否则返回 dist[] 数组中的最大值
    public int networkDelayTime(int[][] times, int n, int k) {
        // 1. 构建邻接表
        List<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] time : times) {
            int u = time[0], v = time[1], w = time[2];
            graph[u].add(new int[] { v, w });
        }
        // 2. 初始化距离数组
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;
        // 3. 优先队列
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[] { 0, k });
        // 4. 松弛操作
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[1], d = cur[0];
            if (d > dist[u])
                continue;
            for (int[] next : graph[u]) {
                int v = next[0], w = next[1];
                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[] { dist[v], v });
                }
            }
        }
        // 5. 计算结果
        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE)
                return -1;
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;

    }

}
