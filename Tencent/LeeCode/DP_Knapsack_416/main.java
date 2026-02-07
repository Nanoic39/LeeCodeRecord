package Tencent.LeeCode.DP_Knapsack_416;

class Solution {
    public boolean canPartition(int[] nums) {
        // 问题可以转化为0-1背包问题
        // 剪枝条件：先求总和，如果总和为奇数，则无法平分成两个数组
        int sum = 0;
        for(int num : nums){
            sum += num;
        }
        if(sum % 2 != 0) {
            return false;
        }

        // 背包问题：能否从nums中选出一些数，使得这些数的和为sum/2
        // dp[i]表示是否能用数组中的元素凑出和为i的子集
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        // 初始化
        dp[0] = true;
        // 状态转移：
        for(int num : nums) {
            // 倒序，避免重复使用同一个元素
            for(int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }

        // 最终结果：dp[target]表示是否能用数组中的元素凑出和为target的子集
        return dp[target];
    }
    
}
