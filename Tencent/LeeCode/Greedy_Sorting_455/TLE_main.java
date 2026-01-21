package Tencent.LeeCode.Greedy_Sorting_455;

import java.util.HashMap;
import java.util.Map;

class Solution {
    // 1 <= g.length <= 3 * 10^4
    // 0 <= s.length <= 3 * 10^4
    // 1 <= g[i], s[j] <= 2^31 - 1
    public int findContentChildren(int[] g, int[] s) {
        // 依旧优先处理边界条件：没有有饼干的时候直接返回0
        if (s.length == 0) {
            return 0;
        }

        // 开始排序
        int gSort[] = countSort(g);
        int sSort[] = countSort(s);

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
     * 计数排序
     * 
     * @param arr 输入数组
     * @param max 数组中的最大值
     * @return 排序后的数组
     */
    private int[] countSort(int[] arr) {
        // 构建计数数组（下标对应元素值，元素值对应出现次数）
        Map<Integer, Integer> countMap = new HashMap<>();
        int min = arr[0], max = arr[0];
        // 遍历一次原始数组进行计数(并获取最大最小值)
        for (int num : arr) {
            // 新增/更新计数（若存在）
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
            if(num < min) {
                min = num;
            } 
            if(num > max) {
                max = num;
            }
        }
        // 遍历计数数组，根据出现次数及下标构建排序后的数组
        int[] sortArr = new int[arr.length];
        int index = 0;
        for(int i = min; i <= max; i++) {
            int times = countMap.getOrDefault(i, 0);
            while(times-- > 0) {
                sortArr[index++] = i;
            }
        }

        return sortArr;

    }
}
