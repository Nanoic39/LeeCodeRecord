package Tencent.LeeCode.Array_Heap_DaC_215;

import java.util.Arrays;

class Solution {
    // 时间复杂度O(nlogn)
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }
}
