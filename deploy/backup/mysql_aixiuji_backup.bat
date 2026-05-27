

@echo off
setlocal enabledelayedexpansion

:: 设置MySQL的bin目录路径（根据实际情况修改）
set MYSQL_PATH=E:\MySQL\

:: 设置数据库的用户名、密码、数据库名和备份文件的路径
set DB_USER=root
set DB_PASS=root
set DB_NAME=aixiuji
set BACKUP_DIR=D:\美发备份系统\数据备份
set DATE_FORMAT=%date:~0,4%%date:~5,2%%date:~8,2%

:: 创建备份目录(如果不存在)
if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

:: 生成带时间戳的备份文件名
:: 获取当前日期并格式化为yyyymmdd
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
set "YYYY=%dt:~0,4%"
set "MM=%dt:~4,2%"
set "DD=%dt:~6,2%"
set "fdate=%YYYY%%MM%%DD%"

:: 构建新文件名并输出
set "new_filename=aixiuji_25.1.0_%fdate%.sql"
echo 新备份文件名: %new_filename%
set BACKUP_FILE=%BACKUP_DIR%/%new_filename%

:: 执行MySQL备份
echo 正在备份数据库 %DB_NAME%...
%MYSQL_PATH%\mysqldump.exe -u%DB_USER% -p%DB_PASS% %DB_NAME% > "%BACKUP_FILE%"
if %ERRORLEVEL% equ 0 (
    echo 数据库备份成功: %BACKUP_FILE%
) else (
    echo 数据库备份失败
    exit /b 1
)

:: 删除2天前的备份文件
echo 正在清理2天前的备份文件...
forfiles /p "%BACKUP_DIR%" /s /m *.sql /d -2 /c "cmd /c del /q @path"

echo 备份和清理任务完成
endlocal
