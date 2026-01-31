package Tencent.LeeCode.Array_DP_Binary_300;

import java.util.Arrays;

class Solution {
    public int lengthOfLIS(int[] nums) {
        // 动态规划，dp[i]表示以nums[i]结尾的最长递增子序列长度
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1); // 初始化dp数组为1
        // 遍历数组，计算每个位置的最长递增子序列长度
        for(int i = 0; i < nums.length; i++) {
            // 遍历之前的所有数字，如果nums[j]小于nums[i]，说明nums[i]可以作为nums[j]的递增子序列，更新dp[i]为当前dp[i]和dp[j]+1中的较大值
            for(int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        // 遍历dp数组，找到最大值
        int max = 0;
        for(int d : dp) {
            max = Math.max(max, d);
        }
        
        return max;
    }
}
