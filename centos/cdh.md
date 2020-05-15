
##
yum -y install psmisc net-tools lrzsz wget curl vim 


#enforce

seliunx关闭
# sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
确认是否修改成功
# grep SELINUX /etc/selinux/config
然后重启系统即可
# reboot


#firewalled
systemctl stop firewalld
systemctl disable firewalld

#离线安装

mv CDH-5.10.2-1.cdh5.10.2.p0.5-el7.parcel /opt/cloudera/parcel-repo/
mv CDH-5.10.2-1.cdh5.10.2.p0.5-el7.parcel.sha /opt/cloudera/parcel-repo/
mv manifest.json /opt/cloudera/parcel-repo/