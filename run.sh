#!/bin/bash
# ═══════════════════════════════════════
# 期末專題 — 一鍵編譯執行腳本 (Mac/Linux)
# ═══════════════════════════════════════

set -e

# 建立目錄
mkdir -p out lib

# 檢查 PostgreSQL JDBC Driver
if [ ! -f lib/postgresql-42.7.3.jar ]; then
    echo "📥 下載 PostgreSQL JDBC Driver..."
    curl -L -o lib/postgresql-42.7.3.jar \
        https://jdbc.postgresql.org/download/postgresql-42.7.3.jar
    echo "✅ 下載完成！"
fi

# 編譯
echo "🔨 編譯中..."
javac -cp "lib/*" -d out -encoding UTF-8 \
    src/main/java/com/silvercare/model/enums/*.java \
    src/main/java/com/silvercare/model/*.java \
    src/main/java/com/silvercare/config/*.java \
    src/main/java/com/silvercare/dao/*.java \
    src/main/java/com/silvercare/util/*.java \
    src/main/java/com/silvercare/service/*.java \
    src/main/java/com/silvercare/view/*.java \
    src/main/java/com/silvercare/Main.java

if [ $? -eq 0 ]; then
    echo "✅ 編譯成功！"
    echo ""
    java -cp "out:lib/*" com.silvercare.Main
else
    echo "❌ 編譯失敗！請檢查錯誤訊息。"
fi
