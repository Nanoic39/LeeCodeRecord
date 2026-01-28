# HashMap与ConcurrentHashMap
> HashMap是线程不安全的，ConcurrentHashMap是线程安全的。

HashMap 的底层实现在 JDK1.8 之前是数组 + 链表（"拉链法"解决哈希冲突），在 JDK1.8 调整为数组 + 链表 + 红黑树（当链表长度大于等于阈值8时会转化为红黑树，以提高查询效率；但是当链表长度小于64时会优先数组扩容）
HashMap 的默认初始容量为16，之后每次扩容容量变为原来的2倍。而且HashMap 总是使用 2 的幂作为哈希表的大小。

