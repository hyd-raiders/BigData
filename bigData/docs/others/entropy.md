#查看
cat /proc/sys/kernel/random/entropy_avail

#安装
yum install -y rng-tools

cat > /etc/sysconfig/rngd <<EOF
OPTIONS="-r /dev/urandom"
EOF

cat /etc/sysconfig/rngd

systemctl restart rngd
systemctl enable rngd

ps -e |grep rngd


#查看
cat /proc/sys/kernel/random/entropy_avail

