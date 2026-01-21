package Tencent.LeeCode.Array_Hash_01;

import java.util.HashMap;
import java.util.Map;

// 两数之和
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 定义哈希表，空间换时间
        // Key: nums的值，Value: nums对应的下标
        Map<Integer, Integer> map = new HashMap<>();

        // 遍历数组
        for (int i = 0; i < nums.length; i++) {
            // 获取当前元素与目标的差值
            int diff = target - nums[i];

            // 检查是否在哈希表中存在
            if (map.containsKey(diff)) {
                // 返回结果[a, b]
                return new int[] { map.get(diff), i };
            }

            // 否则将当前元素插入HashMap表
            map.put(nums[i], i);
        }
        // 保底防止报错
        return new int[] {};
    }
}