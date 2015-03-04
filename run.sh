#!/bin/sh

# Copyright (c) 2015, Xiaomi.com. All rights reserved.
# Author:  Wu Zesheng <wuzesheng@xiaomi.com>
# Created: 2015-01-15
git pull
export JAVA_OPTS="-cp /home/yanghao3/app/slider/hadoop-conf:slider-service-server/target/lib/*:slider-service-server/target/SliderService.jar -Djava.security.krb5.conf=/etc/krb5-hadoop.conf"
#mvn clean package -DskipTests
java ${JAVA_OPTS} com.xiaomi.infra.slider.server.Server 20050 h_yanghao3@XIAOMI.HADOOP ~/h_yanghao3.keytab
