#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

set -a
source ./.env
set +a

: "${DB_URL:?Заполни DB_URL в .env}"
: "${DB_USERNAME:?Заполни DB_USERNAME в .env}"
: "${DB_PASSWORD:?Заполни DB_PASSWORD в .env}"
: "${JWT_SECRET:?Заполни JWT_SECRET в .env}"

export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run