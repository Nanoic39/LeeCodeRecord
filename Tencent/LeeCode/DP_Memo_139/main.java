package Tencent.LeeCode.DP_Memo_139;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Solution {
    // 提升变量
    private String str; // 输入字符串
    private Set<String> wordDictList; // 单词字典列表
    private int[] memo; // 记忆化数组，-1表示未计算，0表示不能拆分，1表示可以拆分
    private int strLength; // 字符串长度
    
    public boolean wordBreak(String s, List<String> wordDict) {
        this.str = s;
        // 转换为HashSet，提高查找效率
        this.wordDictList = new HashSet<>(wordDict);
        this.strLength = s.length();
        this.memo = new int[strLength + 1];
        // 初始化记忆数组为-1
        Arrays.fill(memo, -1);

        // 递归判断从 start=0 开始的整个字符串能不能拆分
        return dfs(0);

    }

    // 递归判断 str[start~strLength-1] 的子字符串能不能拆分
    private boolean dfs(int start) {
        // 如果已经计算过，直接返回结果
        if(memo[start] != -1) {
            return memo[start] == 1;
        }
        // 如果字符串为空（到达字符串末尾），返回true
        if(start == strLength) {
            return true;
        }
        // 遍历所有可能的结束位置
        for(int end = start + 1; end <= strLength; end++) {
            // 子串 str[start~end-1]
            String subStr = str.substring(start, end);
            // 如果子串在字典中，且后续子串也可以拆分
            if(wordDictList.contains(subStr) && dfs(end)) {
                // 标记为可以拆分
                memo[start] = 1;
                return true;
            }
        }
        // 没有找到合适的拆分，标记为不能拆分
        memo[start] = 0;
        return false;
    }
}
