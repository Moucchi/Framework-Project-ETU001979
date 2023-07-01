@echo off
REM Nom du service Tomcat
set "SERVICE_NAME=Tomcat10"

REM Démarrage du service Tomcat
net stop "%SERVICE_NAME%"
net start "%SERVICE_NAME%"