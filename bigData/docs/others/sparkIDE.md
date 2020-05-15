# windows环境下
## 基础环境
    jdk1.8安装 使用最新的8、并配置环境变量，过程略
    下载安装Scala 下载地址: http://www.scala-lang.org/download/2.11.6.html   注：默认安装选项会自动配置环境变量,安装路径不能有空格。
    
## ide
下载最新IntelliJ IDEA、直接安装
在idea中，打开Files->settings选择Plugins,输入scala查看： 确定scala插件已经安装

## 配置sbt镜像源
如图 others/sbt镜像配置.jpg, 修改lancher 为淘宝源 sbt-launch.jar


## sbt配置
 修改文件位置：

window:C:\Users\用户目录\.sbt\repositories

linux:~/.sbt/repositories

mac:呵呵呵

修改文件内容：

[repositories]
#local
public: http://maven.aliyun.com/nexus/content/groups/public/#这个maven
typesafe:http://dl.bintray.com/typesafe/ivy-releases/ , [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly#这个ivy
ivy-sbt-plugin:http://dl.bintray.com/sbt/sbt-plugin-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]#这个ivy
sonatype-oss-releases

sonatype-oss-snapshots

我试过window和linux首次都能在5分钟下载完相应“scala”和相应的“jar包”。