#!/bin/bash
docker login
docker build -t mdelvalle2020/webapp18 -f docker/Dockerfile .
docker push mdelvalle2020/webapp18