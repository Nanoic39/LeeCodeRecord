# 00001. Java语法核心-Stream流
## 基础概念
> Stream（流）是 Java 8 中处理集合的关键抽象概念，它可以可以让你以一种声明的方式对集合执行非常复杂的查找、过滤、映射等操作。
> Stream API 借助于Lambda 表达式，极大的提高了编程效率和程序的可读性。

特性：
1. **不储存**：Stream 不是一种数据结构，不会储存元素；
2. **不改变源**：Stream 操作不会改变源对象，只会返回一个持有结果的新 Stream；
3. **延迟执行**：Stream 操作是延迟执行的，只有在终止操作时才会触发实际的计算，如果不调用最后的终止操作，则中间操作不会执行。

## 创建方式
```java
// 从集合创建流
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
Stream<Integer> stream = list.stream();

// 从数组创建流：Arrays 的静态方法 stream() 可以获取数组流
int[] array = {1, 2, 3, 4, 5};
IntStream stream = Arrays.stream(array);

// 从值创建流
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);

// 从文件创建流
Stream<String> stream = Files.lines(Paths.get("file.txt"));
```

## 中间操作
```java
// filter-过滤：筛选出符合条件的元素
// 作用 ：接收一个 Predicate （断言型接口），结果为 true 的元素会被保留。 
// 场景 ：从用户列表中筛选出指定条件的用户。
// 例：筛选出偶数
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.stream()
    .filter(i -> i % 2 == 0)
    .forEach(System.out::println); // 输出 2, 4

// map-映射：将流中的每个元素映射为另一个元素
// 作用 ：接收一个 Function （函数型接口），将元素转换成其他形式或提取信息。 
// 场景 ： DTO转换（比如把 User 实体对象列表转换成 UserVO 视图对象列表；或者从 User 列表中提取出所有的 id 组成一个新的 List）。
List<String> list = Arrays.asList("a", "b", "c");
// 转换大写
list.stream()
    .map(str -> str.toUpperCase()) 
    .forEach(System.out::println); // 输出 A, B, C

// flatMap-压平映射：将流中的每个元素映射为一个流，然后将所有流连接成一个流。
// 作用 ：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。 
// 场景：解决“套娃”问题（如果你有一个 List<List<String>> （列表里套列表），用 map 处理后还是两层结构，用 flatMap 可以直接把里面的元素全部“铺平”成一个层级的 Stream<String>）。
List<String> list1 = Arrays.asList("a", "b");
List<String> list2 = Arrays.asList("c", "d");
List<List<String>> listStrs = Arrays.asList(list1, list2);
// 效果：[[a, b], [c, d]] -> [a, b, c, d]
listStrs.stream()
        .flatMap(item -> item.stream()) // 将每个小List变成Stream，然后合并
        .forEach(System.out::println);

// sorted-排序：对流中的元素进行排序。
// 作用 ：对流中的元素进行排序。sort() 方法默认是自然排序（升序），流中元素必须实现 Comparable 接口。如果流中的元素没有实现 Comparable 接口，或者你想自定义排序规则，就可以传入自定义的 Comparator 进行排序。
// 场景 ：对用户列表按年龄排序、对商品列表按价格排序等。
List<String> list = Arrays.asList("ccc", "a", "bb");
// 自然排序 (字典序)
list.stream().sorted().forEach(System.out::println); // a, bb, ccc
// 定制排序 (按长度排序)
list.stream()
    .sorted((s1, s2) -> Integer.compare(s1.length(), s2.length()))
    .forEach(System.out::println); // a, bb, ccc

// distinct-去重：对流中的元素进行去重操作
// 作用：去除重复元素。
// 原理：依赖元素的 hashCode() 和 equals() 方法（如果是自定义对象，则必须重写这两个方法，否则去重无效）。
// 场景：对用户列表按姓名去重、对商品列表按商品ID去重等。
List<String> list = Arrays.asList("AA", "BB", "CC", "BB", "CC", "AA", "AA");
long l= list.stream().distinct().count();
String output = list.stream().distinct().collect(collectors.joining(","));
System.out.println(output); // AA,BB,CC

// limit-截取：对流中的元素进行截取操作，只保留前 n 个元素。
// 作用 ：对流中的元素进行截取操作，只保留前 n 个元素。
// 场景 ：分页查询、取前几条数据等。
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.stream()
    .limit(3)
    .forEach(System.out::println); // 输出 1, 2, 3

// skip-跳过：对流中的元素进行跳过操作，跳过前 n 个元素。
// 作用 ：对流中的元素进行跳过操作，跳过前 n 个元素。
// 场景 ：分页查询、跳过前几条数据等。
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.stream()
    .skip(3)
    .forEach(System.out::println); // 输出 4, 5
```
## 终止操作
```java
// forEach-遍历：对流中的每个元素执行指定操作。
// 作用 ：对流中的每个元素执行指定操作。
// 场景 ：对集合中的每个元素进行打印、输出等操作。
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.stream()
    .forEach(System.out::println); // 输出 1, 2, 3, 4, 5

// count-统计：对流中的元素进行统计操作，返回元素的个数。
// 作用 ：对流中的元素进行统计操作，返回元素的个数。
// 场景 ：统计集合中的元素个数、筛选出符合条件的元素个数等。
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
long count = list.stream().count();
System.out.println(count); // 5

// collect-收集：对流中的元素进行收集操作，将流中的元素收集到一个集合或其他数据结构中。
// 作用 ：对流中的元素进行收集操作，将流中的元素收集到一个集合或其他数据结构中。
// 场景 ：将流中的元素收集到一个 List、Set 或 Map 中。
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Alice");
// 转 List
List<String> listResult = names.stream().collect(Collectors.toList());
// 转 Set (自动去重)
Set<String> setResult = names.stream().collect(Collectors.toSet());
// 转 Map (Key: 名字, Value: 名字长度)
// (k1, k2) -> k1 是当Key冲突时，取前一个值
Map<String, Integer> mapResult = names.stream()
    .distinct() // 先去重，防止toMap报错，或者使用mergeFunction
    .collect(Collectors.toMap(s -> s, s -> s.length()));
// 字符串拼接
String str = names.stream().collect(Collectors.joining(", ")); // "Alice, Bob, Charlie, Alice"
// 分组 (按名字长度分组)
Map<Integer, List<String>> groupMap = names.stream()
    .collect(Collectors.groupingBy(String::length));
// 结果：{3=[Bob], 5=[Alice, Alice], 7=[Charlie]}

// match-匹配：对流中的元素进行匹配操作，判断是否符合指定条件。
// 作用 ：对流中的元素进行匹配操作，判断是否符合指定条件。
// 场景 ：判断集合中是否存在符合条件的元素、判断集合是否全部符合条件等。
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
boolean anyMatch = list.stream().anyMatch(item -> item > 3);
System.out.println(anyMatch); // true
boolean allMatch = list.stream().allMatch(item -> item > 3);
System.out.println(allMatch); // false

// find-查找：对流中的元素进行查找操作，返回符合指定条件的第一个元素。
// 作用 ：对流中的元素进行查找操作，返回符合指定条件的第一个元素。
// 场景 ：查找集合中符合条件的第一个元素、查找集合中符合条件的任意元素等。
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
Optional<Integer> first = list.stream().filter(item -> item > 3).findFirst();
System.out.println(first.orElse(-1)); // 4

// reduce-规约：对流中的元素进行规约操作，将流中的元素合并成一个值。
// 作用：对流中的元素进行规约操作，将流中的元素合并成一个值。
// 场景：计算购物车总金额等。
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
// 求和：0是初始值，(a, b) -> a + b 是累加规则
Integer sum = numbers.stream().reduce(0, (a, b) -> a + b); 
// 或者使用方法引用：
Integer sum2 = numbers.stream().reduce(0, Integer::sum);
System.out.println(sum); // 15

// 求最大值
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```
## 并行流基础
> 并行流是 Java 8 为了利用多核处理器优势而设计的功能。它能把一个大任务拆分成多个小任务，在多个线程上同时执行，最后合并结果。

使用方式：将原本的 `stream()` 替换为 `parallelStream()` 或在 `stream()` 后调用 `parallel()` 方法。
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

// 获取并行流
list.parallelStream()
    .forEach(i -> System.out.println(Thread.currentThread().getName() + " : " + i));
// 输出结果是乱序的，且会看到不同的线程名称
```

需要注意的问题：
1. 线程安全问题：如果操作会修改共享变量，并行流会导致数据丢失或异常。解决方案：使用 `collect` 来收集结果，而不是在 `forEach` 里修改外部变量；或是使用线程安全的集合。
2. 拆装箱问题：如果处理的是 Integer 等包装类，拆箱装箱的开销可能抵消并行的优势。解决方案：使用原始类型流（如 `IntStream`、`LongStream` 等）来避免拆箱装箱。
3. 数据量问题：如果数据量非常小，并行流可能会得不偿失，因为线程切换的开销大于并行执行的收益。解决方案：根据实际情况（数据量达到万级以上，且每个元素处理耗时较长（如复杂计算）时），才考虑选择是否使用并行流。

## 面试问题准备
Q：**Stream 的 `map` 和 `flatMap` 有什么区别？**
A：`map` 是"一对一"映射，它接收一个元素，输出一个新元素，结果流的结构保持不变。而 `flatMap` 是"一对多"映射（扁平化），它接收一个元素，输出一个 Stream ，最后将所有 Stream 合并成一个扁平的流。举例：场景处理 `List<String> words` ，用 `map` 获取每个单词长度；处理 `List<List<Order>>` ，用 `flatMap` 将所有订单合并成一个 `List<Order>` 。

Q：**Stream 操作是立即执行的吗？（如果不是立即执行，它的执行时机是？）**
A：不是。Stream 操作分为"中间操作"和"终止操作"。中间操作（如filter、map、flatMap等）是延迟执行（Lazy）的，只有当终止操作（如forEach、collect、reduce等）被调用时，流水线才会真正开始执行。这允许 JVM 对其进行优化，如短路操作（findFirst 找到一个就停止），避免不必要的计算。

Q：**并行流(Parallel Stream)一定比串行流快吗？**
A：不一定。并行流还存在线程切换、任务拆分或合并的开销。数据量较小时、数据结构不宜拆分时、涉及到的拆装箱操作较多时并行流的性能都会受到影响；只有在万级以上数据量且计算密集型场景下，并行流才有明显优势。

Q：**Stream 中的 reduce 和 collect 有什么区别？**
A：`reduce` （归约）通过重复组合流中元素生成新值（如求和、最大值、拼接字符串），它是不可变的归约。 `collect` （收集）主要用于将流中的元素汇总到一个容器中（如：List、Map、Set），它是可变的归约（Mutable Reduction），通常性能更好，因为直接在容器中添加元素，避免了频繁创建新对象。

Q：**如何对流中的元素进行去重？如果是自定义对象呢？**
A：对流中元素去重可以使用 `.distinct()` 方法。如果是自定义对象，必须正确重写 `equals()` 和 `hashCode()` 方法，确保在 `distinct` 中能正确判断是否为重复元素。

Q：**项目中如何避免 Stream 流的常见坑？**
A：用Optional处理findFirst或findAny结果（ifPresent或orElseThrow），过滤 null 元素，避免空指针；并行流操作共享变量使用原子类，不用普通的 int或long 类型；终止操作后重新创建流，避免重复使用已终止的流；小数据量不用并行流，有状态操作放在并行处理后。