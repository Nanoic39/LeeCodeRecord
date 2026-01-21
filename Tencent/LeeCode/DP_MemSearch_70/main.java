package Tencent.LeeCode.DP_MemSearch_70;

class Solution {
    public int climbStairs(int n) {
        // 爬楼梯问题逆向推导可以得出达到第n阶台阶需要上一步为从(n-1)爬1阶或从(n-2)爬两阶
        // 方法数递推公式为：f(n) = f(n-1) + f(n-2)
        // 不难看出其本质是斐波那契数列的变形形式

        if (n == 1) { // 特殊边界情况：只有一级阶梯，只有一种方式
            return 1;
        }
        if (n == 2) { // 特殊边界情况：只有两级阶梯，只有两种方式
            return 2;
        }

        // 因为每一步仅需要由前两步即可推到，所以仅需要存储前两步的方法数
        // 这样空间复杂度仅为O(1)
        int preid2 = 1; // dp[i-2] （previous i diff 2，不是id2）
        int preid1 = 2; // dp[i-1] （同上）
        int cur = 0; // dp[i]

        // 迭代计算dp[i]
        for(int i = 3; i <= n; i++) {
            // 计算当前状态
            cur = preid1 + preid2;
            // 更新前两步状态为前一步状态
            preid2 = preid1;
            // 更新前一步状态为当前状态用来下次计算
            preid1 = cur;
        }

        // 计算完毕后返回dp[i]的值即可
        return cur;
    }
}
