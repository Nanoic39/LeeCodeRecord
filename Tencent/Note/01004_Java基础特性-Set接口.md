# 01004. Java基础特性-Set接口
## 基础概念
> Set 是 Java 集合框架中 `Collection` 接口的子接口，核心特征是**不可重复、无索引**，是开发中用于去重、排序场景的核心集合类型。
> Set接口未提供索引相关方法（`get(int index)`），无法通过索引访问元素，遍历仅能通过迭代器、增强 for 循环实现。

### 核心特性：
1. 不可重复：不允许储存重复元素
2. 无索引：不能通过下标操作元素
3. 有序性区分：
	1. **HashSet** 无序，储存位置由`hashCode()`计算的哈希值决定，与插入顺序无关
	2. **LinkedHashSet** 插入有序（遍历顺序 = 插入顺序）
	3. **TreeSet** 排序有序（按自然顺序 / 定制顺序排序）
4. 非线程安全：均未加同步锁，多线程环境需手动保证线程安全
5. 允许储存null：
	1. **HashSet** 允许储存一个null
	2. **LinkedHashSet** 允许储存一个null
	3. **TreeSet** 不允许储存null，会导致排序时会抛空指针异常

Set 接口的类继承关系：
`Collection` <-- `Set` <-- 实现类：`HashSet` / `LinkedHashSet` / `TreeSet`

## HashSet
### 基本介绍
HashSet 是 Set 接口最常用的实现类，在 JDK 8+ 中底层基于 **HashMap** 实现（JDK 8+），核心设计目标是**高效去重、无序存储**，是开发中去重场景的首选。

### 底层结构
HashSet 本质是 HashMap 的 “包装类”，其所有元素都存储在 HashMap 的 `key` 位置，`value` 固定为一个静态常量 `PRESENT`（节省内存）
```java
// 底层依赖的HashMap对象 
private transient HashMap<E, Object> map;
// 所有key对应的固定value，节省内存（无需为每个key创建新Object） 
private static final Object PRESENT = new Object();

// HashSet 构造器本质是创建 HashMap：
// HashSet空参构造器 
public HashSet() { 
	map = new HashMap<>(); 
} 
// 指定初始容量的构造器 
public HashSet(int initialCapacity) { 
	map = new HashMap<>(initialCapacity); 
}
```

### 去重原理
核心是依赖 **HashMap 的 key 不可重复特性**，本质是元素的`hashCode()`和`equals()`方法协同工作，触发去重的条件是当调用`add(E e)`时，实际上是在调用`map.put(e, PRESENT)`;
1. HashMap先调用元素 **e** 的`hashCode()`计算哈希值，确定元素在哈希表中桶的位置
2. 若该桶位置无元素，直接将 **e** 作为 key 存入，添加成功
3. 若该桶位置已有元素（哈希冲突），则调用 `e.equals(已有元素)` 进行对比
	1. 若 `equals()` 返回 **true** 则认为是重复元素`map.put()`返回旧值，HashSet的`add()`方法返回 **false** ，不添加
	2. 若 `qequals()` 返回 **false** 则认为是不同元素，JDK 8 中会将新元素挂到桶的链表尾部（链表长度≥8 时转为红黑树），添加成功

若自定义类未重写`hashCode()`和`equals()`，则默认使用 Object 类的方法：
- `hashCode()` 返回对象的内存地址哈希值，不同对象哈希值不同
- `equals()` 对比对象的内存地址，只有同一对象才返回**true**

默认方法可能会因为不同对象哈希值不同，导致将相同内容判定为不同，导致去重失效，因此自定义类要实现去重**必须同时重写这两个方法**，以确保内容相同的对象被视为相等，从而实现正确去重

自定义类去重示例：
```java
// 自定义User类，重写hashCode和equals实现按id去重
class User {
    private Integer id;
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // 重写equals：id相同则认为是同一对象
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    // 重写hashCode：仅基于id计算哈希值
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}

// 测试HashSet去重
public class HashSetDemo {
    public static void main(String[] args) {
        Set<User> userSet = new HashSet<>();
        userSet.add(new User(1, "nanoic"));
        userSet.add(new User(1, "NANO1C")); // id相同，视为重复，不添加
        userSet.add(new User(2, "Yuna"));

        // 输出：[User{id=1, name='nanoic'}, User{id=2, name='Yuna'}]
        System.out.println(userSet);
    }
}
```

### 使用示例
```java
Set<String> hashSet = new HashSet<>();
hashSet.add("腾讯");
hashSet.add("NANO1C");
hashSet.add("字节");
hashSet.add("YUNA");

// 输出顺序大概率不是 [腾讯, NANO1C, 字节, YUNA]（可能是：[YUNA, 腾讯, 字节, NANO1C]）
System.out.println(hashSet);
```

## LinkedHashSet
### 基本介绍
LinkedHashSet 是 HashSet 的子类，底层基于 **LinkedHashMap** 实现，核心特征是**插入有序 + 去重**，兼顾 HashSet 的高效和 List 的有序性。

### 底层结构
LinkedHashSet 继承自 HashSet，构造器调用父类 HashSet 的隐藏构造器，创建 LinkedHashMap 作为底层存储
```java
// LinkedHashSet空参构造器
public LinkedHashSet() {
    super(16, 0.75f, true); // 调用HashSet的构造器，指定accessOrder=false（插入有序）
}

// HashSet的隐藏构造器（仅被LinkedHashSet调用）
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```

底层在 HashMap 的 "数组 + 链表 / 红黑树" 基础上，增加了**双向链表**，用于维护元素的插入顺序，遍历 LinkedHashMap 时实际上是在遍历该双向链表，因此插入顺序就是遍历顺序

去重原理与 HashSet 一致，性能比HashSet稍慢（因为存在维护双向链表的开销），但是远高于 TreeSet

### 使用示例
```java
Set<String> linkedHashSet = new LinkedHashSet<>();
hashSet.add("腾讯");
hashSet.add("NANO1C");
hashSet.add("字节");
hashSet.add("YUNA");

// 输出顺序固定为 [腾讯, NANO1C, 字节, YUNA]（插入有序）
System.out.println(linkedHashSet);
```

## TreeSet
### 基本介绍
TreeSet 是 SortedSet 接口的实现类，底层基于 **TreeMap** 实现（TreeMap 底层是红黑树），核心特征是**排序有序 + 去重**，适用于需对元素排序的场景。

### 底层结构
TreeSet 底层依赖 TreeMap，元素存储在 TreeMap 的 `key` 位置，`value` 为固定常量 `PRESENT`
```java
// TreeSet核心成员
private transient NavigableMap<E, Object> m;
private static final Object PRESENT = new Object();

// TreeSet构造器
public TreeSet() {
    this(new TreeMap<>()); // 底层创建TreeMap
}
```

红黑树是自平衡的二叉查找树，保证插入、删除、查找的时间复杂度为 O(log n) ，且始终维持树的平衡

### 排序原理
主要为两种排序方式：
#### **自然排序（默认）**：
1. 要求：实现 `Comparable` 接口，重写 `compareTo(T o)` 方法；
2. 排序规则：根据 `compareTo()` 返回值排序（返回负数：当前元素在前；0：重复元素；正数：当前元素在后）
3. 去重原理：`compareTo()` 返回 0 则认为是重复元素，不添加
4. 常用实现类：String、Integer、Long 等（已实现`Comparable`）
```java
// 示例：
// Integer已实现Comparable，默认升序排序
Set<Integer> treeSet = new TreeSet<>();
treeSet.add(3);
treeSet.add(1);
treeSet.add(2);
treeSet.add(3); // 重复，不添加

// 输出：[1, 2, 3]（自然升序）
System.out.println(treeSet);
```
#### **定制排序（自定义规则）**：
1. 创建 TreeSet 时传入 `Comparator` 接口实现类（匿名内部类 / Lambda 表达式），重写 `compare(T o1, T o2)` 方法
2. 排序规则：根据 `compare()` 返回值排序（返回负数：o1 在前；0：重复；正数：o1 在后）
3. 去重原理：`compare()` 返回 0 则认为是重复元素
4. 优势：无需修改元素类的代码，灵活定制排序规则
```java
// 示例：自定义 User 类按照 name 长度排序
// 自定义User类（无需实现Comparable）
class User {
    private Integer id;
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }

    // getter方法（供Comparator使用）
    public String getName() {
        return name;
    }
}

// 测试TreeSet定制排序
public class TreeSetDemo {
    public static void main(String[] args) {
        // 传入Comparator：按name长度升序，长度相同则按id降序
        Set<User> treeSet = new TreeSet<>((u1, u2) -> {
            int lenCompare = u1.getName().length() - u2.getName().length();
            if (lenCompare != 0) {
                return lenCompare;
            }
            return u2.getId() - u1.getId(); // 长度相同，id降序
        });

        treeSet.add(new User(1, "nanoic"));    // name长度6
        treeSet.add(new User(2, "NANO1C"));    // name长度6（id更大，排在前）
        treeSet.add(new User(3, "YunaNexus"));  // name长度9

        // 输出：[User{id=2, name='NANO1C'}, User{id=1, name='nanoic'}, User{id=3, name='YunaNexus'}]
        System.out.println(treeSet);
    }
}
```

### 注意事项
1. 不允许储存null，因为排序时会调用 `compareTo()`/`compare()`，如果存在 null 会触发`NullPointerException`异常
2. 排序元素必须是同一类型（如全为 String 或全为 User），否则排序时会抛`ClassCastException`异常
3. 基于红黑树特性，`add`/`remove`/`contains`的时间复杂度均为 O(log n)

## 常用方法
### 添加元素
- `boolean add(E e)`：添加元素，重复则不添加。添加成功返回 true，重复返回 false。
- `boolean addAll(Collection<? extends E> c)`：批量添加集合 c 的元素。有元素添加成功返回 true，否则 false。

### 删除元素
- `boolean remove(Object o)`：删除指定元素。删除成功返回 true，元素不存在返回 false。
- `void clear()`：清空所有元素。无返回值。

### 判断/查询
- `boolean contains(Object o)`：判断是否包含指定元素。包含返回 true，否则 false。
- `boolean isEmpty()`：判断 Set 是否为空。空返回 true，否则 false。
- `int size()`：获取元素数量。返回实际元素个数。

### 遍历/转换
- `Iterator<E> iterator()`：获取迭代器。返回 Iterator 对象。
- `Object[] toArray()`：转为 Object 数组。返回包含所有元素的数组。
- `<T> T[] toArray(T[] a)`：转为指定类型数组。返回类型匹配的数组。

### 使用示例
```java
// 示例
public class SetCommonMethodDemo {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        // 1. 添加元素
        set.add("Java");
        set.add("Python");
        set.add("Java"); // 重复，不添加
        System.out.println(set); // [Python, Java]

        // 2. 批量添加
        Set<String> anotherSet = new HashSet<>();
        anotherSet.add("C++");
        anotherSet.add("Java");
        set.addAll(anotherSet);
        System.out.println(set); // [Python, Java, C++]

        // 3. 判断包含
        System.out.println(set.contains("Python")); // true

        // 4. 删除元素
        set.remove("C++");
        System.out.println(set); // [Python, Java]

        // 5. 遍历（三种方式）
        // 方式1：迭代器
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        // 方式2：增强for
        for (String s : set) {
            System.out.println(s);
        }

        // 方式3：JDK8+ forEach
        set.forEach(System.out::println);

        // 6. 转换为数组
        String[] arr = set.toArray(new String[0]);
        System.out.println(Arrays.toString(arr)); // [Python, Java]
    }
}
```

## 面试问题准备
Q：**HashSet 的去重原理？为什么必须同时重写 hashCode () 和 equals ()？**
A：HashSet 底层就是 "包装了一个 HashMap"，往 HashSet 里添加元素时，这个元素会作为 HashMap 的 Key 存入，而Value 是一个固定的空对象（PRESENT），所以 HashSet 的去重，本质就是 HashMap 保证 Key 不重复的过程，分为三步：首先通过调用待添加元素的 hashCode() 方法计算哈希值，根据哈希值定位到 HashMap 数组的某个桶位（也就是下标），然后判断桶位是否为空，如果为空就直接把元素存进去，去重校验结束，否则如果桶位里已有元素（可能是单个元素、链表或是红黑树），就需要逐个调用这些已有元素的 equals() 方法，和新元素做内容比较，只要有一个 equals() 返回 true，就判定为重复元素，HashSet 的 add() 方法返回false，不进行存储，否则就判定为新元素，存入桶位（链表尾 / 红黑树），然后add()返回 true 。
至于为什么必须同时重写 hashCode() 和 equals() 是因为两个方面：一方面是 Object 类默认的 hashCode() 是根据对象的内存地址计算的，如果不重写 hashCode() 会导致即使两个对象即使内容完全相同也会因为实例不同而判定为 hashCode 不同，导致分到不同桶位，又因为桶位不同，HashSet 根本不会调用 equals () 比较内容；另一方面是 Object 默认的 equals() 也是比较 内存地址，就算两个元素内容相同、hashCode 相同，equals 还是会判定为不同。只有同时重写两个方法，才能自行满足内容相同的元素hashCode和equals相同的条件

> 去重原理简单来说就是："先靠 hashCode 找位置，再靠 equals 判内容"，这两步缺一不可！

Q：**HashSet、LinkedHashSet、TreeSet 有哪些区别？分别适用什么场景？**
A：这三种 Set 的核心区别其实就围绕 “有序性”“排序能力” 和 “性能” 展开，这三个都满足 Set “元素不可重复” 的核心特性，但底层结构有很大差异，HashSet基于HashMap、LinkedHashSet基于LinkedHashMap、TreeSet则是TreeMap 的红黑树结构。HashSet只做高效去重，性能是最高的，但是完全不管顺序，我在缓存中临时存储已参与抽奖的用户时会使用。LinkedHashSet 因为多了链表维护顺序，性能与 HashSet 比相对较弱，内存也开会稍高一点，我做用户最近浏览记录时会使用这种方式实现既要去重又按用户浏览的先后顺序展示的功能。TreeSet 核心能力是排序，而不是插入有序，性能是三者中最差，我商品进行销量排行时会使用这种方式。

Q：**Set 遍历中删除元素为什么会报 ConcurrentModificationException？如何避免？**
A：Set 遍历中删除元素报错 ConcurrentModificationException 是因为使用了增强 for 循环（或直接使用集合的 remove 方法）在迭代过程中修改了结构，过程中直接调用 set.remove() 修改集合，modCount 变化，触发了 Set 的迭代器采用快速失败（fail-fast）机制，迭代器检测到后抛出异常。可以使用使用迭代器的 remove() 方法避免此问题，迭代器 remove() 后会同步更新 modCount

Q：**TreeSet 为什么不允许存储 null？另外两个为什么允许储存 null？**
A：reeSet 排序时会调用元素的 compareTo()（自然排序）或 Comparator.compare()（定制排序），若元素为 null，调用方法时会触发 NullPointerException，因此 TreeSet 禁止存储 null；而 HashSet/LinkedHashSet 仅依赖 hashCode() 和 equals()，null 的 hashCode() 为 0，equals() 可正常对比，因此允许存储一个 null。

Q：**如何保证 Set 的线程安全？**
A：四种方法：使用`Collections.synchronizedSet(Set)`：包装 Set，所有方法加同步锁，线程安全但性能一般；或使用`ConcurrentSkipListSet`（JUC）：基于跳表实现，线程安全，支持排序，适用于高并发场景；或使用`CopyOnWriteArraySet`（JUC）：基于 CopyOnWriteArrayList 实现，写时复制数组，读操作无锁，适合读多写少场景；也可以手动加锁：使用 `synchronized`/`ReentrantLock` 包裹 Set 操作。