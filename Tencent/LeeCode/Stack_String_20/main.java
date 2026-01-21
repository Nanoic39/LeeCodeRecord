package Tencent.LeeCode.Stack_String_20;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// 有效的括号 --> 极致运行时间优化考虑方向：复用字符串的 char 数组作为栈
class Solution {
    public boolean isValid(String s) {
        // 长度不为偶数的字符串直接返回false
        if(s.length() % 2 != 0) {
            return false;
        }

        // 定义映射规则
        // 利用数据结构特性简化逻辑（如映射表 key 判断右括号），减少硬编码
        Map<String, String> map = new HashMap<>();
        map.put(")", "(");
        map.put("]", "[");
        map.put("}", "{");

        // 优化时考虑使用Deque<Character> 实现栈功能
        // 定义栈
        Stack<String> stack = new Stack();
        // 遍历字符串
        for (int i = 0; i<s.length(); i++) {
            // 如果属于左括号
            // 处理单个字符优先使用 char/Character，比 String 更高效、更贴合场景
            // 遍历字符串优先使用 charAt(i)，避免 split("") 的额外性能开销
            if(s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') {
                // 入栈
                stack.push(s.charAt(i)+"");
            }
            // 如果属于右括号
            else {
                // 匹配栈顶元素
                // 字符串内容比较必须用 equals()，基本类型 / 包装类（如 Character）比较可用 ==
                if(stack.isEmpty() || !stack.pop().equals(map.get(s.charAt(i)+""))) {
                    return false;
                } else {
                    continue;
                }
            }
        }

        // 检查栈是否为空)
        return stack.isEmpty();
    }
}