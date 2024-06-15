JAVA_PID=`sudo lsof -i :8080 -t`
if [ -z $JAVA_PID ]
  then echo "실행되고 있는 애플리케이션이 없습니다."
else
  sudo kill -9 $JAVA_PID
  echo "기존에 실행되고 있던 애플리케이션을 종료했습니다."
fi
