package Tencent.LeeCode.Greedy_Sorting_455;

import java.util.Arrays;

class Solution {
    // 1 <= g.length <= 3 * 10^4
    // 0 <= s.length <= 3 * 10^4
    // 1 <= g[i], s[j] <= 2^31 - 1
    public int findContentChildren(int[] g, int[] s) {
        // 依旧优先处理边界条件：没有有饼干的时候直接返回0
        if (s.length == 0) {
            return 0;
        }

        // 双轴快排（O(n logn)，3e4元素仅需≈4.5e5次操作
        Arrays.sort(g);
        Arrays.sort(s);

        // 继续剪枝(如果最大的饼干不能满足最小的胃口或最小的饼干都可以满足所有的胃口)
        // 最大饼干小于最小胃口
        if (s[s.length - 1] < g[0]) {
            return 0;
        }
        // 最小饼干大于等于最大胃口
        if (s[0] >= g[g.length - 1]) {
            return Math.min(s.length, g.length); // 直接返回所有(孩子/饼干)数量的最小值
        }

        // 双指针遍历两个数组，统计满足条件的孩子数量
        int gIndex = 0; // 胃口索引
        int sIndex = 0; // 饼干索引
        int gLen = g.length, sLen = s.length;
        // 因为已经排过序，所以第一个无法满足的孩子的下标就是最大数量
        while (gIndex < gLen && sIndex < sLen) {
            // 持续找到下一个满足条件的孩子
            if (s[sIndex++] >= g[gIndex]) {
                gIndex++;
            }
        }
        // 最后返回满足条件的孩子数量
        return gIndex;

    }
}
