#!/usr/bin/env bash

if [ -d "../database/dbDump" ]; then
  # Store database into a dump file
  echo "Export Database..."
  mongodump --db meowDB --out ../database/dbDump
else
  echo "Couldn't Export Database !"
fi

# Stop mongodb
echo "Stop MongoDB..."
sudo systemctl stop mongod
