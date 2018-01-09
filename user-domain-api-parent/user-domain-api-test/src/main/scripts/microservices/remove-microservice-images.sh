#!/usr/bin/env bash

docker rmi -f emtdevelopment/user-domain-api:snapshot
docker rmi -f emtdevelopment/user-service:latest
docker rmi -f emtdevelopment/email-service:latest
