#!/bin/bash
docker login
docker build -t adrimd1208/webapp18 -f docker/Dockerfile .
docker push adrimd1208/webapp18