#!/bin/sh
export JSON="{\"name\":\"$1\",\"event\":\"Clubbing\"}"
echo $JSON
curl -H "Content-Type: application/json" -X POST -d $JSON https://cqezdcjn17.execute-api.us-east-1.amazonaws.com/Production


