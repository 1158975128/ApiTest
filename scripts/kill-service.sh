PID=`cat target/jmeter.pid`
echo $PID;
if [ ! \"$PID\" ] ;then
	     echo \"pid is not exist\"
     else
	         echo \"killing PID $PID\"
		     kill -9 $PID
	     fi

