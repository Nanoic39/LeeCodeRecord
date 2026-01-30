# 00000. Java语法核心-Lambda表达式
## 基础知识
### 语法格式
> Lambda表达式是语法糖，因为它提供了一种更简洁、更紧凑的语法来并表示函数式接口的实例，使开发者能够专注于业务逻辑而非冗余的模块代码，但不是匿名内部类的语法糖。
> Lambda运行时是通过几个JVM底层的api实现的，不是通过动态代理实现的。
> Lambda是Java8最重要的新特性，是继泛型(Generics)和注解(Annotation)以来最大的变化。

语法格式：
```java
// ()：参数列表（可省略部分写法）
// -> Lambda 操作符（箭头），分隔参数和代码
// {}：代码块（可省略部分写法）
(参数列表) -> { 代码块语句 }

// 完整写法：
(String s) -> { System.out.println(s); }

// 省略参数类型：
(s) -> { System.out.println(s); }

// 单参数省略参数括号：
s -> { System.out.println(s); }

// 单语句省略代码块花括号：
(s) -> System.out.println(s)

// 单语句省略返回return：（(a, b) -> { return a + b; }）
(a, b) -> a + b
```

为什么Lambda不是匿名内部类的语法糖？举个例子：
```java | 遍历列表
// 一个遍历列表的Lambda表达式
public static void main(String... args) {
    List<String> strList = ImmutableList.of("Nanoic", "39339", "NANO1C");
    strList.forEach( s -> { System.out.println(s); } );
}
```

如果这是一个匿名内部类的语法糖，那么编译之后会生成两个class文件（一个是包含外部类的文件 Name.class，另一个是包含内部类的文件Name$1.class），但是含有Lambda表达式的类编译之后只有一个class文件（Name.class）。

```java | 
// 反编译后代码为：
public static /* varargs */ void main(String ... args) {
    ImmutableList strList = ImmutableList.of((Object)"Nanoic", (Object)"39339", (Object)"NANO1C");
    strList.forEach((Consumer<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$main$0(java.lang.String ), (Ljava/lang/String;)V)());
}

private static /* synthetic */ void lambda$main$0(String s) {
    System.out.println(s);
}
```

在 `forEach` 方法中，实际上是调用了 `LambdaMetafactory.metafactory` 方法，这个方法是JVM底层的一个方法，用于创建Lambda表达式的实例。在这个方法中，第一个参数是类加载器，第二个参数是方法名，第三个参数是方法描述符，第四个参数是Lambda表达式的参数类型，第五个参数是Lambda表达式的实现方法，第六个参数是Lambda表达式的返回类型。
第五个参数其实是调用了一个 `lambda$main$0` 方法进行了输出。

### 函数式接口（Consumer/Supplier/Function/Predicate）
> 函数式接口的定义：一个有且仅有一个抽象方法，但是可以有多个非抽象方法（默认方法、静态方法、私有方法）的接口。
> 函数式接口可选 `@FunctionalInterface` 注解，用于编译期间校验是否符合函数式接口规范，如果不写注解只要满足规则也属于函数式接口
> 函数式接口可以隐式转换为lambda表达式。

**Consumer：消费型，有入参无返回**
Consumer 接口表示要对单个输入参数执行的操作。常用于遍历集合、打印数据等场景。
```java | 抽象方法及示例
// 抽象方法：
void accept(T t);

// 示例：
List<String> list = Arrays.asList("a","b"); list.forEach(s -> System.out.println(s));
```

**Supplier：供给型，无入参有返回**
Supplier 接口产生给定泛型类型的结果。常用于延迟加载、惰性计算、生成数据或工厂模式等场景。
```java | 抽象方法及示例
// 抽象方法：
T get();

// 示例：
Supplier<Integer> s = () -> new Random().nextInt(100); 
System.out.println(s.get());
```

**Function：函数型，有入参有返回**
Function 接口表示接受一个参数并产生结果的函数。常用于数据/类型转换、映射等场景。
```java | 抽象方法及示例
// 抽象方法：
R apply(T t);

// 示例：
Function<User, UserVO> f = user -> new UserVO(user.getId(), user.getName());
```

**Predicate：断言型，有入参返回boolean**
Predicate 接口表示一个参数的布尔值函数。常用于过滤集合、参数校验、判断条件等场景。
```java | 抽象方法及示例
// 抽象方法：
boolean test(T t);

// 示例：
List<Integer> nums = Arrays.asList(1,2,3); 
nums.stream().filter(n -> n > 2).forEach(System.out::println);
```

扩展：
- BiConsumer\<T,U>：双参数消费型（`void accept(T t, U u)`），如`Map.forEach((k,v) -> System.out.println(k+":"+v))`；
- BiFunction\<T,U,R>：双参数函数型（`R apply(T t, U u)`），如`(a,b) -> a + "-" + b`；
- BiPredicate\<T,U>：双参数判断型（`boolean test(T t, U u)`），如`(a,b) -> a.equals(b)`；
- UnaryOperator\<T>：单参数一元操作（继承 Function，入参返回同类型），如 `num -> num * 2`；
- BinaryOperator\<T>：双参数二元操作（继承BiFunction，入参返回同类型），如 `(a,b) -> a + b`；

```java | 自定义函数式接口
// 自定义函数式接口
// 自定义函数式接口（校验手机号）
@FunctionalInterface
public interface PhoneValidator {
    boolean validate(String phone);
    
    // 允许添加默认方法
    default String getRegex() {
        return "^1[3-9]\\d{9}$";
    }
}

// 使用自定义函数式接口
PhoneValidator validator = phone -> phone.matches(validator.getRegex());
System.out.println(validator.validate("13900004514")); // true
```

### 方法引用/构造器引用
> 方法引用通过方法的名字来指向一个方法，它可以使语言的构造更紧凑简洁，减少冗余代码。
> 方法引用是 Lambda 表达式的「进一步简化语法糖」，仅当 Lambda 代码块中只有「调用一个已有方法」时可用。

#### 方法引用四种类型：
1. 静态方法引用：类名::静态方法
```java
// 例如：
Lambda：(num) -> Integer.parseInt(num)
方法引用：Integer::parseInt
```

2. 对象实例方法引用：对象::实例方法
```java
// 例如：
Lambda：str -> System.out.println(str)
方法引用：System.out::println
```

3. 类实例方法引用：类名::实例方法
```java
// 例如：
Lambda：(str1, str2) -> str1.equals(str2)
方法引用：String::equals
```

4. 数组引用：类型[]::new
```java
// 例如：
Lambda：() -> new int[10]
方法引用：int[]::new
```

#### 构造器引用：
构造器引用：类名::new（根据函数式接口的抽象方法参数，匹配对应参数的构造器）
```java
// 无参构造器（Supplier）
Supplier<User> s = () -> new User();
// User::new;

// 单参构造器（Function）
Function<String, User> f = name -> new User(name);
// User::new;

// 多参构造器（BiFunction）
BiFunction<String, Integer, User> bf = (name, age) -> new User(name, age);
// User::new;
```

### 变量捕获规则
> 局部变量捕获：Lambda 中引用的外部局部变量，必须是final或「有效 final」（即变量声明后未重新赋值，即使没写 final 关键字）
> 成员变量 / 静态变量捕获：无 final 限制，可自由修改；

规则底层原因：
- 局部变量存储在 栈内存 ，线程私有；而Lambda表达式可能在另一个线程中执行，栈内存会随方法执行结束而释放，若允许修改会导致数据不一致；
- 成员变量 / 静态变量存储在「堆内存」，线程共享；Lambda表达式可以访问并修改这些变量，所以无需 final 限制。

使用说明：
在实际使用中应该避免在Lambda表达式中捕获大量局部变量，会使代码可读性变差。
如果需要修改外部数据，优先使用成员变量 / 原子类（AtomicInteger），而非局部变量。
如果一定要捕获局部变量，应该使用显式的 final 关键字或有效 final 变量。

局部变量捕获示例：
```java
// 错误示例
int num = 10;
Runnable r = () -> System.out.println(num);
num = 20; // 编译报错：局部变量num必须是final或有效final

// 正确示例-1
final int num = 10; // 显式final
Runnable r = () -> System.out.println(num);

// 正确示例-2
int num2 = 20; // 有效final（未重新赋值）
Runnable r2 = () -> System.out.println(num2);
```

成员变量 / 静态变量捕获示例：
```java
public class Test {
    private int memberNum = 10; // 成员变量
    private static int staticNum = 20; // 静态变量
    
    public void test() {
        Runnable r = () -> {
            memberNum = 30;
            staticNum = 40;
            System.out.println(memberNum + "," + staticNum);
        };
        r.run(); // 输出30,40
    }
}
```
## 面试问题准备
Q：**Lambda 表达式的核心语法是什么？有哪些简化规则？**
A：Lambda 表达式的核心语法是：`(参数列表) -> 表达式或代码块`，简化规则有以下四种：
1. 参数的类型可以省略，编译器会根据上下文自动推断。
2. 如果只有一个参数，可以省略参数的括号。
3. 如果只有一个语句，可以省略语句的花括号。
4. 如果语句是return语句，可以省略 return 关键字。

Q：**什么是函数式接口？@FunctionalInterface注解的作用是什么？**
A：函数式接口是一个一个有且仅有一个抽象方法，但是可以有多个非抽象方法（默认方法、静态方法、私有方法）的接口。@FunctionalInterface 注解用于在编译期间检查接口是否符合函数式接口的定义，若不符合会报错。仅用于标记函数式接口，方便开发者进行代码规范和错误检查，即使不进行标注，只要满足规则的也会属于函数式接口。

Q：**请分别说明 Consumer、Supplier、Function、Predicate 的作用和典型应用场景。**
A：Consumer 接口是消费型接口，用于接收一个参数并对其进行消费，有入参无返回值，抽象方法为 `void accept (T t)` ，常应用在对集合中的元素进行操作，如集合遍历场景中；Supplier 接口是供给型接口，用于产生一个指定类型的结果，无参数有返回值，抽象方法为 `T get()` ，常应用在延迟加载、惰性计算、生成数据或工厂模式场景中；Function 接口是函数型接口，用于接收一个参数并返回一个结果，抽象方法为 `R apply(T t);` ，常用于数据/类型转换、映射场景中；Predicate 接口是断言型接口，表示一个参数的布尔值函数，有入参返回boolean，抽象方法为 `Boolean test(T t);` ，常用于过滤集合、参数校验、判断条件等场景。

Q：**Lambda 表达式和匿名内部类的区别？**
A：Lambda 表达式和匿名内部类都是用于实现函数式接口的方式，但是它们有以下区别：
1. 功能(类型)限制：Lambda 只能实现函数式接口，匿名内部类可实现任意接口 / 继承任意类；
2. 字节码实现：Lambda 表达式主要利用 invokedynamic 指令，在运行时动态生成对应的代理类或实现类，不直接生成物理 class 文件，从而具有更轻量级的内存开销和更高的效率；而匿名内部类则会在编译时生成一个对应的 .class 文件。

Q：**方法引用有哪几种类型？请举例说明每种类型的写法？**
A：方法引用有四种类型，分别为：静态方法引用、对象实例方法引用、类实例方法引用、数组引用。
1. 静态方法引用：类名::静态方法，例如 `Integer::parseInt`
2. 对象实例方法引用：对象::实例方法，例如 `System.out::println`
3. 类实例方法引用：类名::实例方法，例如 `String::equals`
4. 数组引用：类型[]::new，例如 `int[]::new`

Q：**Lambda 表达式中引用的局部变量为什么必须是 final 或有效 final？**
A：这是因为局部变量储存在栈内存中，而 Lambda 表达式可能在另一个线程中执行，栈内存会随方法执行结束而释放，若允许修改会导致数据不一致。因此，为了线程安全和避免数据一致性问题，Lambda 表达式中引用的局部变量必须是 final 或有效 final（即变量声明后未重新赋值，即使没写 final 关键字）。

Q：**自定义函数式接口时需要注意什么？如何保证其符合规范？**
A：首先，自定义函数式接口需要保证只有一个抽象方法，否则编译会报错（可以使用 @FunctionalInterface 注解进行检查）；其次，抽象方法的参数和返回值类型要贴合业务场景，避免与其他函数式接口产生混淆；最后，可以添加一些默认方法或静态方法，提高接口的可扩展性。

Q：**能否在 Lambda 表达式中修改外部的成员变量？为什么？**
A：可以在 Lambda 表达式中修改外部的成员变量，因为成员变量存储在堆内存中，线程共享且可见，因此无 final 限制。但是为了避免多线程数据一致性问题，应该尽量避免在 Lambda 表达式中修改外部的成员变量，而是通过参数传递或返回值来进行数据交互。