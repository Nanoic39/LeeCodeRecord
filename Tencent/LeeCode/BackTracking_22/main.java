package Tencent.LeeCode.BackTracking_22;

import java.util.List;
import java.util.ArrayList;

// 优化考虑使用StringBuilder，但是要注意需要手动撤销选择（sb.deleteCharAt(sb.length()-1)
class Solution {
    private List<String> res;
    
    public List<String> generateParenthesis(int n) {
        res = new ArrayList<>();
        // 回溯(空字符串、0个左括号、0个右括号、n对括号)
        backtrack("", 0, 0, n);
        return res;
    }

    private void backtrack(String s, int left, int right, int n) {
        // 如果所有括号都用完了
        if(s.length() == 2 * n) {
            res.add(s);
            return; // 返回上一层
        };

        // 如果还有左括号能用
        if(left < n) { 
            backtrack(s + "(", left + 1, right, n);
        }
        // 如果还有右括号能用
        if(right < left) {
            backtrack(s + ")", left, right + 1, n);
        }
    }
}
