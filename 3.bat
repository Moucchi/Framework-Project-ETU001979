cd W:\Projects\Git\Java\Web_dynamique\Framework-Project-ETU001979\Test_framework
jar cvf deployed.war *
xcopy /Y deployed.war W:\Tools\Intsallation\Tomcat\webapps
del deployed.war