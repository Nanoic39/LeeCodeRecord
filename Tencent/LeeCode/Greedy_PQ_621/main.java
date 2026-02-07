package Tencent.LeeCode.Greedy_PQ_621;

import java.util.Map;
import java.util.HashMap;

class Solution {
    public int leastInterval(char[] tasks, int n) {
        // 核心公式：需要冷却的最小时间为：(maxCount - 1) * (n + 1) + maxNum
        // 统计出现次数最多的任务
        Map<Character, Integer> map = new HashMap<>();
        for(char c : tasks) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }

        // 出现次数最多的任务
        int maxCount = 0;
        for(int count : map.values()) {
            maxCount = Math.max(maxCount, count);
        }
        
        // 统计到达最多次数的任务有多少个
        int maxNum = 0;
        for(int count : map.values()) {
            if(count == maxCount) {
                maxNum++;
            }
        }

        // 计算最小冷却时间和结果
        int minTime = (maxCount - 1) * (n + 1) + maxNum;
        return Math.max(minTime, tasks.length);
    }
    
}
