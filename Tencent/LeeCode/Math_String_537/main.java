package Tencent.LeeCode.Math_String_537;

class Solution {
    public String complexNumberMultiply(String num1, String num2) {
        // 拆分字符串
        String[] nums1 = num1.split("\\+");
        String[] nums2 = num2.split("\\+");

        // 先列出a、c和b、d
        int a = Integer.parseInt(nums1[0]);
        int c = Integer.parseInt(nums2[0]);
        int b = Integer.parseInt(nums1[1].replace("i", ""));
        int d = Integer.parseInt(nums2[1].replace("i", ""));

        // 计算
        int real = a * c - b * d;
        int imaginary = a * d + b * c;

        // 拼接字符串
        return real + "+" + imaginary + "i";
    }
}