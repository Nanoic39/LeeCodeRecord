package Tencent.LeeCode.Stack_Recursion_224;

import java.util.Stack;

class Solution {

    // 没有考虑到多位数字的情况，写得太混乱了，重新理一下逻辑
    public int calculate(String s) {
        // 去除无用的空格
        s = s.replaceAll("[\\s]", ""); 

        // 转为char
        char[] cList = s.toCharArray();

        boolean isFlip = false;
        int sum = 0;
        char b = '0';
        int count = 0; // 无特殊符号就表示这是一整个数字，那就原样输出

        Stack<Integer> isF = new Stack<>();

        char sig = ' ';

        // 录入
        for (int i = 0; i < cList.length; i++) {
            
            if (cList[i] == '-') {
                // 特殊情况（遇到"-("就进行翻转，并在栈中记录，并且不占用符号位）
                if (cList[i + 1] == '(') {
                    isF.add(1);
                    isFlip = !isFlip;
                }

                // 录入符号
                if (isFlip) {
                    sig = '+';
                } else {
                    sig = '-';
                }

                count++;
                continue; // 只要不是数字就跳过计算
            } else if (cList[i] == '+') {
                if (isFlip) {
                    sig = '-';
                } else {
                    sig = '+';
                }

                count++;
                continue; // 只要不是数字就跳过计算
            } else if (cList[i] == '(') {
                isF.add(0);
                continue; // 只要不是数字就跳过计算
            } else if (cList[i] == ')') {
                // 如果遇到尾括号就进行判断，看是否需要翻转回去
                if (isF.peek() == 1) {
                    isF.pop();
                    isFlip = !isFlip;
                }
                continue; // 只要不是数字就跳过计算
            } else {
                b = cList[i];
            }
            
            // 计算 :-)
            if (sig == '-') {
                sum = sum - (b - '0');
            } else {
                sum = sum + (b - '0');
            }
        }

        if (count != 0) {
            return sum;
        } else {

            return Integer.parseInt(s.replaceAll("[()]", ""));
        }
    }

}
