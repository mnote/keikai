@echo off

cd /d %~dp0

del /f /s /q %cd%\temp\*
del /f /s /q %cd%\logs\*
del /f /s /q %cd%\work\*

rd /s /q %cd%\logs
rd /s /q %cd%\temp
rd /s /q %cd%\work

del /s /q /f %cd%\*.sh
del /s /q /f /a .DS_STORE

mkdir logs
mkdir temp
mkdir work
