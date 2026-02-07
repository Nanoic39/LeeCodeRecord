package Tencent.LeeCode.Stack_739;

import java.util.Deque;
import java.util.LinkedList;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        Deque<Integer> stack = new LinkedList<>();

        // 单调栈，栈中元素从底到顶递减，栈中记录温度下标
        int[] answer = new int[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int index = stack.pop(); // 弹出栈顶元素，计算距离
                answer[index] = i - index; // 记录距离
            }
            // 当前温度不大于栈顶温度，入栈，等待后续匹配
            stack.push(i);
        }

        return answer;
    }
}