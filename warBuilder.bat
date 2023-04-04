xcopy /Y .\index.jsp .\Test_framework
xcopy /Y .\url.jsp .\Test_framework
jar cvf deployed.war .\Test_framework
xcopy /Y .\deployed.war D:\Tools\Tomcat\webapps\
del .\deployed.war
del .\index.jsp
del .\url.jsp