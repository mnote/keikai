@echo off

chcp 936

cd /d %~dp0

title %cd%

set JAVA_HOME=%cd%\..\jdk8
set JAVA_BASE=%cd%

set JETTY_HOME=%cd%\..\jetty

%JAVA_HOME%\bin\java -jar %JETTY_HOME%\start.jar -Djava.io.tmpdir=%cd%\temp -javaagent:%cd%\webapps\kks\WEB-INF\lib\aspectjweaver-1.9.6.jar -Dfile.encoding="GBK" -server -Xms256m -Xmx1024m -Xverify:none

pause
