package Tencent.LeeCode.DP_322;

class Solution {
    public int coinChange(int[] coins, int amount) {
        // dp[i]表示组成金额i的最少硬币数量
        int[] dp = new int[amount + 1];
        // 初始化dp[0]
        dp[0] = 0;
        // 初始化其他dp元素值为amount + 1
        for (int i = 1; i <= amount; i++) {
            dp[i] = amount + 1;
        }

        // 转移方程：dp[i] = min(dp[i], dp[i - coin] + 1)
        for(int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                // 如果当前硬币可以使用，更新dp[i]
                if(coin <= i) {
                    // 取当前dp[i]和dp[i - coin] + 1的较小值
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        // 判断结果
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }
}
