#!/usr/bin/env bash

cd $(dirname "${BASH_SOURCE}")/..

#echo "==== starting to build admin-service ===="
#mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -Dmaven.gitcommitid.skip=true -Dapollo_profile=auth -P k8s -pl apollo-adminservice -am

#echo "==== starting to build config-service ===="
#mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -Dmaven.gitcommitid.skip=true -Dapollo_profile=auth -P k8s -pl apollo-configservice -am

#echo "==== starting to build portal ===="
#mvn clean package dockerfile:build -DskipTests -Dassembly.skipAssembly=true -Dmaven.gitcommitid.skip=true -Dapollo_profile=auth -P k8s -pl apollo-portal -am

mvn clean package dockerfile:build -DskipTests -Dapollo_profile=auth -P docker -pl '!apollo-mockserver';
#mvn versions:set -DnewVersion=2.0.0.M2 -DprocessAllModules=true -DallowSnapshots=true -DgenerateBackupPoms=false