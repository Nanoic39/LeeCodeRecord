package Tencent.LeeCode.Array_DP_122;

class Solution{
    // 1 <= prices.length <= 3 * 10^4
    // 0 <= prices[i] <= 10^4
    // 此处使用更加通用的动态规划，后续优化考虑贪心
    public int maxProfit(int[] prices) {
        // dp[i][j] 表示第i天时的j状态下的总利润，最终结果为dp[prices.length-1][0]
        // 最后一天必须卖出才能获取最大利润，状态一定为0
        // j状态：0表示不持有股票，1表示持有股票
        // dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i]) //不持有或持有后卖出
        // dp[i][1] = max(dp[i-1][1], dp[i-1][0] - prices[i]) //不持有后买入或原本就持有

        // 先处理边界
        if(prices.length == 1) {
            return 0; // 只有一天你买个damn，第二天又不能卖，买了纯亏啊
        }

        // 初始化状态
        int perCash, perHold, cash, hold;
        cash = 0; // 第0天不持有
        hold = -prices[0]; // 第0天持有

        // 更新dp
        for(int i = 1; i < prices.length; i++) {
            // 记录今天操作开始前(昨天)的状态
            perCash = cash;
            perHold = hold;

            // 更新今天状态
            cash = Math.max(perCash, perHold + prices[i]);
            hold = Math.max(perHold, perCash - prices[i]);
        }

        // 最后一天必须卖出才能获取最大利润，状态一定为0
        return cash;
    }
}
