@echo off
REM Variables : 

set base_path=Z:\Tools\Intsallation\Tomcat\webapps\Framework-Project-ETU001979
set framework_path=%base_path%\Framework
set Test_Framework_path=%base_path%\Test_Framework
set src_path=%framework_path%\src
set build_path=%framework_path%\build
set lib_path=%Test_Framework_path%\WEB-INF\lib

REM compilation des fichiers sources
cd %src_path%
call compile.bat

REM depalcement des .class vers le dossier build
robocopy .\etu1979 %build_path%\etu1979\ /E /IS /MOVE

REM creation de la bibliotheque jar
cd %build_path%
jar cvf framework.jar .\etu1979\*

REM copie de la bibliotheque jar vers le repertoire de librairie du projet test
xcopy .\framework.jar %lib_path%\ /C /I /Y