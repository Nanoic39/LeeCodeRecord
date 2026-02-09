class MyLinkedList {
    class ListNode {
        int val; // 值
        ListNode prev; // 前驱节点
        ListNode next; // 后继节点
    
        public ListNode(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }
    }

    private ListNode dummyHead; // 虚拟头结点
    private ListNode dummyTail; // 虚拟尾节点
    private int size; // 实际节点数

    // 初始化
    public MyLinkedList() {
        // 创建头尾虚拟节点
        dummyHead = new ListNode(0);
        dummyTail = new ListNode(0);
        // 编辑指向
        dummyHead.next = dummyTail;
        dummyTail.prev = dummyHead;
        // 初始化大小
        size = 0;
    }
    
    // 获取指定下标节点值
    public int get(int index) {
        // 下标无效时返回-1
        if(index < 0 || index >= size) {
            return -1;
        }
        // 否则根据位置选择便利方向进行遍历
        ListNode curr;
        if (index < size / 2) {
            // 从前往后
            curr = dummyHead.next;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        } else {
            // 从后往前
            curr = dummyTail.prev;
            for(int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
        }

        return curr.val;
    }
    
    // 头插节点
    public void addAtHead(int val) {
        // 创建要插入的节点
        ListNode newNode = new ListNode(val);
        ListNode head = dummyHead.next; // 原本的头节点
        // 构造：虚拟头节点 -> 新节点 -> 原本的头节点（双向指针，先连接新节点，再断开旧节点）
        
        // 虚拟头节点 -> 新节点
        dummyHead.next = newNode;
        newNode.prev = dummyHead;
        // 新节点 -> 原本的头结点
        newNode.next = head;
        head.prev = newNode;

        // 节点数+1
        size++;
    }
    
    // 尾插节点
    public void addAtTail(int val) {
        // 创建要插入的节点
        ListNode newNode = new ListNode(val);
        ListNode tail = dummyTail.prev; // 原本的尾节点
        // 构造：原本的尾节点 -> 新节点 -> 虚拟尾节点（双向指针，先连接新节点，再断开旧节点）
        
        // 原本的尾结点 -> 新节点
        tail.next = newNode;
        newNode.prev = tail;

        // 新节点 -> 虚拟尾节点
        newNode.next = dummyTail;
        dummyTail.prev = newNode;

        // 节点数+1
        size++;
    }
    
    // 在 index 前插入节点
    public void addAtIndex(int index, int val) {
        // 如果 index > size（不在合法范围内）
        // 这里不是 ≥ 是因为等于size时可以在size位置（最后一个元素之后）插入，因此也是合法的
        if (index > size) {
            return;
        }
        // 如果 index <= 0（头插）
        if (index <= 0) {
            addAtHead(val);
            return;
        }
        // 如果 index == size（尾插）
        if(index == size) {
            addAtTail(val);
            return;
        }

        // 否则找到 index 节点，然后在前驱插入
        ListNode curr = dummyHead.next;
        ListNode newNode = new ListNode(val);
        for(int i = 0; i < index; i++) {
            curr = curr.next;
        }
        // 需要设置为：prev -> newNode -> curr
        ListNode prev = curr.prev;
        prev.next = newNode;
        newNode.prev = prev;
        newNode.next = curr;
        curr.prev = newNode;

        // 节点数+1
        size++;
    }
    
    // 删除下标为 index 的节点
    public void deleteAtIndex(int index) {
        // 下标不在合法范围（删除时不允许超限）
        if (index < 0 || index >= size) {
            return;
        }
    
        // 否则找到要删除的节点
        ListNode curr = dummyHead.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        // 调整为 prev -> next
        ListNode prev = curr.prev;
        ListNode next = curr.next;

        prev.next = next;
        next.prev = prev;

        // 将 curr 删除（指向null）
        curr.next = null;
        curr.prev = null;

        // 节点数-1
        size--;
    }
}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */