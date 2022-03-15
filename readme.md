# APP 启动优化 AndroidStartup 框架搭建

## 初步实现任务的管理

### 任务管理

- 依赖管理

    - 创建顶层任务接口
        1. 提供创建的方法
        2. 提供 当前任务依赖的集合
        3. 提供当前任务入度方法
    - 默认实现顶层接口
- 任务排序

  排序的时候 需要借助几张表

    - 入度表

      当前任务执行直线所依赖的所有任务

      Map<Class<? extends Startup>, Integer> inDegreeMap = new HashMap<>();

    - 0度表

      任务执行的队列 所有待执行的任务 都会进出这里

      Deque<Class<? extends Startup>> zeroDeque = new ArrayDeque<>();

    - 原始任务表

      记录所有任务 为排序前的任务

      Map<Class<? extends Startup>, Startup<?>> startupMap = new HashMap<>();

    - 当前任务依赖表

      记录当前任务执行之前 所依赖的所有任务

      Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap = new
      HashMap<>();

## 升级1

### 缓存初始化结果

    第一步可以实现任务的按需执行 但是每一步执行的结果未知
    本次弥补这个劣势 实现任务的执行结果

    - 创建结果类
        每一个任务执行的结果数据不一定一直 所以使用泛型保存
        在这里没有对结果做任何处理 只是原样保存
    - 创建一个缓存管理类
        在这里 保存了一份所有任务执行的结果
        因为结果只需一份 所以使用全局单例
        考虑到线程的安全：
                     1. 单例使用双重校验 加上volatile
                     2. 缓存使用ConcurrentHashMap

### 封装AndroidStartup

    第一步 直接使用AndroidStartup 可以实现先过但是作为一个框架 显然少了一层封装

    - 创建一个Startup管理类
        此处的封装只是 隐藏了Startup的执行过程
        封装提供一个构造 传入上下文和任务列表
        提供一个star方法 
        提供一个内部类 Builder 可以使用build 构建实例
        
    