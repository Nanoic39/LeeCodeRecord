package Tencent.LeeCode.Stack_Recursion_224;

import java.util.Stack;

class Solution {

    int sum = 0; // 和

    public int calculate(String s) {
        // 去除空格
        s = s.replaceAll("\\s+", "");
        // 栈（用于记录符号翻转时间）
        Stack<Integer> isFilp = new Stack<>();
        isFilp.add(0); // 默认不翻转
        
        char[] cList = s.toCharArray(); // 转换为数组
        int len = cList.length; // 长度

        boolean sig = true; // 0:-,1:+
        int currentNum = 0; // 专门整个这个用来统计多位数字

        boolean isInFilp = false;

        for (int i = 0; i < len; i++) {
            if (Character.isDigit(cList[i])) {
                // 如果存在数字就会乘十后移动到高位，然后加上新的数字
                // 否则 0 * 10 = 0
                currentNum = (currentNum * 10) + (cList[i] - '0');

                // 判断数字前一个是不是左括号，如果是的话就根据isInFilp设置sig
                if(currentNum == 0) {
                    if (i > 0 && cList[i - 1] == '(') {
                        if (isInFilp) {
                            sig = false;
                        } else {
                            sig = true;
                        }
                    }
                }

                if (i != len - 1) {
                    continue;
                }
            }

            myCalc(currentNum, sig);

            // 负号
            if (cList[i] == '-') {
                // 特殊情况（遇到"-("就sig进行翻转，并在栈中记录，并且不占用符号位）
                if (cList[i + 1] == '(') {
                    isFilp.add(1);
                    isInFilp = !isInFilp;
                }

                // 不需要翻转就要记录负号（默认正）
                if(isInFilp) {
                    sig = true;
                } else {
                    sig = false;
                }
            }

            // 正号
            if (cList[i] == '+') {
                // 翻转时记录为负号
                if(isInFilp) {
                    sig = false;
                } else {
                    sig = true;
                }
            }

            // 单独左括号（如果前面不是负号就添加不翻转标记，然后不管是什么，都直接拆封）
            if (cList[i] == '(') {
                if (i > 0 && cList[i - 1] == '-') {
                    // 什么都不做，也不能continue;
                    // 但是得有这种情况作为排除
                } else {
                    isFilp.add(0);
                }
            }

            // 右括号处理标记栈弹出
            if (cList[i] == ')') {
                if (isFilp.peek() == 1) {
                    isInFilp = !isInFilp;
                }

                // 弹出栈顶标记
                isFilp.pop();
            }
          
            // 计算完成后重置数字
            currentNum = 0;
        }

        // 返回值
        return sum;
    }

    private void myCalc(int currentNum, boolean sig) {
        // 计算
        if (sig) {
            sum += currentNum;
        } else {
            sum -= currentNum;
        }
            
    }

}
