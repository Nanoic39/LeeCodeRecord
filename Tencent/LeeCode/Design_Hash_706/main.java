class MyHashMap {
    // 桶数组 + 链表(默认) / 红黑树(链表长度>=8且桶数组长度>=64)
    // 红黑树节点<=6时转回链表

    // 红黑树节点（集成链表节点，直接复用Key/Value）
    class TreeNode extends Node {
        TreeNode parent; // 父节点
        TreeNode left; // 左子节点
        TreeNode right; // 右子节点
        boolean red; // 是否为红节点
    
        public TreeNode(int key, int value) {
            super(key, value);
            this.red = true; // 新节点默认红色（减少红黑树调整次数）
        }
    }

    class Node {
        int key;
        int value;
        Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public Node(int key, int value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // 手撕红黑树到底有多爽
    class RedBlackTree {
        private TreeNode root; // 红黑树根节点

        // 左旋
        private void rotateLeft(TreeNode node) {
            TreeNode rightChild = node.right;
            // 将 rightChild 的左子树挂载到 node 的右子树
            node.right = rightChild.left;
            if (rightChild.left != null) {
                rightChild.left.parent = node;
            }

            // 处理 rightChild 的父节点
            rightChild.parent = node.parent;
            if (node.parent == null) {
                // 如果 node 是根节点
                root = rightChild;
            }
            else if (node == node.parent.left) {
                // node 是左子节点
                node.parent.left = rightChild;
            }
            else {
                // node 是右子节点
                node.parent.right = rightChild;
            }

            // 将 node 挂载到到 rightChild 的左子树
            rightChild.left = node;
            node.parent = rightChild;
        }

        // 写完左旋写右旋
        private void rotateRight(TreeNode node) {
            TreeNode leftChild = node.left;
            // 将 leftChild 的右子树挂载到 node 的左子树
            node.left = leftChild.right;
            if (leftChild.right != null) {
                leftChild.right.parent = node;
            }

            // 处理 leftChild 的父节点
            leftChild.parent = node.parent;
            if (node.parent == null) {
                // 如果 node 是根节点
                root = leftChild;
            }
            else if (node == node.parent.right) {
                // node 是右子节点
                node.parent.right = leftChild;
            }
            else {
                // node 是左子节点
                node.parent.left = leftChild;
            }

            // 将 node 挂载到到 leftChild 的左子树
            leftChild.right = node;
            node.parent = leftChild;
        }

        // 修复红黑树平衡
        private void fixAfterInsert(TreeNode node) {
            node.red = true; // 新节点默认红色
            TreeNode curr = node;
            
            // 循环调整：当前节点的父节点如果是红色
            while (curr != null && curr != root && curr.parent.red) {
                TreeNode parent = curr.parent;
                TreeNode grandpa = parent.parent; // 组父节点（一定存在，因为paren是红色，根是黑色）

                if (parent == grandpa.left) {
                    // 父节点是祖父节点的左子节点
                    TreeNode uncle = grandpa.right; // 叔叔节点

                    // 如果叔叔节点是红色（直接变色）
                    if (uncle != null && uncle.red) {
                        parent.red = false;
                        uncle.red = false;
                        grandpa.red = true;
                        curr = grandpa; // 继续向上调整
                    } else {
                        // 叔叔节点是黑色，且当前节点式右子节点（先左旋父节点）
                        if (curr == parent.right) {
                            curr = parent;
                            rotateLeft(curr);
                        }

                        // 叔叔节点是黑色，且当前节点是左子节点（右旋祖父节点并变色）
                        parent.red = false;
                        grandpa.red = true;
                        rotateRight(grandpa);
                    }
                } else {
                    // 父节点是祖父节点的右子节点（与上面的情况堆成）
                    TreeNode uncle = grandpa.left;

                    if (uncle != null && uncle.red) {
                        parent.red = false;
                        uncle.red = false;
                        grandpa.red = true;
                        curr = grandpa;
                    } else {
                        if (curr == parent.left) {
                            curr = parent;
                            rotateRight(curr);
                        }
                        parent.red = false;
                        grandpa.red = true;
                        rotateLeft(grandpa);
                    }
                }
            }

            root.red = false;
        }

        // 红黑树插入
        public void put(int key, int value) {
            // 查找是否已经存在 key
            TreeNode curr = root;
            TreeNode parent = null;
            while (curr != null) {
                parent = curr;
                
                if (key < curr.key) {
                    curr = curr.left;
                } 
                else if (key > curr.key) {
                    curr = curr.right;
                }
                else {
                    curr.value = value; // key 已存在，更新value
                    return;
                }
            }

            // Key 不存在，新增节点
            TreeNode newNode = new TreeNode(key, value);
            newNode.parent = parent;

            if (parent == null) {
                root = newNode; // 树为空时，新节点是根
            }
            else if (key < parent.key) {
                parent.left = newNode;
            }
            else {
                parent.right = newNode;
            }

            // 修复红黑树平衡
            fixAfterInsert(newNode);
        }

        // 红黑树查询
        public int get(int key) {
            TreeNode curr = root;

            while (curr != null) {
                if (key < curr.key) {
                    curr = curr.left;
                }
                else if (key > curr.key) {
                    curr = curr.right;
                }
                else {
                    return curr.value; // 找到 key，返回value
                }
            }

            return -1; // key 不存在
        }

        // 红黑树删除
        public void remove(int key) {
            // 这里只使用简化实现（仅删除叶子节点）
            // 核心思路：
            // 1. 找到节点
            // 2. 替换为后继节点
            // 3. 修复平衡
            TreeNode curr = root;
            TreeNode target = null;

            while (curr != null && target == null) {
                if (key < curr.key) {
                    curr = curr.left;
                }
                else if (key > curr.key) {
                    curr = curr.right;
                }
                else {
                    target = curr;
                }
            }
            if (target == null) {
                return; // 无该节点
            }
            // 简化实现
            if (target.left == null && target.right == null) {
                if (target == root) {
                    root = null;
                }
                else if (target == target.parent.left) {
                    target.parent.left = null;
                }
                else {
                    target.parent.right = null;
                }
            }
        }

        // 获取根节点（用于外部判断树是否为空）
        public TreeNode getRoot() {
            return root;
        }
    }

    // 核心参数
    private static final int DEFAULT_CAPACITY = 16; // 初始桶数（2的幂）
    private static final int TREEIFY_THRESHOLD = 8; // 链表转红黑树阈值
    private static final int UNTREEIFY_THRESHOLD = 6; // 红黑树转链表阈值
    private Node[] buckets; // 桶数组
    private int size; // 键值对总数

    // 构造函数
    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // 优化哈希函数
    private int hash(int key) {
        return key ^ (key >>> 16); // 高位异或低位，减少哈希冲突
    }

    // 计算桶索引
    private int getIndex(int key) {
        return hash(key) & (buckets.length - 1); // 2的幂时，等价于取模，效率更高
    }

    // 统计链表长度
    private int getLinkedListLength(Node head) {
        int len = 0;
        Node curr = head;
        while (curr != null) {
            len++;
            curr = curr.next;
        }
        return len;
    }

    // 链表转红黑树
    private void treeify(Node head, int index) {
        RedBlackTree tree = new RedBlackTree();
        // 遍历链表，插入到红黑树
        Node curr = head;
        while (curr != null) {
            tree.put(curr.key, curr.value);
            curr = curr.next;
        }
        // 桶数组中存储红黑树根节点（用TreeNode强转）
        buckets[index] = tree.getRoot();
    }

    // 插入/更新键值对（最优版核心）
    public void put(int key, int value) {
        int index = getIndex(key);
        Node head = buckets[index];

        // 桶为空，直接插入链表节点
        if (head == null) {
            buckets[index] = new Node(key, value);
            size++;
            return;
        }

        // 桶中是红黑树
        if (head instanceof TreeNode) {
            RedBlackTree tree = new RedBlackTree();
            // 简化：重新构造红黑树（面试版，源码更高效）
            tree.put(key, value);
            buckets[index] = tree.getRoot();
            return;
        }

        // 桶中是链表
        Node curr = head;
        Node prev = null;
        while (curr != null) {
            if (curr.key == key) {
                curr.value = value; // 更新value
                return;
            }
            prev = curr;
            curr = curr.next;
        }
        // 链表中无该key，新增节点（尾插，源码用尾插）
        prev.next = new Node(key, value);
        size++;

        // 检查是否需要转红黑树
        int len = getLinkedListLength(head);
        if (len >= TREEIFY_THRESHOLD && buckets.length >= 64) {
            treeify(head, index);
        }
    }

    // 查询（最优版核心）
    public int get(int key) {
        int index = getIndex(key);
        Node head = buckets[index];

        if (head == null) return -1;

        // 桶中是红黑树
        if (head instanceof TreeNode) {
            RedBlackTree tree = new RedBlackTree();
            // 简化：重新构造树（面试版）
            return tree.get(key);
        }

        // 桶中是链表
        Node curr = head;
        while (curr != null) {
            if (curr.key == key) {
                return curr.value;
            }
            curr = curr.next;
        }
        return -1;
    }

    // 删除
    public void remove(int key) {
        int index = getIndex(key);
        Node head = buckets[index];
        if (head == null) return;

        // 桶中是红黑树
        if (head instanceof TreeNode) {
            RedBlackTree tree = new RedBlackTree();
            tree.remove(key);
            buckets[index] = tree.getRoot();
            size--;
            return;
        }

        // 桶中是链表
        Node curr = head;
        Node prev = null;
        while (curr != null) {
            if (curr.key == key) {
                if (prev == null) {
                    buckets[index] = curr.next; // 删除头节点
                } else {
                    prev.next = curr.next; // 删除中间节点
                }
                size--;
                // 检查是否需要转回链表（简化版）
                int len = getLinkedListLength(buckets[index]);
                if (len <= UNTREEIFY_THRESHOLD) {
                    // 无需转，链表长度已小
                }
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }
}

/**
 * Your MyHashMap object will be instantiated and called as such:
 * MyHashMap obj = new MyHashMap();
 * obj.put(key,value);
 * int param_2 = obj.get(key);
 * obj.remove(key);
 */