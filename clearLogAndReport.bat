@echo off
setlocal enabledelayedexpansion

set parent=%~dp0

rem for /f %%i in ('dir /B %parent%\logs\*.log') do del "%parent%\logs\%%i"
rem for /f %%j in ('dir /B %parent%\report\*.html') do del "%parent%\report\%%j"
rem for /f %%k in ('dir /B %parent%\reports\*.html') do del "%parent%\reports\%%k"

del /f /s /q %parent%\logs\*.log
del /f /s /q %parent%\report\*.html
del /f /s /q %parent%\reports\*.html

rem pause
exit