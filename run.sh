#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/src"
MYSQL_DIR="$ROOT_DIR/mysql"
JAR="$SRC_DIR/mysql-connector-j-9.7.0.jar"
CP="$SRC_DIR:$JAR"

if [[ ! -f "$JAR" ]]; then
    echo "Error: no se encontro el driver MySQL en $JAR"
    exit 1
fi

echo "==> Verificando MySQL..."
if ! docker compose -f "$MYSQL_DIR/docker-compose.yml" ps --status running 2>/dev/null | grep -q mysql; then
    echo "==> Levantando MySQL con Docker..."
    docker compose -f "$MYSQL_DIR/docker-compose.yml" up -d
    echo "==> Esperando a que MySQL este listo..."
    sleep 5
fi

echo "==> Compilando..."
javac -sourcepath "$SRC_DIR" -cp "$JAR" \
    "$SRC_DIR"/schemas/*.java \
    "$SRC_DIR"/horses/*.java \
    "$SRC_DIR"/dtos/*.java \
    "$SRC_DIR"/config/*.java \
    "$SRC_DIR"/daos/*.java \
    "$SRC_DIR"/controllers/*.java \
    "$SRC_DIR"/iu/*.java

echo "==> Ejecutando Carrera de Caballos..."
java -cp "$CP" iu.Main
