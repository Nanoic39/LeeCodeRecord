package Tencent.LeeCode.Array_Binary_704;

class Solution {
    // nums升序
    // 假设元素不重复
    // 1 <= n <= 10000
    // -9999 <= nums[i] <= 9999
    public int search(int[] nums, int target) {
        // 定义指针为当前搜索范围的中间位置
        int left = 0, right = nums.length - 1; // 定义左指针和右指针
        // 先测试边界情况（如果是最边界的值就不要二分了）
        if(nums[left] == target || nums[right] == target) {
            return nums[left] == target ? left : right;
        }

        // 开始循环
        while (left <= right) {
            // 开始二分
            int mid = left + (right - left) / 2;
            // 尝试匹配目标值
            if (nums[mid] == target) {
                return mid;
            }
            // 如果mid值大于目标值，说明目标值在左半部分，收缩右指针到mid左侧
            else if (nums[mid] > target) {
                right = mid - 1;
            }
            // 如果mid值小于目标值，说明目标值在右半部分，收缩左指针到mid右侧
            else if (nums[mid] < target) {
                left = mid + 1;
            }
        }

        // 循环结束没找到
        return -1;
    }
}
