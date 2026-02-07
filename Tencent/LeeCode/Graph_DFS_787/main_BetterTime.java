package Tencent.LeeCode.Graph_DFS_787;

import java.util.Arrays;

class Solution {
    // 极致优化版 Bellman-Ford
    // 使用一维数组扁平化存储航班信息，提高 CPU 缓存命中率
    // 严格控制 K 次松弛，使用 prev 数组防止同层串联
    // 提前剪枝，无更新时跳出
    // 时间复杂度：O(K * E)，但常数极小
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        final int INF = 0x3f3f3f3f; // 足够大且防止加法溢出
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // 扁平化 flights 数组，提升访问速度
        // Java 的二维数组是对象数组，访问开销大
        int size = flights.length;
        int[] flat = new int[size * 3];
        int idx = 0;
        for (int[] f : flights) {
            flat[idx++] = f[0];
            flat[idx++] = f[1];
            flat[idx++] = f[2];
        }

        // K + 1 次松弛
        for (int i = 0; i <= k; i++) {
            // 备份当前状态，必须 clone，确保基于上一轮状态更新
            int[] prev = dist.clone();
            boolean changed = false;
            
            // 遍历所有边
            for (int j = 0; j < flat.length; j += 3) {
                int u = flat[j];
                int v = flat[j+1];
                int w = flat[j+2];

                if (prev[u] != INF && prev[u] + w < dist[v]) {
                    dist[v] = prev[u] + w;
                    changed = true;
                }
            }
            
            if (!changed) break;
        }

        return dist[dst] == INF ? -1 : dist[dst];
    }
}
