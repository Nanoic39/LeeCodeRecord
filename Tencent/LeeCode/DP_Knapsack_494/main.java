package Tencent.LeeCode.DP_Knapsack_494;

class Solution {
    // 公式推导：
    // P 表示数组中所有元素为正的总和，N 表示数组中所有元素为负的总和
    // sum 表示数组所有元素的总和，target表示目标值
    // sum = P + N，target = P - N
    // P = (sum + target) / 2
    // 问题可以转化为 统计从数组中选若干元素，使其和等于 P 的方法数
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for(int num : nums) {
            sum += num;
        }
        int P = (sum + target) / 2;
        // 边界条件：(sum + target) 必须是偶数；target 必须小于等于 sum；P 必须大于等于 0
        if((sum + target) % 2 != 0 || Math.abs(target) > sum || P < 0) {
            return 0;
        }

        // dp[i] 表示为凑出和为 j 的子集的方法数
        int[] dp = new int[P + 1];
        dp[0] = 1;

        // 遍历元素+倒序更新dp
        for(int num : nums) {
            for(int i = P; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }

        return dp[P];
    }

}
