# 安装及使用


### Maven 中央仓库搜索
* [https://mvnrepository.com/search?q=com.buession.canal](https://mvnrepository.com/search?q=com.buession.canal)
* [https://search.maven.org/search?q=g:com.buession.canal](https://search.maven.org/search?q=g:com.buession.canal)

### 手动编译
```shell
git clone https://github.com/buession/buession-canal
cd buession-canal/buession-canal-parent && mvn clean install
```

### Maven
```xml
<dependency>
    <groupId>com.buession.canal</groupId>
    <artifactId>buession-canal-xxx</artifactId>
    <version>x.x.x</version>
</dependency>
```

### Gradle
```gradle
compile group: 'com.buession.canal', name: 'buession-canal-xxx', version: 'x.x.x'
```

其中，artifactId 中的 xxx 表示对应的子模块；version 中的 x.x.x 代表版本号，根据需要使用特定版本，建议使用 maven 仓库中已构建好的最新版本[![Maven Central](https://img.shields.io/maven-central/v/com.buession.canal/buession-canal-core.svg)](https://search.maven.org/search?q=g:com.buession.canal)的包。