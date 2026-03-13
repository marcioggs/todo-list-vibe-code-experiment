#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRONTEND_DIR="$ROOT_DIR/frontend"
BACKEND_DIR="$ROOT_DIR/backend"

cleanup() {
  if [[ -n "${BACKEND_PID:-}" ]]; then
    kill "$BACKEND_PID" 2>/dev/null || true
  fi
  if [[ -n "${FRONTEND_PID:-}" ]]; then
    kill "$FRONTEND_PID" 2>/dev/null || true
  fi
}

trap cleanup EXIT INT TERM

if [[ ! -d "$FRONTEND_DIR/node_modules" ]]; then
  echo "Installing frontend dependencies..."
  npm --prefix "$FRONTEND_DIR" install
fi

echo "Starting backend on http://localhost:8080"
mvn -f "$BACKEND_DIR/pom.xml" spring-boot:run &
BACKEND_PID=$!

echo "Starting frontend on http://localhost:4200"
npm --prefix "$FRONTEND_DIR" start &
FRONTEND_PID=$!

wait "$BACKEND_PID" "$FRONTEND_PID"
