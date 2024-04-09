#!/usr/bin/env bash

if [ -d "../database/dbDump" ]; then
	# Store database into a dump file
	printf "\033[32m||========================||\033[0m\n"
	printf "\033[32m||  Exporting Database... ||\033[0m\n"
	printf "\033[32m||========================||\033[0m\n"

	mongodump --db meowDB --out ../database/dbDump
else
	echo "Couldn't Export Database !"
fi
