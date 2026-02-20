package Tencent.LeeCode.Stack_Recursion_224;

import java.util.Stack;

class Solution {
    public int calculate(String s) {
        // 去除所有空格
        s = s.replaceAll("\\s+", "");
        char[] chars = s.toCharArray();
        int n = chars.length;
        
        // 符号栈存储每一层括号的符号因子（1=正，-1=负），初始栈底为1（默认正）
        Stack<Integer> signStack = new Stack<>();
        signStack.push(1);
        
        int res = 0;          // 最终结果
        int currentSign = 1;  // 当前数字的符号（由栈顶和加减号共同决定）
        int currentNum = 0;   // 累积多位数（处理12、345等）
        
        for (int i = 0; i < n; i++) {
            char c = chars[i];
            
            // 如果是数字 → 累积多位数
            if (Character.isDigit(c)) {
                currentNum = currentNum * 10 + (c - '0');
            } 
            // 如果是加号或减号 → 先计算前一个数字，再更新当前符号
            else if (c == '+' || c == '-') {
                // 先把之前累积的数字按当前符号加到结果中
                res += currentSign * currentNum;
                currentNum = 0; // 重置累积数字
                // 根据栈顶因子和当前符号，更新下一个数字的符号
                currentSign = signStack.peek() * (c == '+' ? 1 : -1);
            } 
            // 如果是左括号 → 压入当前符号因子（记录当前层的符号）
            else if (c == '(') {
                signStack.push(currentSign);
            } 
            // 如果是右括号 → 弹出当前层因子，计算括号内最后一个数字
            else if (c == ')') {
                res += currentSign * currentNum;
                currentNum = 0; // 重置累积数字
                signStack.pop(); // 弹出当前层符号因子
                currentSign = signStack.peek(); // 恢复外层符号因子
            }
        }
        
        // 最后额外处理表达式末尾的数字（如"2-1+2"的最后一个2）
        res += currentSign * currentNum;
        
        return res;
    }
}
