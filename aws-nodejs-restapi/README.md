<!--
title: 'AWS Serverless REST API with DynamoDB and offline support example in NodeJS'
description: 'This example demonstrates how to run a service locally, using the ''serverless-offline'' plugin. It provides a REST API to manage Todos stored in DynamoDB.'
layout: Doc
framework: v1
platform: AWS
language: nodeJS
-->
# Serverless REST API with DynamoDB and offline support

This example demonstrates how to run a service locally, using the
[serverless-offline](https://github.com/dherault/serverless-offline) plugin. It
provides a REST API to manage Todos stored in a DynamoDB, similar to the
[aws-node-rest-api-with-dynamodb](https://github.com/serverless/examples/tree/master/aws-node-rest-api-with-dynamodb)
example. A local DynamoDB instance is provided by the
[serverless-dynamodb-local](https://github.com/99xt/serverless-dynamodb-local)
plugin.

## Use-case

Test your service locally, without having to deploy it first.

## Setup

```bash
npm install
serverless dynamodb install
serverless offline start
serverless dynamodb migrate (this imports schema)
```

## Run service offline

```bash
serverless offline start
```

## Usage

You can create, retrieve, update, or delete todos with the following commands:

### Create a Todo

```bash
curl -X POST -H "Content-Type:application/json" http://localhost:3000/todos --data '{ "text": "Learn Serverless" }'
```

Example Result:
```bash
{"text":"Learn Serverless","id":"ee6490d0-aa11e6-9ede-afdfa051af86","createdAt":1479138570824,"checked":false,"updatedAt":1479138570824}%
```

### List all Todos

```bash
curl -H "Content-Type:application/json" http://localhost:3000/todos
```

Example output:
```bash
[{"text":"Deploy my first service","id":"ac90feaa11e6-9ede-afdfa051af86","checked":true,"updatedAt":1479139961304},{"text":"Learn Serverless","id":"206793aa11e6-9ede-afdfa051af86","createdAt":1479139943241,"checked":false,"updatedAt":1479139943241}]%
```

### Get one Todo

```bash
# Replace the <id> part with a real id from your todos table
curl -H "Content-Type:application/json" http://localhost:3000/todos/<id>
```

Example Result:
```bash
{"text":"Learn Serverless","id":"ee6490d0-aa11e6-9ede-afdfa051af86","createdAt":1479138570824,"checked":false,"updatedAt":1479138570824}%
```

### Update a Todo

```bash
# Replace the <id> part with a real id from your todos table
curl -X PUT -H "Content-Type:application/json" http://localhost:3000/todos/<id> --data '{ "text": "Learn Serverless", "checked": true }'
```

Example Result:
```bash
{"text":"Learn Serverless","id":"ee6490d0-aa11e6-9ede-afdfa051af86","createdAt":1479138570824,"checked":true,"updatedAt":1479138570824}%
```

### Delete a Todo

```bash
# Replace the <id> part with a real id from your todos table
curl -X DELETE -H "Content-Type:application/json" http://localhost:3000/todos/<id>
```

No output


###list table
```
var dynamodb = new AWS.DynamoDB({
region: 'us-east-1',
endpoint: "http://localhost:5000"
});
var tableName = "serverless-rest-api-with-dynamodb-dev";

var params = {
TableName: "serverless-rest-api-with-dynamodb-dev",
Select: "ALL_ATTRIBUTES"
};


function doScan(response) {
if (response.error) ppJson(response.error); // an error occurred
else {
    ppJson(response.data); // successful response

    // More data.  Keep calling scan.
    if ('LastEvaluatedKey' in response.data) {
        response.request.params.ExclusiveStartKey = response.data.LastEvaluatedKey;
        dynamodb.scan(response.request.params)
            .on('complete', doScan)
            .send();
    }
}
}
console.log("Starting a Scan of the table");
dynamodb.scan(params)
.on('complete', doScan)
.send();
```