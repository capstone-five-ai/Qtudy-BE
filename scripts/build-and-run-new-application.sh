PROJECT_PATH=/home/ubuntu/Qtudy-BE
BUILD_PATH=$PROJECT_PATH/build/libs
BUILD_JAR=$BUILD_PATH/Qtudy-server-0.0.1-SNAPSHOT.jar

nohup java -jar $BUILD_JAR --spring.profiles.active=stage /dev/null 2> /dev/null < /dev/null &
