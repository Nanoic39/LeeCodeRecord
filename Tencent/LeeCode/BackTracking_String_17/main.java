package Tencent.LeeCode.BackTracking_String_17;

import java.util.List;
import java.util.ArrayList;

class Solution {
    private final String Map[] = {
        "", "", // 0, 1
        "abc", // 2
        "def", // 3
        "ghi", // 4
        "jkl", // 5
        "mno", // 6
        "pqrs", // 7
        "tuv", // 8
        "wxyz" // 9
    };

    private List<String> res;
    
    public List<String> letterCombinations(String digits) {
        res = new ArrayList<>();

        // 解析每个数字
        backtrack(new StringBuilder(), 0, digits);

        return res;
    }

    private void backtrack(StringBuilder s, int index, String digits) {
        // 递归出口：所有数字处理完毕
        if(index == digits.length()) {
            res.add(s.toString());
            return;
        }
        // 获取对应字母表
        char c = digits.charAt(index);
        String letter = Map[c - '0'];

        for (char cha : letter.toCharArray()) {
            // 选择
            s.append(cha);
            // 回溯
            backtrack(s, index + 1, digits);
            // 撤销选择（删除最后一个字母）
            s.deleteCharAt(s.length() - 1);
        }
    }
}
