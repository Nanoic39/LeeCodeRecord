package Tencent.LeeCode.DP_62;

class Solution {
    public int uniquePaths(int m, int n) {
        // 边界条件：1x1或单行单列
        if (m == 1 || n == 1) {
            return 1;
        } 

        // dp[i][j]表示到达第i行第j列的路径数量
        // 转移方程：dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        int[][] dp = new int[m][n];
        // 初始化第一行
        for(int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for(int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for(int i = 1; i < m; i++) {
            for(int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }
}
