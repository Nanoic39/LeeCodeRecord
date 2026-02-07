package Tencent.LeeCode.DFS_BFS_207;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 边界条件：没有先修课程的情况直接返回true
        if(prerequisites == null || prerequisites.length == 0) {
            return true;
        }

        // 判断prerequisites是否能够成环，若能成环则无法完成所有课程
        // 这里使用BFS来求解，更加适合实际工程中使用
        // 构建邻接表：存储有向图的边关系，adj.get(b) 表示b指向的所有节点
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        // 记录入度
        int[] inDegree = new int[numCourses];

        // 填充邻接表和入度数组
        for(int[] p : prerequisites) {
            int a = p[0]; // 要学的课程
            int b = p[1]; // 先修的课程

            adj.get(b).add(a); // 填充邻接表，b指向a
            inDegree[a]++; // a的入度增加(a多了前置依赖b)
        }

        // 入度为0的入队
        Queue<Integer> queue = new LinkedList<>();
        for(int i = 0; i < numCourses; i++) {
            if(inDegree[i] == 0) {
                queue.offer(i); 
            }
        }
        
        // BFS遍历
        int count = 0; // 已学完的课程
        // 还有可以直接学习的课程
        while (!queue.isEmpty()) {
            // 弹出队首节点
            int u = queue.poll();
            count++;

            // 遍历u的所有临接节点
            for(int v : adj.get(u)) {
                inDegree[v]--; // u学完后v入度-1
                // 如果v的入度变为0，说明v的所有前置课程都学完了，入队
                if(inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // 判断是否学完全部课程
        return count == numCourses;
    }
}
