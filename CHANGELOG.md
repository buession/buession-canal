 Buession Canal Changelog
===========================


## [1.0.0](https://github.com/buession/buession-canal/releases/tag/v1.0.0) (2024-05-06)

### 🔨依赖升级

- [依赖库版本升级和安全漏洞修复](https://github.com/buession/buession-parent/releases/tag/v2.3.3)


### ⭐ 新特性

- **buession-canal-springboot：** 增加 canal 的 spring boot 支持


---


## [0.0.2](https://github.com/buession/buession-canal/releases/tag/v0.0.2) (2023-12-27)

### 🔨依赖升级

- [依赖库版本升级和安全漏洞修复](https://github.com/buession/buession-parent/releases/tag/v2.3.2)


### 🐞 Bug 修复

- **buession-canal-core：** 修复 buession-beans bean 转换导致的数据丢失的 BUG
- **buession-canal-core：** 修复 @CanalEventListener 仅指定 schema 或 table 服务映射方法的 BUG
- **buession-canal-core：** 修复无法获取 @CanalEventListener schema 和 table 的 BUG
- **buession-canal-core：** 修复线程池线程序号错误的 BUG
- **buession-canal-core：** 修复 Table 设置数据库名和表名错误的 BUG
- **buession-canal-client：** 修复多实例下，实例不执行的 BUG
- **buession-canal-spring：** 修复注解 CanalBinding 无法重复定义 destination 的 BUG


### ⏪ 优化

- **buession-canal-core：** 代码优化
- **buession-canal-spring：** 代码优化
- **buession-canal-spring：** 优化 CanalClientFactoryBean 多次调用 afterPropertiesSet 时，重复初始化 CanalClient


---


## [0.0.1](https://github.com/buession/buession-canal/releases/tag/v0.0.1) (2023-11-19)

### 🔨依赖升级

- [依赖库版本升级和安全漏洞修复](https://github.com/buession/buession-parent/releases/tag/v2.3.1)


### ⭐ 新特性

- **buession-canal-annotation：** canal 注解
- **buession-canal-core：** canal 核心库
- **buession-canal-client：** canal 客户端
- **buession-canal-spring：** canal spring 支持