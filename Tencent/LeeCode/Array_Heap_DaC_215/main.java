package Tencent.LeeCode.Array_Heap_DaC_215;

import java.util.PriorityQueue;

class Solution {
    // 时间复杂度要求O(n)
    public int findKthLargest(int[] nums, int k) {
        // 使用大根堆，维护前k大的元素
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        for (int num : nums) {
            pq.offer(num);
        }
        // 弹出k-1个元素
        for (int i = 0; i < k - 1; i++) {
            pq.poll();
        }
        // 堆顶元素即为第k大的元素
        return pq.peek();
    }
}
