#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/src"
LIB_DIR="$ROOT_DIR/lib"
MYSQL_DIR="$ROOT_DIR/mysql"

if [[ ! -d "$LIB_DIR" ]] || [[ -z "$(find "$LIB_DIR" -maxdepth 1 -name '*.jar' -print -quit)" ]]; then
    echo "Error: no se encontraron dependencias en $LIB_DIR"
    echo "Ejecuta primero: bash download-libs.sh"
    exit 1
fi

CP="$SRC_DIR"
for jar in "$LIB_DIR"/*.jar; do
    CP="$CP:$jar"
done

echo "==> Verificando MySQL..."
if ! docker compose -f "$MYSQL_DIR/docker-compose.yml" ps --status running 2>/dev/null | grep -q mysql; then
    echo "==> Levantando MySQL con Docker..."
    docker compose -f "$MYSQL_DIR/docker-compose.yml" up -d
    echo "==> Esperando a que MySQL este listo..."
    sleep 5
fi

echo "==> Compilando..."
javac -sourcepath "$SRC_DIR" -cp "$CP" \
    "$SRC_DIR"/schemas/*.java \
    "$SRC_DIR"/horses/*.java \
    "$SRC_DIR"/dtos/*.java \
    "$SRC_DIR"/config/*.java \
    "$SRC_DIR"/daos/*.java \
    "$SRC_DIR"/controllers/*.java \
    "$SRC_DIR"/iu/*.java

echo "==> Ejecutando Carrera de Caballos..."
java -cp "$CP" iu.Main
