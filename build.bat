call Framework\execute.bat

cd Z:\Tools\Intsallation\Tomcat\webapps\Framework-Project-ETU001979\

jar cvf framework.jar Framework\etu1979\*
xcopy .\framework.jar .\Test_Framework\WEB-INF\lib\ /C /I /Y
