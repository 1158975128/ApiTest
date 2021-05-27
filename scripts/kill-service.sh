export BUILD_ID=DONTKILLME
#项目根目录
WORKSPACE=/target
#要启动的jar程序
JAR_NAME=fuliye.jar
# 外置config路径
CONFIG_FILES=config

PID=`ps -ef | grep ${JAR_NAME} | grep -v grep | grep -v startup | awk '{print \$2}'`
echo $PID;
if [ ! \"$PID\" ] ;then
	     echo \"pid is not exist\"
     else
	         echo \"killing PID$PID\"
		     kill -9 $PID
	     fi

