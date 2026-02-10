package Tencent.LeeCode.Queue_Sliding_Heap_239;

import java.util.Deque;
import java.util.LinkedList;

class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 边界条件
        if(nums.length == 1) {
            return nums;
        }
        // 单调队列
        Deque<Integer> dq = new LinkedList<>();

        int len = nums.length;
        int[] res = new int[len - k + 1];
        int resIndex = 0; // 最大值的索引

        for (int i = 0; i < len; i++) {
            // 移除超出窗口左侧（i - k）的索引
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) {
                dq.pollFirst();
            }
            
            // 移除队尾值小于当前nums[i]的索引
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) {
                dq.pollLast();
            }

            // 将当前元素索引加入队尾
            dq.offerLast(i);

            // 存在窗口时队列头就是当前窗口最大值的索引
            if (i >= k - 1) {
                res[resIndex++] = nums[dq.peekFirst()];
                
            }
        }

        return res;
    }
}