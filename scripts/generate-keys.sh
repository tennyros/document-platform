#!/bin/bash

KEYS_DIR=".docker/keys"
PRIVATE_KEY="$KEYS_DIR/private.key"
PUBLIC_KEY="$KEYS_DIR/public.key"

mkdir -p "$KEYS_DIR"

if [[ -f "$PRIVATE_KEY" || -f "$PUBLIC_KEY" ]]; then
  echo "Keys already exist. Delete them to generate new ones."
  exit 1
fi

openssl genpkey -algorithm RSA -out "$PRIVATE_KEY" -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in "$PRIVATE_KEY" -out "$PUBLIC_KEY"

echo "The keys have been generated: $PRIVATE_KEY, $PUBLIC_KEY"