set root=Z:\Tools\Intsallation\Tomcat\webapps\Framework-Project-ETU001979\Framework\src
set framework_path=%root%\etu1979\framework

javac -d %root% %framework_path%\Annotation\URL.java
javac -d %root% %framework_path%\Annotation\Scope.java
javac -d %root% %framework_path%\FileUpload.java
javac -d %root% %framework_path%\ModelView.java
javac -d %root% %framework_path%\Mapping.java
javac -d %root% %framework_path%\util\Inc.java
javac -d %root% %framework_path%\servlet\FrontServlet.java