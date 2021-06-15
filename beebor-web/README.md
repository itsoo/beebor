# beebor-web

MVC 框架

test 目录下有测试代码，仿照工程目录划分，可在测试类中启动 Application.testStart 方法

### 目前已实现：

1. 类 Controller 功能，提供能力的注解为 @Action
2. 全局异常处理
3. 拦截器实现（非 AOP 的实现，很简陋）
4. 集成了 IoC 模块，实现依赖注入
5. 响应数据结构支持 JSON


### 目前未实现：

1. 类 PathVariable 功能
2. 未实现视图解析器功能
