@echo OFF
:: ----------------------------------------
:: Script qui dÈmarre l'application d'évaluation
:: d'une expression parenthésée
:: Projet: Exemple CUP
:: Author : A. Roussanaly
:: Organization : Loria
:: date : Fev 2013
:: ---------------------------------------
:: Example of usage:
:: > run

:: -----------------------------------------
:: Change here
:: -----------------------------------------

:: --->1) project root dirname
set PROJ_HOME=%~p0\..

:: --->2) lib dir (where *.jar are stored)
set LIB_DIR=%PROJ_HOME%\lib

:: --->3) classes dir (where *.class are stored)
set CLASSES_DIR=%PROJ_HOME%\build\classes

:: --->4) the name of class to launch
set CLASS_NAME=fr.ul.miage.exemple.Main

:: -->5) extra parameters
set PARAMS=

:: -----------------------------------------
:: do not change below
:: -----------------------------------------

:: ---->Classpath automatique (c'est pas simple en DOS !)
setlocal ENABLEDELAYEDEXPANSION
if errorlevel 1 goto :err1
set PROJ_CP=%CLASSES_DIR%
for /F "delims=" %%i in ('dir /B /S "%LIB_DIR%\*.jar"') do set PROJ_CP=!PROJ_CP!;%%i

:: ---->java command
set COMMAND=java   -Xmx512m "-Djava.util.logging.config.file=%PROJ_HOME%\logging.properties" -classpath "%PROJ_CP%" %CLASS_NAME% %PARAMS% %1 %2 %3 %4 %5 %6 %7 %8 %9
::echo %COMMAND%
%COMMAND%
goto :end

:err1
echo Error : libraries not found (check the LIB_DIR path)...
:end
:: ----------------------------------------
::                  END
:: ----------------------------------------