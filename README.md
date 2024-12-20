# gen_ai_training


This application provides endpoints to interact with an AI model.

Please add your credentials before running this application locally:
- OPEN_AI_KEY
- OPEN_AI_ENDPOINT
- OPEN_AI_DEPLOYMENT_NAME


## Endpoints

### Get an answer from AI model
**Session id** should be a valid UUID.

**Temperature** should be a value between 0 and 1.
Use values:
- close to 0 to get more predictable and conservative answers
- close to 1 to get more creative answers

```
POST /openai
{
  "sessionId": "6e1e6888-8543-4d75-bdcb-a11bd2fb3690",
  "prompt": "I want to find top-10 books about world history",
  "temperature": 1
}
```

### Delete chat history for session id
```
DELETE /openai/history?sessionId=6e1e6888-8543-4d75-bdcb-a11bd2fb3690
```

### Get deployments
Get deployment names and their ids
```
GET /openai/deployments
```
