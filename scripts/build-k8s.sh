#!/usr/bin/env bash

cd $(dirname "${BASH_SOURCE}")/..

#echo "==== starting to build admin-service ===="
#mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -P k8s -pl apollo-adminservice -am

#echo "==== starting to build config-service ===="
#mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -P k8s -pl apollo-configservice -am

#echo "==== starting to build portal ===="
#mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -P k8s -pl apollo-portal -am

mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -P k8s -pl '!apollo-assembly,!apollo-demo,!apollo-mockserver'
