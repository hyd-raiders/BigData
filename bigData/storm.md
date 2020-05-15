#机器
192.168.12.247  主节点（nimbus）
192.168.12.246  从节点（supervisor）
192.168.12.245  从节点（supervisor）
# 安装jdk，并配置环境变量
java -version
# 安装zookeeper集群并启动集群
/usr/zookeeper/bin/zkServer.sh start (每台都需要)
# 安装python 
python -v
# 下载并解压storm ，这里我选择1.2.1  （apache-storm-1.2.1.tar.gz）
tar -xzvf apache-storm-1.2.1.tar.gz -C /usr/
cd /usr/
mv apache-storm-1.2.1 storm

# 配置环境变量
vim /etc/profile

export STORM_HOME=/usr/storm
export PATH=$STORM_HOME/bin:$PATH

source /etc/profile

# 以下操作在主节点进行
# 配置storm (storm.yaml)
vim /usr/storm/conf/storm.yaml
###配置Zookeeper地址（配置Zookeeper的主机名，注意: 如果Zookeeper集群使用的不是默认端口，那么还需要配置storm.zookeeper.port)
storm.zookeeper.servers: 
    - "192.168.12.247"
    - "192.168.12.246"
    - "192.168.12.245"
storm.local.dir: "/usr/storm/status"
nimbus.seeds: ["192.168.12.247"]  
supervisor.slots.ports: 
    - 6700
    - 6701
    - 6702
    - 6703
drpc.servers:
        - "192.168.12.247"   #配置drpc服务节点
###Strom Web UI 端口（默认8080），如果该端口被spark占用，则打开修改为其他值
#ui.port: 8080 

##注意:以上配置，凡是有冒号的地方，冒号后都要有个空格。

# 发送内容到2台从几点
scp -r /usr/storm root@192.168.12.246:/usr/
scp -r /usr/storm root@192.168.12.245:/usr/

# 启动storm
cd /usr/storm/
# 主节点启动
./bin/storm ui >/dev/null 2>&1 &
./bin/storm nimbus >/dev/null 2>&1 &
./bin/storm drpc >/dev/null 2>&1 &

# 从节点启动
./bin/storm supervisor >/dev/null 2>&1 &

# 验证 ，输入命令jps，查看有以下4个则表示启动完成
nimbus、core、QuorumPeerMain、Supervisor

# 登录web ui
http://192.168.12.247:8080 进行查看访问

# 提交远程strom执行
#将项目打包成jar文件并上传至storm的根目录,执行
./bin/storm jar startDemo.jar com.dcjet.storm.startDemo.App  arg1 arg2
