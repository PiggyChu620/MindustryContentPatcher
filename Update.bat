@echo off
setlocal EnableExtensions EnableDelayedExpansion

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMddHHmm"') do set "ts=%%i"

git add .
if errorlevel 1 exit /b %errorlevel%

git commit -m "%ts%"
if errorlevel 1 exit /b %errorlevel%

git push
exit /b %errorlevel%
