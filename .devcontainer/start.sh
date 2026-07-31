#!/usr/bin/env bash

set -e

cd "$(dirname "$0")/.."

mkdir -p /tmp/laurierfs

if ! curl --silent --fail http://localhost:8080/api/products > /dev/null 2>&1; then
	nohup java -cp "lib/*:bin" server.Server > /tmp/laurierfs/server.log 2>&1 &
fi

if ! curl --silent --fail http://localhost:5173 > /dev/null 2>&1; then
	nohup npm run dev --prefix laurierFS-client -- --host 0.0.0.0 > /tmp/laurierfs/client.log 2>&1 &
fi

echo "LaurierFS is starting. Open forwarded port 5173 for the client."