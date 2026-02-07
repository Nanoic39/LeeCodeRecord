package Tencent.LeeCode.LinkedList_TowPoint_141;

// 题目中已给出
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
}

class Solution {
    public boolean hasCycle(ListNode head) {
        // 如果只有一个节点，则表示无环，直接返回false
        if(head==null || head.next==null) {
            return false;
        }

        // 定义快慢指针(不同步长的指针)
        // 原理为：如果链表存在环，则快指针一定会在某个时刻追上慢指针；否则快指针会优先到达链表末尾
        // 这样只需要O(1)的空间复杂度和O(n)的时间复杂度

        // 初始化快慢指针
        ListNode fast = head;
        ListNode slow = head;

        // 循环遍历链表
        // 若链表长度为偶数，则fast会先到达null，若链表长度为奇数，则fast.next会先到达null
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            // 如果快慢指针相遇
            if(slow == fast) {
                return true;
            }
        }

        // 循环结束，快指针到达末尾
        return false;
    }
}
