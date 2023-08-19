
# Docker Run Commands for source(MySQL) and target(Postgres) databases

## Running MySQL Container
docker run --hostname=8a30365b680c --env=MYSQL_USER=mrgenco --env=MYSQL_PASSWORD=admin --env=MYSQL_ROOT_PASSWORD=admin --env=MYSQL_DATABASE=cdc  -p 3306:3306 -p 33060:33060  -d mysql:latest

## Running Postgres Container
docker run --hostname=57040267da3e --env=POSTGRES_PASSWORD=admin --env=POSTGRES_USER=mrgenco --env=POSTGRES_DB=analytics   -p 5432:5432  -d postgres:latest
