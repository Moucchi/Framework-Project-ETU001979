move W:\Projects\Git\Java\Web_dynamique\Framework-Project-ETU001979\Framework\src\etu1979 W:\Projects\Git\Java\Web_dynamique\Framework-Project-ETU001979\Framework\build
cd W:\Projects\Git\Java\Web_dynamique\Framework-Project-ETU001979\Framework\build
jar cvf framework.jar *
xcopy /Y framework.jar W:\Projects\Git\Java\Web_dynamique\Framework-Project-ETU001979\Test_framework\WEB-INF\lib\
xcopy /Y framework.jar W:\Tools\Intsallation\Tomcat\lib
del framework.jar