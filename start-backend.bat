@echo off
echo ========================================
echo   Demarrage de Ega Bank Backend
echo ========================================
echo.
echo Base de donnees MongoDB: ega_bank
echo URI: mongodb://localhost:27017/ega_bank
echo.
echo Verifiez que MongoDB est en cours d'execution avant de continuer...
echo.
pause
echo.
echo Demarrage de l'application Spring Boot...
call mvnw.cmd spring-boot:run
