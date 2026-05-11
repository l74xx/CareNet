@echo off
REM ═══════════════════════════════════════
REM 期末專題 — 一鍵編譯執行腳本 (Windows)
REM ═══════════════════════════════════════

if not exist out mkdir out
if not exist lib mkdir lib

REM 檢查 JDBC Driver
if not exist lib\postgresql-42.7.3.jar (
    echo 請手動下載 PostgreSQL JDBC Driver:
    echo https://jdbc.postgresql.org/download/postgresql-42.7.3.jar
    echo 放到 lib\ 資料夾中
    pause
    exit /b
)

echo 編譯中...
javac -cp "lib\*" -d out -encoding UTF-8 ^
    src\main\java\com\template\model\enums\*.java ^
    src\main\java\com\template\model\*.java ^
    src\main\java\com\template\config\*.java ^
    src\main\java\com\template\dao\*.java ^
    src\main\java\com\template\service\*.java ^
    src\main\java\com\template\view\*.java ^
    src\main\java\com\template\Main.java

if %ERRORLEVEL% EQU 0 (
    echo 編譯成功！
    echo.
    java -cp "out;lib\*" com.template.Main
) else (
    echo 編譯失敗！
)
pause
