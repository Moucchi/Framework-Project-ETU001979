@echo off
REM Variables :
set /p project_name=Quel sera le nom du projet ? 

set tomcat_path=D:\Tools\Tomcat
set lib_path=Test_Framework\WEB-INF\lib
set jsp_path=Test_Framework\jsp
set web_xml=Test_Framework\WEB-INF\web.xml
set base_path=%tomcat_path%\webapps\Framework-Project-ETU001979

set destination=%base_path%\temp_project\WEB-INF\classes\
set lib=%base_path%\Test_Framework\WEB-INF\lib\framework.jar
set files=%base_path%\Test_Framework\WEB-INF\classes\*.java

REM creation de l'archive jar
call PortableBuid.bat

REM Création d'un dossier temporaire
mkdir temp_project

REM Création de la structure du projet pour tomcat
mkdir temp_project\WEB-INF temp_project\WEB-INF\classes temp_project\WEB-INF\lib

REM Copie des fichiers de lib et des fichiers jsp
xcopy /E %jsp_path%\* temp_project
xcopy /E %lib_path%\* temp_project\WEB-INF\lib

REM compilation des classes personnalises : 
javac -parameters -d %destination% -cp %lib%  %files%

REM Copie du fichier web.xml -> temp
copy %web_xml% temp_project\WEB-INF

REM Création de l'archive war
cd temp_project
jar -cvf "%project_name%.war" *

REM Déplacement de l'archive war vers tomcat
move "%project_name%.war" "%tomcat_path%\webapps"

REM Suppression du projet temporaire
cd ..
rmdir /S /Q temp_project
