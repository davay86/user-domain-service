#!/usr/bin/env bash

docker rmi -f sleepingtalent/user-domain-api:snapshot
docker rmi -f sleepingtalent/user-service:latest
docker rmi -f sleepingtalent/email-service:latest
