package Tencent.LeeCode.Array_Hash_BackTracking_36;

class Solution {
    public boolean isValidSudoku(char[][] board) {
        // 声明哈希表
        int[][] row = new int[9][9];
        int[][] col = new int[9][9];
        int[][][] box = new int[3][3][9]; // 3*3个小盒子，每个盒子内储存9个数字
        // 遍历数独表
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                // 如果当前位置是空格就跳过
                if(board[i][j] == '.') {
                    continue;
                }
                // 计算当前位置的数字
                int num = board[i][j] - '1'; // 数组是0~8，原始表格是1~9，所以这里转换为数字应该减'1'
                // 检查当前数字是否已经出现过
                if(row[i][num] == 1 || col[j][num] == 1 || box[i/3][j/3][num] == 1) {
                    // 直接返回false
                    return false;
                }
                // 否则标记当前数字已经出现过
                row[i][num] = 1;
                col[j][num] = 1;
                box[i/3][j/3][num] = 1;
                // 继续遍历下一个位置
            }
        }

        return true;
    }
}