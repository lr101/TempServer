#!/bin/bash
mvn install
cp ./target/SpringServer-1.jar ../stuff/SpringServer-1.jar
cd ../stuff || exit
git add .
git commit -m "new SpringServer Version"
git push

