mvn clean package
nohup java -jar target/fuliye.jar > target/jmeter.log 2>&1&
echo $! > target/jmeter.pid.txt
sleep 1m