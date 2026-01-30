package Tencent.LeeCode.Graph_DFS_787;

import java.util.List;
import java.util.ArrayList;

class Solution {
    // DFS算法，核心为：从源点开始，递归地探索所有可能的路径，直到找到目标点或探索完所有路径
    // 时间复杂度：O(n^k)，其中n为节点数，k为最大中转次数
    // 空间复杂度：O(n)，其中n为节点数，主要为递归栈的空间
    // 剪枝：1. 当当前路径长度超过k+1时，直接返回；2. 当当前路径长度等于k+1时，判断是否到达目标点
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // 1. 构建邻接表
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] flight : flights) {
            int u = flight[0], v = flight[1], w = flight[2];
            graph[u].add(new int[]{v, w});
        }
        // 2. DFS
        int[] minPrice = new int[]{Integer.MAX_VALUE};
        dfs(graph, src, dst, k + 1, 0, minPrice);
        return minPrice[0] == Integer.MAX_VALUE ? -1 : minPrice[0];
    }

    // DFS递归函数，参数为：邻接表、当前节点、目标节点、最大中转次数、当前路径长度、最小价格数组
    private void dfs(List<int[]>[] graph, int u, int dst, int k, int price, int[] minPrice) {
        // 1. 终止条件：1）当前路径长度超过k+1，直接返回；2）当前路径长度等于k+1，判断是否到达目标点，若到达则更新最小价格，否则直接返回
        if (k < 0) return;
        if (u == dst) {
            minPrice[0] = Math.min(minPrice[0], price);
            return;
        }
        // 2. 递归探索
        for (int[] next : graph[u]) {
            int v = next[0], w = next[1];
            if (price + w > minPrice[0]) continue; // 剪枝1：当前价格大于最小价格，直接返回
            dfs(graph, v, dst, k - 1, price + w, minPrice);
        }
    }
}
