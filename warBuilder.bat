compileTest.bat
cd D:\Projects\Git\Framework-Project-ETU001979\Test_framework
jar cvf deployed.war *
xcopy /Y deployed.war D:\Tools\Tomcat\webapps\
del deployed.war