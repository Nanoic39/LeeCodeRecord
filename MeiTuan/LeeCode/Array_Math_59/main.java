package MeiTuan.LeeCode.Array_Math_59;

class Solution {
    public int[][] generateMatrix(int n) {
        // 初始化n×n的矩阵
        int[][] matrix = new int[n][n];
        
        // 遍历矩阵的每个位置(i,j)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // 计算当前位置属于第k圈（层）
                int k = Math.min(Math.min(i, j), Math.min(n-1-i, n-1-j));
                // 计算第k圈的边长
                int m = n - 2 * k;
                // 计算第k圈的起始数字
                int start = 1 + 4 * k * (n - k);
                
                // 根据所在边计算具体值
                if (i == k) {
                    // 上边：行号等于k，从左到右
                    matrix[i][j] = start + (j - k);
                } else if (j == n - 1 - k) {
                    // 右边：列号等于n-1-k，从上到下
                    matrix[i][j] = start + (m - 1) + (i - k);
                } else if (i == n - 1 - k) {
                    // 下边：行号等于n-1-k，从右到左
                    matrix[i][j] = start + 2 * (m - 1) + (n - 1 - k - j);
                } else {
                    // 左边：列号等于k，从下到上
                    matrix[i][j] = start + 3 * (m - 1) + (n - 1 - k - i);
                }
            }
        }
        return matrix;
    }
}