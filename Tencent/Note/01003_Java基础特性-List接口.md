# 01003. Java基础特性-List接口
## 基础概念
> List是 Java 集合框架中 Collection 接口的子接口，属于有序集合（序列），核心特征是允许存储重复元素、支持按索引访问元素，是开发中最常用的集合类型之一。
> List 接口定义了基于索引操作元素的核心规范，底层由不同实现类（ArrayList/LinkedList/Vector）完成具体逻辑，适配不同的性能场景。

核心特性：
1. 有序：元素的插入顺序与遍历顺序一致，可通过索引（从 0 开始）精准定位元素；
2. 可重复：允许存储多个相同的元素（判断依据是 equals() 方法）；
3. 索引访问：提供 get(int index)、set(int index, E element) 等索引操作方法；
4. 动态扩容（除 Vector 外）：支持动态调整容量

继承关系为：Collection <- List <- ArrayList / LinkedList / Vector

## ArrayList
ArrayList 的底层是一个动态调整大小的 Object 数组 `transient Object[] elementData` ，它继承于 AbstractList，实现了 `List` 、`RandomAccess` 、`Cloneable` 、`java.io.Serializable` 接口。与 Java 中的数组相比，它的容量能动态增长。在添加大量元素前可以使用 ensureCapacity() 操作来增加 ArrayList 实例的容量。这可以减少递增式再分配的数量。
JDK7 中 ArrayList 初始容量为 10，而 JDK8 中为 0（懒加载，首次添加元素时才初始化数组）。

ArrayList 的底层核心成员（非私有，便于内部类访问）：
```java
// 底层存储元素的数组
transient Object[] elementData;
// 集合中实际元素的数量
private int size;
// 默认初始容量（JDK 8 懒加载，首次添加元素时才初始化数组，减少空实例的内存浪费）
private static final int DEFAULT_CAPACITY = 10; // 初始容量为10
// 空数组常量（用于空实例初始化）
private static final Object[] EMPTY_ELEMENTDATA = {};
```

```java
// ArrayList 类的定义
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{
}
// List 表明它是一个列表，支持添加、删除、查找等操作，并且可以通过下标进行访问
// RandomAccess 表明它支持快速随机访问，即可以通过下标直接访问元素
// Cloneable 表明它支持拷贝，可以进行深浅拷贝操作
// java.io.Serializable 表明它支持序列化，即可以将对象转换为字节流进行存储或传输
```

扩容机制：
JDK8中，ArrayList 初始容量为 10，当添加元素导致 size >= elementData.length 时（size为原本数组的长度加上要添加的元素数）触发扩容流程：
1. 扩容1.5倍：新容量 = 旧容量 + 旧容量 / 2 （小数时向下取整）
2. 若新容量仍小于最小需求容量，则新容量 = 最小需求容量
3. 若新容量超过 Integer.MAX_VALUE - 8（数组最大容量上限），则新容量 = Integer.MAX_VALUE
4. 扩容完毕后进行数据迁移，将旧数组元素复制到新数组中：通过 `Arrays.copyOf(elementData, newCapacity)` 完成旧数组元素到新数组的拷贝（底层依赖 `System.arraycopy()` 本地方法）
```java
// 扩容方法源码（JDK8）
private void grow(int minCapacity) {
    // 旧容量 = 数组当前长度
    int oldCapacity = elementData.length;
    // 新容量 = 旧容量 + 旧容量/2（右移1位，等价于除以2，效率更高）
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    // 若新容量 < 最小需求容量，直接用最小需求容量
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    // 若新容量超过数组最大容量上限，调整到临界值
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // 数组拷贝：创建新数组并复制旧元素
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

ArrayList 的使用方式：
```java
// 初始化（指定容量，避免扩容）
List<String> list = new ArrayList<>(10);

// 添加元素（尾插/指定索引插）
list.add("Java"); // 尾插，O(1)
list.add(0, "Tencent"); // 索引0插入，O(n)

// 获取元素（O(1)）
String first = list.get(0); // "Tencent"

// 修改元素（O(1)）
list.set(1, "JavaSE"); // 索引1改为"JavaSE"

// 删除元素（指定索引/元素）
list.remove(0); // 删除索引0元素，O(n)
list.remove("JavaSE"); // 删除指定元素（需遍历查找），O(n)

// 包含判断（O(n)，需遍历对比equals）
boolean contains = list.contains("Java");

// 排序（基于Arrays.sort，底层双轴快排）
list.add("C++");
list.add("Python");
list.sort((s1, s2) -> s1.length() - s2.length()); // 按长度排序

// 遍历（三种方式，效率：普通for > 增强for ≈ 迭代器）
// 方式1：普通for（利用索引，ArrayList最优）
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}
// 方式2：增强for（底层迭代器）
for (String s : list) {
    System.out.println(s);
}
// 方式3：迭代器（支持遍历中删除）
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String s = iterator.next();
    if (s.equals("Python")) {
        iterator.remove(); // 安全删除，不会触发ConcurrentModificationException
    }
}
```

## LinkedList
LinkedList 的底层是双向链表（JDK1.6 及之前为循环双向链表，JDK1.7 + 取消循环），它继承于 AbstractSequentialList，实现了 `List` 、`Deque` 、`Cloneable` 、`java.io.Serializable` 接口。由于基于链表实现，它不支持快速随机访问（未实现 RandomAccess），但首尾节点的增删操作效率极高。

特性：
1. 非线程安全：方法未加同步锁，多线程环境下需手动保证线程安全
2. 双端操作：实现 Deque 接口，可作为队列（FIFO）、栈（LIFO）使用

LinkedList 的底层核心成员与节点结构：
```java
// 双向链表节点类
private static class Node<E> {
    E item; // 节点存储的元素
    Node<E> next; // 后继节点
    Node<E> prev; // 前驱节点

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}

// 链表实际元素数量
transient int size = 0;
// 头节点
transient Node<E> first;
// 尾节点
transient Node<E> last;
```

使用方式：
```java
// 初始化
List<String> linkedList = new LinkedList<>();
// 作为双端队列使用
Deque<String> deque = new LinkedList<>();

// 首尾添加元素（O(1)）
linkedList.addFirst("First"); // 头插
linkedList.addLast("Last"); // 尾插
deque.offer("QueueElem"); // 队列尾插
deque.push("StackElem"); // 栈头插

// 获取元素（O(n)）
String first = linkedList.get(0); // 需遍历到索引0节点
String head = deque.peek(); // 获取队列头（不删除）
String stackTop = deque.pop(); // 弹出栈顶（删除并返回）

// 修改元素（O(n)）
linkedList.set(1, "NewLast"); // 遍历到索引1节点并修改

// 删除元素（O(1)/O(n)）
linkedList.removeFirst(); // 头删，O(1)
linkedList.remove(1); // 删除索引1元素，O(n)
deque.poll(); // 队列头删，O(1)

// 遍历（推荐迭代器/增强for，普通for效率极低）
// 方式1：增强for（底层迭代器）
for (String s : linkedList) {
    System.out.println(s);
}
// 方式2：迭代器（支持遍历删除）
ListIterator<String> iterator = linkedList.listIterator();
while (iterator.hasNext()) {
    String s = iterator.next();
    if (s.equals("Last")) {
        iterator.remove();
    }
}
// 方式3：双端迭代器（逆序遍历）
Iterator<String> descIterator = linkedList.descendingIterator();
while (descIterator.hasNext()) {
    System.out.println(descIterator.next());
}
```

## Vector
Vector 是 Java 早期的有序集合实现类，底层是固定大小的 Object 数组（与 ArrayList 结构一致），继承于 AbstractList，实现了 `List` 、`RandomAccess` 、`Cloneable` 、`java.io.Serializable` 接口。核心特点是线程安全（方法加 synchronized 修饰），但效率较低，属于遗留类（JDK1.0 引入），开发中更推荐使用 ArrayList + Collections.synchronizedList() 替代。

特性：
1. 线程安全：所有公开方法（add/get/remove 等）均加 synchronized 关键字，多线程下无需额外同步
2. 性能劣势：synchronized 修饰导致单线程下效率远低于 ArrayList
3. 枚举遍历：支持 Enumeration 迭代器（古老方式，性能低于 Iterator）

核心成员与扩容机制：
```java
// 底层存储元素的数组
protected Object[] elementData;
// 实际元素数量
protected int elementCount;
// 扩容增量（若指定则按增量扩容，否则翻倍）
protected int capacityIncrement;

// 扩容方法源码（JDK8）
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    // 新容量：若指定了增量则+增量，否则翻倍
    int newCapacity = oldCapacity + ((capacityIncrement > 0) ? capacityIncrement : oldCapacity);
    // 校验新容量是否满足最小需求
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    // 校验是否超过数组最大容量上限
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // 数组拷贝迁移数据
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

使用方式：
```java
// 初始化（指定初始容量+扩容增量）
Vector<String> vector = new Vector<>(10, 5); // 初始容量10，扩容每次+5

// 添加元素（同步方法，O(1)）
vector.add("Java");
vector.add(0, "Vector"); // 索引插入，O(n)

// 获取元素（同步方法，O(1)）
String elem = vector.get(1);

// 修改元素（同步方法，O(1)）
vector.set(0, "VectorDemo");

// 删除元素（同步方法，O(n)）
vector.remove(0);

// 枚举遍历（古老方式，不推荐）
Enumeration<String> enumeration = vector.elements();
while (enumeration.hasMoreElements()) {
    System.out.println(enumeration.nextElement());
}

// 转为线程安全的ArrayList（推荐替代方案）
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
```

## 常用方法
1. add(E e)：添加元素到末尾
2. add(int index, E e)：在指定索引位置插入元素
3. get(int index)：获取指定索引位置的元素
4. set(int index, E e)：将指定索引位置的元素替换为新值
5. remove(int index)：删除指定索引位置的元素
6. size()：返回集合中元素的数量
7. isEmpty()：判断集合是否为空
8. clear()：清空集合中的所有元素

## 面试问题准备
Q：**ArrayList 和 LinkedList 的核心区别？适用场景分别是什么？**
A：ArrayList 和 LinkedList 最核心的差别是底层的数据结构完全不一样，ArrayList 是基于动态数组来实现的，内存空间是连续分配的，而 LinkedList 是双向链表，每个数据节点都会保存自身内容以及前后节点的引用，内存并不是连续排布的，这个底层差异直接决定了它们在操作效率和使用场景上的所有不同。
因为数组自带固定的下标索引，所以 ArrayList 做随机查询、根据位置直接获取元素的时候速度非常快，可一旦需要在列表中间插入或者删除元素，后面所有的元素都要批量移动位置，效率就会明显下降，数组到达容量上限后的扩容过程，还需要重新开辟内存并复制全部原有数据，也会产生额外开销。而 LinkedList 依靠链表的指针关联，在任意位置增删节点时，只需要修改相邻节点的引用关系，不用挪动其他数据，这部分操作的效率很高，但它没有数组那样的随机访问能力，想定位到某个指定位置的元素，只能从头或尾开始逐个遍历，查询效率远不如 ArrayList，再加上每个节点都要额外存储前后指针，整体的内存消耗也会比 ArrayList 更大。
在我的业务中，绝大多数常规业务我都会优先用 ArrayList，像展示用户订单列表、商品清单这类场景，主要操作都是查询和顺序遍历，新增和删除大多只在列表末尾执行，所以用 ArrayList 既能保证页面加载和数据读取的速度，内存使用也更轻量化。只有当业务需要频繁在列表中间位置做增删，或者要直接把集合当成队列、双端队列来处理任务，比如临时缓冲待消费的业务消息、流转后台任务时，我才会选用 LinkedList，依靠它链表结构的增删优势，去适配这种高频修改、几乎不做随机查询的场景来保证对应业务逻辑的执行效率。

Q：**ArrayList 的扩容机制是什么？**
A：其实 ArrayList 的扩容本质就是解决 "数组长度固定" 的问题，它底层是个 Object 类型的数组，一旦数组装不下新元素了，就会新建一个更大的数组，把原来的数据全复制过去，这就是扩容的核心逻辑。扩容的时机是每次调用 add () 或者 addAll () 添加元素时，会先检查 "当前元素个数（size）+ 要加的元素数" 是不是超过了数组当前的容量（capacity），如果超了，立刻触发扩容。第一步先算新容量，默认是按老容量的 1.5 倍来算，计算方式是老容量 + 老容量右移一位（相当于除以 2），比如老容量 10，扩容后就是 15，再满了就扩到 22（15+7）。不过如果是一次性 addAll 大量元素（比如一次加 20 个），算出来的 1.5 倍容量还不够，就会直接把新容量设为 “能装下所有元素的最小容量”，避免多次扩容浪费性能。用 Arrays.copyOf () 把原数组的所有元素，挨个复制到新的大数组里，这个复制过程是扩容的核心开销 —— 我做订单列表的时候就踩过这个坑，最开始没指定初始容量，默认 10，用户订单多的时候，列表频繁扩容，每次复制数组都要耗时间，订单列表加载明显变慢。后来我根据业务预估，初始化时直接指定容量，比如 new ArrayList<>(100)，刚好够存用户的历史订单，尽量避免了彻底避免了扩容，大幅提升了这个接口的响应速度。还有个小细节，扩容不是无限涨的，最大容量能到 Integer.MAX_VALUE - 8，这是为了避免虚拟机分配内存时出问题；如果业务真的需要更大的容量，会直接用 Integer.MAX_VALUE，但我项目运行这么久，实际业务里根本到不了这个上限。

Q：**List 遍历中删除元素为什么会报 ConcurrentModificationException？如何避免？**
A：报这个异常是因为List（以 ArrayList 为例）内部有个叫modCount的计数器，专门记录集合的结构性修改次数，比如 add、remove、clear 这些会改变集合大小的操作，都会让modCount加 1，当我们用 foreach 遍历（本质是用 Iterator 迭代器）时，迭代器初始化会先把当前的modCount值存到自己的expectedModCount变量里；遍历过程中，每次取元素前都会检查modCount和expectedModCount是否相等：如果我们在遍历中直接调用list.remove()，这个操作会让modCount加 1，但迭代器的expectedModCount还是旧值，两者一不一致，就会触发 “快速失败（fail-fast）” 机制，直接抛 ConcurrentModificationException，注意哪怕是单线程也会报这个错，因为核心是遍历和修改导致的校验不通过，另外，如果用普通正序 for 循环时删元素，除了可能报这个异常，还会因为删元素后 list.size () 变小，导致后续元素漏遍历。在实际项目中我解决这个问题的核心思路是让遍历和修改的计数器保持一致，或者绕开计数器校验。最标准的方法就是官方推荐的迭代器的remove()，迭代器的remove()方法在删除元素的同时，会把自己的expectedModCount同步成最新的modCount，这样遍历中的校验就不会失败。
如果是高并发场景（比如多个线程同时遍历、删除订单列表），普通 ArrayList 不管怎么遍历删都会出问题，这时候我就会使用CopyOnWriteArrayList。它的核心是"写时复制"：删元素时会复制一个新数组，在新数组里删除元素，再把原数组替换成新数组；遍历的时候读的是旧数组，完全不会触发并发修改异常。不过它的缺点是写操作（删 / 加）会耗内存（复制数组），所以只适合读多写少的场景，比如我做库存监控列表时用它，列表大部分时间是查询，偶尔删除失效的监控项，是性能完全够用的。

Q：**LinkedList 为什么不实现 RandomAccess 接口？**
A：因为 LinkedList 底层是双向链表，索引访问需从首尾节点开始遍历到目标索引，不满足快速随机访问的特性，而RandomAccess 是标记接口，用于标识集合支持快速随机访问，因此不实现该接口。

Q：**ArrayList 中的 elementData 为什么用 transient 修饰？**
A：因为elementData 数组通常有冗余空间（size < 数组长度），序列化全部数组会浪费空间，而且 ArrayList 重写了 writeObject/readObject 方法，仅序列化实际存储的元素而非整个数组可以减少序列化体积。

Q：**如何保证 List 的线程安全？**
A：1. 使用 Vector：方法加 synchronized，线程安全但性能低；2. Collections.synchronizedList (List)：包装 List，所有方法加同步锁（基于传入的 mutex 对象），性能优于 Vector；3. CopyOnWriteArrayList（JUC）：写时复制数组，读操作无锁，适合读多写少场景（写操作复制新数组，开销大）；4. 手动加锁：使用 synchronized/ReentrantLock 包裹 List 操作。

Q：**为什么 ArrayList 的扩容是 1.5 倍，而 Vector 是 2 倍？**
A：1.5倍扩容可以更好地平衡扩容频率和内存浪费，1.5 倍增长更平缓，减少内存碎片，且右移 1 位的计算效率高，而 Vector 是因为早期设计未考虑内存效率，翻倍扩容导致内存浪费更严重。