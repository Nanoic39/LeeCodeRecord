package Tencent.LeeCode.DFS_BFS_200;

class Solution {
    int m, n;

    public int numIslands(char[][] grid) {
        // 统计岛屿数量
        int count = 0;

        // 长宽
        m = grid.length;
        n = grid[0].length;

        // 遍历网格
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    // 深度优先递归将所有相邻1标记为已访问
                    dfs(grid, i, j);
                }
            }
        }

        return count;
    }

    // 网格规模较大时(例如1000*1000以上)需要改用BFS，否则会递归栈溢出
    private void dfs(char[][] grid, int i, int j) {
        // 条件为：i、j在网格范围内且当前位置为1
        if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == '0') {
            return;
        }

        // 标记当前位置为已访问
        grid[i][j] = '0';

        // 递归访问上下左右
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }
}
