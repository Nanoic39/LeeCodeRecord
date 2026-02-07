package Tencent.LeeCode.Graph_DFS_787;

import java.util.Arrays;

class Solution {
    // 使用 Bellman-Ford 算法 (动态规划)
    // 原始 DFS 在最坏情况下复杂度为指数级，会导致超时
    // Bellman-Ford 时间复杂度：O(K * E)，K 为中转次数，E 为航班数
    // 空间复杂度：O(N)
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // 使用一个足够大的值表示不可达
        int INF = Integer.MAX_VALUE; 
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // k 次中转意味着路径上最多有 k + 1 条边
        // 进行 k + 1 次松弛操作
        for (int i = 0; i <= k; i++) {
            // 必须使用 clone 数组，确保每一轮的更新只基于上一轮的结果
            // 否则可能在一次迭代中使用了多次跳跃（即当前轮次更新后的值被立刻用于后续更新）
            int[] nextDist = Arrays.copyOf(dist, n);
            for (int[] flight : flights) {
                int u = flight[0];
                int v = flight[1];
                int w = flight[2];
                
                if (dist[u] != INF) {
                    nextDist[v] = Math.min(nextDist[v], dist[u] + w);
                }
            }
            dist = nextDist;
        }

        return dist[dst] == INF ? -1 : dist[dst];
    }
}
