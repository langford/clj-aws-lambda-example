#!/bin/sh
export JSON="{\"name\":\"$1\",\"event\":\"Dance\"}"
echo $JSON
curl -H "Content-Type: application/json" -X POST -d $JSON  http://localhost:8100


