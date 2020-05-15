
## 修改ip配置
vim /etc/sysconfig/network-scripts/ifcfg-eth0

## 规则
vim /etc/udev/rules.d/70-persistent-net.rules

## 重启网络
service network restart

## 重启机器
reboot