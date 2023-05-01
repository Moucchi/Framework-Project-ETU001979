cd D:\Projects\Git\Framework-Project-ETU001979\Framework\Source
execute.bat
move etu1979 D:\Projects\Git\Framework-Project-ETU001979\Framework\Build
cd D:\Projects\Git\Framework-Project-ETU001979\Framework\Build\
jar cvf framework.jar *
xcopy /Y framework.jar D:\Projects\Git\Framework-Project-ETU001979\Test_framework\lib
xcopy /Y framework.jar D:\Tools\lib\
xcopy /Y framework.jar D:\Tools\Tomcat\lib\
del framework.jar
