# Kotlin 模板

> 一套简单的 Kotlin DSL Gradle 项目

## 快速开始

1. 从 `https://github.com/OpenEdgn/GradleKotlinTemplate` 克隆项目
2. 修改[LICENSE](./LICENSE) 为合适的分发协议
3. 重建 [README.md](./README.md) 文件
4. 修改 [settings.gradle.kts](./settings.gradle.kts) 中 `project.name` 为合适的名称
5. 在项目根目录下执行 `./gradlew build -x test` 
6. 在项目根目录下执行 `./gradlew test`
7. 删除 [core](./core) 下的模板代码

## 更新日志

- **20201125**
    - 更改项目名称为 `GradleKotlinTemplate`
    - 添加 `maven-publish` 插件，现在可以使用 `gradlew publishToMavenLocal` 了
    - 修改 `Kotlin` 插件编译生成目录 [build.gradle.kts](./core/build.gradle.kts#L9) 
    
- **20201120**
    - 更新 `kotlin` 为 `1.4.10`
    - 更新 `gradle` 为 `6.5.1`
    - 删除过时的 `buildSrc` 方案

## LICENSE

请看 [LICENSE](./LICENSE) 来了解此模板的 LICENSE