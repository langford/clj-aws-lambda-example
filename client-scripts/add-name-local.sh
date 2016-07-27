#!/bin/sh
export JSON="{\"name\":\"$1\",\"event\":\"Clubbing\"}"
echo $JSON
curl -H "Content-Type: application/json" -X POST -d $JSON  http://localhost:8100


