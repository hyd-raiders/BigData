

# ntp安装@所有机器
rpm -qa | grep ntp
yum -y install ntp
ntpdate cn.pool.ntp.org

# ntp配置@ntp服务器（master，集群中挑选一台）

vim /etc/ntp.conf
`
注释掉以下内容
#server 0.centos.pool.ntp.org
#server 1.centos.pool.ntp.org
#server 2.centos.pool.ntp.org
#server 3.centos.pool.ntp.org iburst
把以下内容注释去掉，如果内容不存在 手动添加
server  127.127.1.0     # local clock
fudge   127.127.1.0 stratum 10
去掉以下内容的#  192.168.30.0 修改自己的网段
restrict 192.168.30.0 mask 255.255.255.0 nomodify notrap
保存退出
`
service ntpd start
chkconfig ntpd on


# ntp配置@ntp slaves

## 同步时间
ntpdate hadoop1(ntpMaster)
#制定计划任务 周期性同步时间 二三台服务器
## 写定时任务
crontab -e
`
*/10 * * * * /usr/sbin/ntpdate hadoop1
`
分 时 日 月 星期

重启 ：
service crond restart


service ntpd start
chkconfig ntpd on