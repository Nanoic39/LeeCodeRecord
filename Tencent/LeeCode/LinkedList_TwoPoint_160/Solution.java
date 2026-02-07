package Tencent.LeeCode.LinkedList_TwoPoint_160;

// 题中已给出
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution {
    // listA中节点数目m，listB中节点数目n
    // 1 <= m, n <= 3 * 10^4
    // 1 <= Node.val <= 10^5
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // 定义双指针
        ListNode pointA = headA;
        ListNode pointB = headB;

        // 遍历两个链表，当指针相等时只有可能有两种情况：
        // 1. 两个指针相遇，指向同一位置
        // 2. 两个指针都遍历完了链表，都指向了null
        while (pointA != pointB) {
            // 如果指针A遍历完链表A，则指向链表B头节点，否则向下一个运行
            if (pointA == null) {
                pointA = headB;
            } else {
                pointA = pointA.next;
            }
            // 指针B同理
            if (pointB == null) {
                pointB = headA;
            } else {
                pointB = pointB.next;
            }
        }

        // 返回哪个都可以，结果不是相遇的值就是null（这里正好是null，如果需要返回其他值则需要特殊处理）
        return pointA;
    }
}
