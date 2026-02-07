package Tencent.LeeCode.Array_TowPoint_15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();

        // 排序
        Arrays.sort(nums);

        // 遍历固定第一个数字
        for (int i = 0; i < nums.length - 2; i++) {
            // 剪枝，如果排序后固定的数字大于0，后续不可能三数和为0
            if (nums[i] > 0) {
                break;
            }
            // 去重，如果当前数字与前一个数字相同，跳过，避免产生重复三元组
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // 双指针
            int left = i + 1;
            int right = nums.length - 1;

            while(left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if(sum == 0) {
                    res.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    // 去重
                    while(left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while(left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }

                    left++;
                    right--;
                } else if (sum < 0) { // 和小于0，左指针向右移动
                    left++;
                } else { // 和大于0，右指针向左移动
                    right--;
                }
            }
        }

        return res;
        
    }
}
