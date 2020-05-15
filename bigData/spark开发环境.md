Scala 下载地址: http://www.scala-lang.org/download/2.11.6.html   注：默认安装选项会自动配置环境变量,安装路径不能有空格。

　　IntelliJ IDEA 下载地址：https://www.jetbrains.com/idea/

由于这里下载的ideaIU-15.0.2.exe，已经包含有Scala插件，如果不包含需要下载。查看是否已有scala插件可以新建项目，打开Files->settings选择Plugins,输入scala查看：




配置sbt国内源

查看 sb镜像配置.jpg

本地IDEA调试
1在本地配置HADOOP_HOME环境变量（HADOOP_HOME和PATH）
2.修改本地JAVA_HOME的值（如果值中含有空格，例如C:\Program Files\Java\jdk1.8.0_172 则需要改为C:\PROGRA~1\Java\jdk1.8.0_172）
3.下载hadoop-common-2.2.0-bin-master.zip，解压，将其中的hadoop.cmd，hadoop.dll，winutils.exe 拷贝至本地hadoop\bin目录下
4.重启电脑.
