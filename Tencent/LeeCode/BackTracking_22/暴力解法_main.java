package Tencent.LeeCode.BackTracking_22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Solution_Rage {
    public List<String> generateParenthesis(int n) {
        if (n == 1) {
            return Arrays.asList("()");
        }

        Set<String> list = new HashSet<>();

        for (String s : generateParenthesis(n - 1)) {
            for (int i = 0; i < 2 * n - 2; i++) {
                list.add(s.substring(0, i) + "()" + s.substring(i, s.length()));
            }
        }
        
        return new ArrayList<>(list);
    }
}
