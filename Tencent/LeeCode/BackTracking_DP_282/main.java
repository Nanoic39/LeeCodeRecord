package Tencent.LeeCode.BackTracking_DP_282;

import java.util.ArrayList;
import java.util.List;

class Solution {

    // 结果集
    private List<String> result = new ArrayList<>();
    private String num;
    private int target;


    public List<String> addOperators(String num, int target) {
        this.num = num;
        this.target = target;

        // 回溯
        backtrack(0, new StringBuilder(), 0, 0);
    
        return result;
    }

    private void backtrack(int index, StringBuilder path, long sum, long last) {
        // 遍历完所有数字后终止
        if (index == num.length()) {
            if (sum == target) {
                // 如果和等于目标值则保存路径作为结果之一
                result.add(path.toString());
            }
            return;
        }

        // 遍历从index开始的所有可能的数字
        for (int i = index; i < num.length(); i++) {
            // 如果当前数字是0而且不是单个零（i>index）
            if (i > index && num.charAt(index) == '0') {
                break;
            }

            // 截取数字串并转换长整型防止溢出
            // 这里是 i + 1，因为要尝试拼接出多位数
            long currentNum = Long.parseLong(num.substring(index, i + 1));
            int len = path.length(); // 记录路径长度（用于回溯）

            // 如果是第一个数字
            // 没有运算符号
            if (index == 0) {
                path.append(currentNum);
                backtrack(i + 1, path, currentNum, currentNum);
                path.setLength(len); // 回溯并删除刚才添加的数字
            } else { // 否则需要考虑插入运算符号
                // 加入加号
                path.append('+').append(currentNum);
                backtrack(i + 1, path, sum + currentNum, currentNum);
                path.setLength(len);

                // 加入减号
                path.append('-').append(currentNum);
                backtrack(i + 1, path, sum - currentNum, -currentNum);
                path.setLength(len);

                // 加入乘号
                path.append('*').append(currentNum);
                // 乘号优先级高于加减所以要修正上一步的sum
                backtrack(i + 1, path, sum - last + last * currentNum, last * currentNum);
                path.setLength(len);                
            }
        }
    }
}
