#!/usr/bin/env bash

# Start mongodb
echo "Start MongoDB..."
sudo systemctl start mongod

if [ -d "../database/dbDump" ]; then
  # Import database from dump file
  echo "Import Database..."
  mongorestore --db meowDB ../database/dbDump
else
  echo "Couldn't Import Database !"
fi
