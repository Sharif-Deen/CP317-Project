#!/usr/bin/env bash

set -e

cd "$(dirname "$0")/.."

mkdir -p /tmp/laurierfs

nohup java -cp "lib/*:bin" server.Server > /tmp/laurierfs/server.log 2>&1 &
nohup npm run dev --prefix laurierFS-client -- --host 0.0.0.0 > /tmp/laurierfs/client.log 2>&1 &

echo "LaurierFS is starting. Open forwarded port 5173 for the client."