@echo off

set jmx=HTTP Request.jmx
set jtl=jmeter_%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%.jtl

jmeter -n -t "D:\DevOps\Test_JMeter\%jmx%" -l "D:\DevOps\Test_JMeter\%jtl%"