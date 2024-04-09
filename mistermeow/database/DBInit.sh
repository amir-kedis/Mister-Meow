#!/usr/bin/env bash

if [ -d "../database/dbDump" ]; then
	# Import database from dump file
	printf "\033[32m||========================||\033[0m\n"
	printf "\033[32m||  Importing Database... ||\033[0m\n"
	printf "\033[32m||========================||\033[0m\n"
	mongorestore --db meowDB ../database/dbDump
else
	echo "Couldn't Import Database !"
fi
