## yarn 配置

<!-- scheduler configuration, for multi-tasks run in queue, avoid mapreduce-run & pyspark ACCEPTED not run problem -->
    <property>
        <name>yarn.resourcemanager.scheduler.class</name>
        <value>org.apache.hadoop.yarn.server.resourcemanager.scheduler.fair.FairScheduler</value>
    </property>
    <property>
        <name>yarn.scheduler.fair.preemption</name>
        <value>true</value>
    </property>
<!-- 下面配置用来设置集群利用率的阀值， 默认值0.8f，最多可以抢占到集群所有资源的80% -->
    <property>
        <name>yarn.scheduler.fair.preemption.cluster-utilization-threshold</name>
        <value>1.0</value>
    </property>