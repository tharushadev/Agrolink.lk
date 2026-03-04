@echo off
echo Starting Agrolink Backend...
echo.
REM Set Maven User Home to a local folder to avoid permission errors in C:\Users\...
set "MAVEN_USER_HOME=%CD%/.mvn/wrapper/local"

REM Run the application
.\mvnw spring-boot:run

pause
