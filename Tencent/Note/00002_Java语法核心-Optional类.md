# 00002. Java语法核心-Optional类
## 基本概念
> Optional 是 Java8 引入的不可变容器类，核心作用是优雅解决 NPE（NullPointerException） 问题，它可以包含空值或非空值。
> 它内部维护一个value属性（泛型 T），仅能通过静态方法创建实例，且通过私有构造器避免外部实例化。

在[阿里巴巴开发手册](https://xiaoxue-images.oss-cn-shenzhen.aliyuncs.com/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E8%A7%84%E8%8C%83%EF%BC%88%E5%B5%A9%E5%B1%B1%E7%89%88%EF%BC%89.pdf)（Page31,11.）这样写到：
```txt
【推荐】防止 NPE，是程序员的基本修养...
...
正例：使用 JDK8 的 Optional 类来防止 NPE 问题。
```

下面来通过分析源码拆解 Optional 的工作原理。
```java
// 假设有一个 Zoo 类，里面有个属性 Dog，需求要获取 Dog 的 age
class Zoo {
   private Dog dog;
}
class Dog {
   private int age;
}

// 传统解决 NPE 问题
int getDogAge(Zoo zoo) {
    if (zoo != null) {
        Dog dog = zoo.getDog();
        if (dog != null) {
            return dog.getAge();
        }
    }
    return 0;
}

// 如果使用 Optional 实现，只需要写成：
int getDogAgeWithOptional(Zoo zoo) {
    return Optional.ofNullable(zoo)
            .map(Zoo::getDog)
            .map(Dog::getAge)
            .orElse(0);
}
```

Optional 类的核心成员（JDK8）:

```java
public final class Optional<T> {
    // 空值单例（全局唯一）
    private static final Optional<?> EMPTY = new Optional<>();
    // 存储实际值（可为null）
    private final T value;

    // 私有构造器：空值构造
    private Optional() {
        this.value = null;
    }
    // 私有构造器：非空值构造
    private Optional(T value) {
        this.value = Objects.requireNonNull(value);
    }
    // 工具类：空值检查（底层依赖）
    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
}
```

## 创建
Optional 的创建仅支持 3 种静态方法，核心区别是对null的处理逻辑：
- `empty()`：创建空 Optional 实例（单例）
```java
// empty()源码
public static<T> Optional<T> empty() {
    @SuppressWarnings("unchecked")
    Optional<T> t = (Optional<T>) EMPTY;
    return t;
}
// 返回全局唯一的EMPTY单例（value 为 null），避免频繁创建空实例，节省内存；
// 使用场景：明确值为 null 时（如方法返回空结果）
```
- `of(T value)`：创建非空 Optional 实例（强制非 null 值）
```java
// of(T value)源码
public static <T> Optional<T> of(T value) {
    return new Optional<>(value); // 调用私有构造器，依赖Objects.requireNonNull检查
}
// 层调用 Objects.requireNonNull ，若 value 为 null 则直接抛NullPointerException；
// 使用场景：明确值为非 null 时（如方法参数）
```

- `ofNullable(T value)`：创建可空值的 Optional 实例
```java
// ofNullable(T value)源码
public static <T> Optional<T> ofNullable(T value) {
    return value == null ? empty() : of(value);
}
// 兼容 null 值，若 value 为 null 则返回 EMPTY，否则调用 of() 方法创建实例
// 使用场景：不确定值是否为 null 时（如方法入参、数据库查询结果）
```

示例：
```java
// 空实例
Optional<Zoo> emptyZoo = Optional.empty();
// 非空（若zoo为null则抛NPE）
Optional<Zoo> nonNullZoo = Optional.of(new Zoo());
// 兼容null（推荐）
Optional<Zoo> nullableZoo = Optional.ofNullable(zoo);
```

## 安全取值
核心用于解决值为 null 时返回默认值或抛异常的问题，区别在于懒加载和异常类型。
- `orElse(T other)`：非空返回值，空返回默认值（立即加载）
```java
// orElse(T other)源码
public T orElse(T other) {
    return value != null ? value : other;
}
// 立即加载：无论 value 是否为 null，other都会被提前实例化（即使不需要）
// 缺点：如果other是耗时对象（如 new ArrayList ()），会造成性能浪费。
// 使用场景：默认值计算成本低时（如数据库查询可为空的固定数据默认值）
```

- `orElseGet(Supplier<? extends T> other)`：非空返回值，空返回默认值（懒加载默认值）
```java
// orElseGet(Supplier<? extends T> other)源码
public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
}
// 懒加载：只有在 value 为 null 时，才会调用 other.get() 方法实例化默认值。
// 优点：避免了立即实例化默认值的性能浪费，性能优于 orElse。
// 使用场景：默认值计算成本高时（如创建新对象、数据库查询）
```

- `orElseThrow(Supplier<? extends X> exceptionSupplier)`：非空返回值，空抛异常
```java
// orElseThrow(Supplier<? extends X> exceptionSupplier)源码
public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (value != null) {
        return value;
    } else {
        throw exceptionSupplier.get();
    }
}
// value 为 null 时，抛exceptionSupplier生成的自定义异常（替代默认 NPE）
// JDK10 + 新增无参重载orElseThrow()，默认会抛NoSuchElementException
// 使用场景：明确值可能为 null （如方法返回空结果）且需要抛指定异常提示时。
```

示例：
```java
// orElse：空返回0（提前创建0）
int age1 = Optional.ofNullable(zoo).map(Zoo::getDog).map(Dog::getAge).orElse(0);
// orElseGet：空时调用supplier（懒加载，仅空时执行）
int age2 = Optional.ofNullable(zoo).map(Zoo::getDog).map(Dog::getAge).orElseGet(() -> 0);
// orElseThrow：空时抛自定义异常
int age3 = Optional.ofNullable(zoo)
    .map(Zoo::getDog)
    .map(Dog::getAge)
    .orElseThrow(() -> new IllegalArgumentException("Zoo/Dog为空"));
```

## 判空
核心用于解决判断值是否存在，并执行逻辑的问题。
- `isPresent()`：判断值是否存在（非 null）
```java
// isPresent()源码
public boolean isPresent() {
    return value != null;
}
// 简单返回value != null的布尔结果，等价于传统if (obj != null)；
// 使用场景：简单判断值是否存在，无需后续逻辑处理时。
// 注意：单独使用isPresent()+get()会退化为传统判空，失去 Optional 意义，如：
if (optionalZoo.isPresent()) { // 与传统的 if (zoo != null) 无区别
    return optionalZoo.get().getDog().getAge();
}
```

- `ifPresent(Consumer<? super T> consumer)`：非空时执行 consumer 操作（无返回值）
```java
// ifPresent(Consumer<? super T> consumer)源码
public void ifPresent(Consumer<? super T> consumer) {
    if (value != null) {
        consumer.accept(value);
    }
}
// 非空时调用 consumer.accept(value) 执行逻辑，空时不执行。
// JDK9 + 新增 ifPresentOrElse ，支持值为空时执行兜底逻辑。
// 使用场景：值存在时需要执行特定操作（如打印、修改等），无需返回值。
```

使用示例：
```java
// 值存在时打印年龄，空则无操作
Optional.ofNullable(zoo)
    .map(Zoo::getDog)
    .map(Dog::getAge)
    .ifPresent(age -> System.out.println("狗的年龄：" + age));

// JDK9+：空时打印提示
Optional.ofNullable(zoo)
    .map(Zoo::getDog)
    .map(Dog::getAge)
    .ifPresentOrElse(
        age -> System.out.println("狗的年龄：" + age),
        () -> System.out.println("Zoo/Dog为空")
    );
```

## 映射
核心用于解决嵌套对象的链式取值问题，避免多层 if(obj != null) 。

- `map(Function<? super T, ? extends U> mapper)`：非空时映射值转换+自动包装（有返回值）
```java
// map(Function<? super T, ? extends U> mapper)源码
public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper); // 确保mapper非空
    if (!isPresent())
        return empty(); // 空值直接返回EMPTY
    else {
        return Optional.ofNullable(mapper.apply(value)); // 转换后包装为Optional
    }
}
// 空时直接返回EMPTY；非空时执行mapper.apply(value)转换值，再通过ofNullable包装为新的 Optional（自动将转换后的结果包装为 Optional，无需手动处理 null）。
// 使用场景：需要对非空值进行转换或映射操作时。
```

- `flatMap(Function<? super T, Optional<U>> mapper)`：非空时映射值转换+自定义包装的映射（有返回值）
```java
// flatMap(Function<? super T, Optional<U>> mapper)源码
public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<U>> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent())
        return empty(); // 空值返回EMPTY
    else {
        return Objects.requireNonNull(mapper.apply(value)); // 直接返回mapper的Optional结果
    }
}
// 空时直接返回EMPTY；非空时执行mapper.apply(value)转换值，直接返回 Optional（不会自动包装为 Optional）。
// 使用场景：需要对非空值进行转换或映射操作，且转换结果已经是 Optional 类型时（避免嵌套Optional<Optional>）。
```

使用示例：
```java
// map：链式取值（自动包装）
Optional.ofNullable(zoo)
    .map(Zoo::getDog) // Zoo→Dog，返回Optional<Dog>
    .map(Dog::getAge) // Dog→int，返回Optional<Integer>
    .orElse(0);

// flatMap：自定义Optional返回（避免嵌套）
// 假设Dog类有方法：Optional<Integer> getOptionalAge()
Optional.ofNullable(zoo)
    .map(Zoo::getDog) // Optional<Dog>
    .flatMap(Dog::getOptionalAge) // 直接返回Optional<Integer>，而非Optional<Optional<Integer>>
    .orElse(0);
```

## 过滤
核心用于解决值存在时，按条件筛选的问题。
- `filter(Predicate<? super T> predicate)`：非空时按条件筛选（有返回值）
```java
// filter(Predicate<? super T> predicate)源码
public Optional<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate); // 确保predicate非空
    if (!isPresent())
        return this; // 空值直接返回自身（EMPTY）
    else
        return predicate.test(value) ? this : empty(); // 满足条件返回自身，否则返回EMPTY
}
// 空时直接返回当前Optional；非空时执行 predicate.test(value) 判断，符合条件返回当前Optional，否则返回EMPTY。
// 使用场景：需要对非空值进行条件筛选时。
// 注意：过滤失败不会抛异常，仅返回空 Optional。
```

使用示例：
```java
// 筛选出年龄大于等于10的狗
Optional.ofNullable(zoo)
    .map(Zoo::getDog) // Optional<Dog>
    .filter(dog -> dog.getAge() >= 10) // 年龄>=10时返回当前Optional，否则返回EMPTY
    .map(Dog::getAge) // Optional<Integer>
    .orElse(0);
```

## 面试问题准备
Q：**为什么要使用 Optional？它解决了什么问题？**
A：Optional 类主要用于解决空指针异常（也就是我们常说的NPE - NullPointerException）问题，并将传统显式的null检查转化为显式的类型定义，让代码更简洁、可读性更高。同时，它通过封装空值处理逻辑（如默认值、自定义异常），规范了空值处理的方式，减少因手动判空遗漏导致的 NPE，可以提升代码健壮性。

Q：**Optional 的常用方法有哪些？它们有什么区别？**
A：Optional 常用的创建实例方法有empty()、of()、ofNullable()；取值方法有orElse()、orElseGet()、orElseThrow()；映射方法有map()、flatMap()；过滤方法有filter()。对于创建实例的三种方法，empty()返回空的Optional实例，of()要求值非空，ofNullable()可以接受null值；对于取值方法，orElse()和orElseGet()都可以返回默认值，区别在于orElse()立即加载默认值，而orElseGet()在值为空时才会执行 Supplier 函数，orElseThrow()用于在值为空时抛出自定义异常；对于映射方法，map()和flatMap()都可以对非空值进行转换操作，区别在于map()返回转换后的 Optional，而flatMap()返回的是转换函数直接返回的 Optional（用于避免嵌套Optional）；

Q：**Optional 的 orElse 和 orElseGet 有什么区别？分别适用于什么场景？**
A：orElse 和 oeElseGet 的核心区别是默认的加载时机。orElse是立即加载默认值，无论值是否为空，other都会提前实例化，常用于默认值计算成本低，比如返回固定数值 0、空字符串""的场景。而orElseGet是在值为空时才会执行 Supplier 函数，只有在需要时才会加载默认值。orElse适用于默认值计算成本低的场景，而orElseGet适用于默认值计算成本高的场景（如数据库复杂结构体数据查询拼接、网络请求等）。

Q：**如何避免 Optional 的使用陷阱或滥用？**
A：避免在实体类（POJO/DTO）字段中使用 Optional，Optional 未实现 Serializable，会导致序列化问题；避免用 Optional 作为方法参数，因为这样增加了调用者负担；避免在 ifPresent 中处理复杂的业务逻辑，应使用if结构进行判断和处理。

Q：**Optional 可以被序列化吗？适合作为类的成员变量吗？**
A：Optional 不可以被序列化，Optional 未实现 Serializable 接口，其设计初衷是作为 方法返回值 或 局部变量 处理空值，而非数据载体。同时，Optional 也不适用于作为类的成员变量，一方面序列化会失败，另一方面 Optional 是容器类，会增加成员变量的复杂度；若需表示成员变量可为空，直接用 null 并在取值时通过 ofNullable 处理更合理。

Q：**Optional 的 empty() 方法为什么返回单例？这样设计的好处是什么？**
empty() 返回全局唯一的 EMPTY 静态常量而非每次创建新实例的设计好处是可以避免频繁创建空 Optional 实例，减少内存占用和 GC 开销，符合 “享元模式” 思想，能够提升性能。

Q：**如何利用 Optional 优雅地处理 null 值？**
A：Optional.ofNullable(user).map(User::getAddress).map(Address::getCity).orElse("Unknown")