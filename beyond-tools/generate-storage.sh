#!/usr/bin/env bash
export MAVEN_OPTS="-Dfile.encoding=UTF-8"
mvn clean package mybatis-generator:generate -Dmybatis.generator.configurationFile=src\\main\\resources\\generator-config-storage.xml
