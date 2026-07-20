  #!/usr/bin/env bash

set -euo pipefail

KAFKA_HOME="${KAFKA_HOME:-/opt/kafka}"
CONFIG_FILE="${CONFIG_FILE:-$KAFKA_HOME/config/server.properties}"

echo "========================================="
echo " Kafka KRaft Setup"
echo "========================================="

if ! command -v java >/dev/null 2>&1; then
    echo "Java is not installed."
    exit 1
fi

echo "Java Version:"
java -version

if [ ! -d "$KAFKA_HOME" ]; then
    echo "Kafka not found at $KAFKA_HOME"
    exit 1
fi

if [ ! -f "$CONFIG_FILE" ]; then
    echo "Kafka config not found at $CONFIG_FILE"
    exit 1
fi

cd "$KAFKA_HOME"

LOG_DIRS=$(grep "^log.dirs=" "$CONFIG_FILE" | cut -d= -f2- | tr -d '[:space:]' || true)

if [ -z "$LOG_DIRS" ]; then
    echo "log.dirs not found in $CONFIG_FILE"
    exit 1
fi

PRIMARY_LOG_DIR="${LOG_DIRS%%,*}"
mkdir -p "$PRIMARY_LOG_DIR"

if [ ! -f "$PRIMARY_LOG_DIR/meta.properties" ]; then
    echo "Kafka storage is not initialized."

    CLUSTER_ID=$(bin/kafka-storage.sh random-uuid)
    echo "Generated Cluster ID: $CLUSTER_ID"

    bin/kafka-storage.sh format \
        --standalone \
        -t "$CLUSTER_ID" \
        -c "$CONFIG_FILE"

    echo "Kafka storage initialized."
else
    echo "Kafka storage already initialized."
fi

echo "Starting Kafka..."

exec bin/kafka-server-start.sh "$CONFIG_FILE"
