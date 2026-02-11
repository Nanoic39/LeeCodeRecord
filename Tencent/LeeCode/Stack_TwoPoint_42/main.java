package Tencent.LeeCode.Stack_TwoPoint_42;

class Solution {
    public int trap(int[] height) {
        int len = height.length;
        int left = 0;
        int right = len - 1;
        int maxPer = 0, maxSuf = 0;
        int ans = 0;
        
        // 等于时也要进行计算
        while (left <= right) {
            maxPer = Math.max(maxPer, height[left]);
            maxSuf = Math.max(maxSuf, height[right]);
            
            // 比较哪边更低，按最低的计算
            if (maxPer < maxSuf) {
                // 疣猪啊，把maxPer写成maxSuf找了15分钟问题在哪里QAQ
                ans += maxPer - height[left];
                left+=1;
            } else {
                ans += maxSuf - height[right];
                right-=1;
            }
        }

        return ans;

    }
}
