@echo off
echo Starting Agrolink Backend...
echo.
REM Set Maven User Home to a local folder to avoid permission errors in C:\Users\...
set "MAVEN_USER_HOME=%CD%/.mvn/wrapper/local"

REM Optional: pass port as first argument. Example: run_backend.bat 8082
set "PORT=%~1"
if "%PORT%"=="" set "PORT=8080"

echo Using port %PORT%

REM MongoDB connection
if "%MONGODB_URI%"=="" (
	echo MongoDB: using default local connection (set MONGODB_URI to use Atlas)
) else (
	echo MongoDB: using MONGODB_URI from environment
)

REM Run the application
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=%PORT%"

pause
