package Tencent.LeeCode.Tree_DFS_124;

// Definition for a binary tree node.
public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
 
class Solution {

    // 最大路径和初始化为最小值
    private int maxSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        // nodePathSum = dfs(node.left) + node.value + dfs(node.right)

        // 边界处理
        if (root == null) {
            return 0;
        }

        // 开始dfs;
        dfs(root);

        return maxSum;
    }

    private int dfs(TreeNode node) {
        // 如果节点为空
        if (node == null) {
            return 0;
        }

        // 后序遍历
        int leftMax = Math.max(dfs(node.left), 0); // 左子树（如果dfs结果为负数则直接舍弃）
        int rightMax = Math.max(dfs(node.right), 0); // 右子树（同上）

        // 计算当前 node 为最高点的路径和
        maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

        // 返回以当前node为起点，向下延伸的向左或向右其中最大的和（便于上层继续计算）
        return node.val + Math.max(leftMax, rightMax);
    }
}