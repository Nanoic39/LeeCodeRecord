package Tencent.LeeCode.Array_Sort_56;

import java.util.ArrayList;
import java.util.Arrays;

class Solution {
    public int[][] merge(int[][] intervals) {
        // 如果只有一个区间，直接返回
        if (intervals.length == 1) {
            return intervals;
        }

        ArrayList<int[]> res = new ArrayList<>();

        // 排序
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // 合并
        int index = 1, left = intervals[0][0], right = intervals[0][1];
        while (index < intervals.length) {
            if (index == intervals.length - 1) {
                if (intervals[index][0] > right) {
                    res.add(new int[] { intervals[index][0], intervals[index][1] });
                } else {
                    res.add(new int[] { left, Math.max(right, intervals[index][1]) });
                }
            }
            // 如果当前区间的左边界大于right值
            if (intervals[index][0] > right) {
                // 写入前一个区间
                res.add(new int[] { left, right });
                // 更新区间
                left = intervals[index][0];
                right = intervals[index][1];
            } else if (intervals[index][1] > right) { // 如果左区间在范围内且有边界大于right值，则只更新right值
                right = intervals[index][1];
            }
            index++;
        }
        return res.toArray(new int[res.size()][]);
    }
}