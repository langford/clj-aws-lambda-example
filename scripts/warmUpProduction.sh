aws lambda invoke --invocation-type RequestResponse \
    --function-name backendProduction \
    --region us-east-1 \
    --log-type Tail  \
    --payload '{}' \
    ~/Desktop/warmup_output.txt

    
