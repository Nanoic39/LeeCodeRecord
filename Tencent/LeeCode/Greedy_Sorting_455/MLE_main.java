package Tencent.LeeCode.Greedy_Sorting_455;

class Solution {
    // 1 <= g.length <= 3 * 10^4
    // 0 <= s.length <= 3 * 10^4
    // 1 <= g[i], s[j] <= 2^31 - 1
    public int findContentChildren(int[] g, int[] s) {
        // 依旧优先处理边界条件：没有有饼干的时候直接返回0
        if (s.length == 0) {
            return 0;
        }

        // g[i] / s[j] 的数值范围有限，采用计数排序，排序速度从O(nlogn)降到O(n+numMax)
        // 一般小于等于10^5即可
        int gMax = getMax(g);
        int sMax = getMax(s);

        // 开始排序
        int gSort[] = countSort(g, gMax);
        int sSort[] = countSort(s, sMax);

        // 继续剪枝(如果最大的饼干不能满足最小的胃口或最小的饼干都可以满足所有的胃口)
        // 最大饼干小于最小胃口
        if (gSort[gSort.length - 1] < sSort[0]) {
            return 0;
        }
        // 最小饼干大于最大胃口
        if (gSort[0] > sSort[sSort.length - 1]) {
            return Math.min(sSort.length, gSort.length); // 直接返回所有(孩子/饼干)数量的最小值
        }

        // 双指针遍历两个数组，统计满足条件的孩子数量
        int gIndex = 0; // 胃口索引
        int sIndex = 0; // 饼干索引
        int gLen = g.length, sLen = s.length;
        // 因为已经排过序，所以第一个无法满足的孩子的下标就是最大数量
        while (gIndex < gLen && sIndex < sLen) {
            // 持续找到下一个满足条件的孩子
            if (sSort[sIndex++] >= gSort[gIndex]) {
                gIndex++;
            }
        }
        // 最后返回满足条件的孩子数量
        return gIndex;

    }

    /**
     * 获取数组中的最大值
     * 
     * @param arr 输入数组
     * @return 数组中的最大值
     */
    private int getMax(int[] arr) {
        int max = arr[0];
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * 计数排序
     * 
     * @param arr 输入数组
     * @param max 数组中的最大值
     * @return 排序后的数组
     */
    private int[] countSort(int[] arr, int max) {
        // 构建计数数组（下标对应元素值，元素值对应出现次数）
        int[] count = new int[max + 1];
        // 遍历一次原始数组进行计数
        for (int num : arr) {
            count[num]++;
        }
        // 遍历计数数组，根据出现次数及下标构建排序后的数组
        int sortArr[] = new int[arr.length];
        int index = 0;
        for(int i = 0; i < count.length; i++) {
            while(count[i]-- > 0) {
                sortArr[index++] = i;
            }
        }

        return sortArr;

    }
}
