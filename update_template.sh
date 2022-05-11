#!/bin/bash
export DB_USER=[user]
export DB_PASSWORD=[password]
export DATASOURCE_URL=[jdbc string]
mvn install
cp ./target/SpringServer-1.jar ../[git jar directory]/SpringServer-1.jar
cd ../[git jar directory] || exit
git add .
git commit -m "new SpringServer Version"
git push

