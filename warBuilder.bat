jar cvf deployed.war .\Test_framework
xcopy /Y .\deployed.war D:\Tools\Tomcat\webapps\
del .\deployed.war