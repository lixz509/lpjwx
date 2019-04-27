#停止接入程序
cd weixin
mvn spring-boot:stop

#停止关注程序
cd ../subscribe
mvn spring-boot:stop

#停止取消关注程序
cd ../unsubscribe
mvn spring-boot:stop
cd ..