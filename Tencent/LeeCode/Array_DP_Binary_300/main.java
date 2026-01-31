package Tencent.LeeCode.Array_DP_Binary_300;

class Solution {
    public int lengthOfLIS(int[] nums) {
        // 贪心 + 二分查找，时间复杂度 O(n log n)
        // 目标：让递增子序列增长得尽可能慢，这样后面才有机会接上更多的数字。
        // tails[i] 表示长度为 i+1 的所有递增子序列中，结尾最小的那个数
        int[] tails = new int[nums.length];
        int res = 0; // 当前最长递增子序列的长度
        
        for(int num : nums) {
            // 二分查找：在 tails[0...res-1] 中找到第一个 >= num 的位置
            int i = 0, j = res;
            while(i < j) {
                int m = (i + j) / 2; // 取中间位置
                if(tails[m] < num) i = m + 1; // 如果 tails[m] 小于 num，说明 num 应该在 m 的右边
                else j = m; // 如果 tails[m] 大于等于 num，说明 num 应该在 m 的左边或等于 m
            }
            
            // 如果找不到（i == res），说明 num 比当前所有 tails 都大，可以直接扩展长度
            // 如果找到了，说明 num 可以替换 tails[i]，让长度为 i+1 的子序列结尾更小（贪心策略）
            tails[i] = num; // 更新 tails[i] 为 num
            if(res == j) res++; // 如果 i 等于当前最长长度，说明新增了一个更长的子序列，更新 res
        }
        
        return res;
    }
}
