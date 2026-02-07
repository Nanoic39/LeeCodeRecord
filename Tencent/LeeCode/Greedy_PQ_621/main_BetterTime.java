package Tencent.LeeCode.Greedy_PQ_621;

class Solution {
    // 使用HashMap的性能没有直接使用char的好
    // 时间复杂度O(N)
    // 空间复杂度O(1)
    public int leastInterval(char[] tasks, int n) {
        // 核心公式：需要冷却的最小时间为：(maxCount - 1) * (n + 1) + maxNum
        // 统计出现次数最多的任务
        char[] map = new char[26];
        for(char c : tasks) {
            map[c - 'A']++;
        }

        // 出现次数最多的任务
        int maxCount = 0;
        for(int count : map) {
            maxCount = Math.max(maxCount, count);
        }
        
        // 统计到达最多次数的任务有多少个
        int maxNum = 0;
        for(int count : map) {
            if(count == maxCount) {
                maxNum++;
            }
        }

        // 计算最小冷却时间和结果
        int minTime = (maxCount - 1) * (n + 1) + maxNum;
        return Math.max(minTime, tasks.length);
    }
    
}
