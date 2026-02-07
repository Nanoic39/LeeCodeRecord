package Tencent.LeeCode.Array_TwoPoint_11;

class Solution {
    // O(n)
    public int maxArea(int[] height) {
        int ans = 0;
        int left = 0, right = height.length - 1;
        while (left < right) {
            // 计算面积
            int area = (right - left) * Math.min(height[left], height[right]);
            ans = Math.max(ans, area);
            // 移动指针(移动短边)
            if(height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return ans;
    }
}