# Message receiver service

<!-- TOC -->
* [Message receiver service](#message-receiver-service)
* [Design](#design)
* [Message Receiver Service](#message-receiver-service-1)
    * [Dataflow](#dataflow)
        * [ReceiveMessage](#receivemessage)
        * [RouteMessage](#routemessage)
        * [ValidateMessage](#validatemessage)
        * [SendToRabbitMq](#sendtorabbitmq)
* [REST API](#rest-api)
* [Examples](#examples)
    * [Sending a mail message](#sending-a-mail-message)
<!-- TOC -->

The `Message receiver service` or also referred as `MRS` is responsible for receiving messages through a REST API and
then based on the channel type, it will
determine the appropriate routing and send the message to RabbitMq.

# Design

```mermaid
graph LR
    Sender --> MRS[Message Receiver Service]
    subgraph Communication Service
        MRS --> RabbitMq
        RabbitMq --> MPS[Message Processing Service]
    end
    MPS --> Receiver

```

With supporting tools and services:

```mermaid
graph LR
    Sender --> Gateway[Gateway Service]
    Gateway[Gateway Service] --> KEY[Keycloak]
    Gateway[Gateway Service] --> MRS[Message Receiver Service]
    KEY[Keycloak]
    subgraph SC[Spring Cloud Services]
        Registry[Registry Service]
        Config[Config Service]
    end
    subgraph COMMS[Communication Services]
        MRS --> RabbitMq
        RabbitMq --> MPS[Message Processing Service]
    end
    SC --> COMMS
    MPS --> Receiver

```

# Message Receiver Service

## Dataflow

```mermaid
stateDiagram
    state MRS {
        [*] --> ReceiveMessage
        ReceiveMessage --> RouteMessage
        RouteMessage --> ValidateMessage
        ValidateMessage --> SendToRabbitMq
        SendToRabbitMq --> [*]
    }
```

### ReceiveMessage

This message is received through a REST API.
The API documentation is available at http://localhost:8080/swagger-ui/index.html.

### RouteMessage

This step determines the channel type of the message and based on that, it will route the message to the appropriate
queue in RabbitMq.

| Channel Type | Route                      |
|--------------|----------------------------|
| TEAMS        | messages.channel.teams.add |
| SLACK        | messages.channel.slack.add |
| EMAIL        | messages.channel.email.add |

### ValidateMessage

Before the message is sent to RabbitMq, it is validated to ensure that all required fields are present. Each object has
schema defined that is used to validate the message.

### SendToRabbitMq

Finally, the message is sent to RabbitMq using the appropriate routing key based on the channel type. The meessage is
then ready to be processed by the `Message Processing Service` (MPS).

# REST API

This message is received through a REST API.
The API documentation is available at http://localhost:8080/swagger-ui/index.html.

# Examples

**Each request requires Bearer token in the header**

### Acquire token

#### Request
```bash
curl --location 'keycloak-web:8090/realms/openleap-realm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_secret=***' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'scope=mrs_send' \
--data-urlencode 'client_id=message-receiver-service-client'
```
#### Response

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlS25RY3ZTSkxKYTIyOWMydEZZa0RpUjJyLXhLNGoxOUV5RlN6Z3ZuWGlzIn0.eyJleHAiOjE3NTEwMjA1NjEsImlhdCI6MTc1MTAyMDI2MSwianRpIjoiZjdjZGMyYzgtYzFmNi00NjkyLWJmNDUtNDY3ZjQ3ZDUwZjIzIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLXdlYjo4MDkwL3JlYWxtcy9vcGVubGVhcC1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5YzFhNmRmMy03OTY0LTQyNzAtYjBkZC01YzY4OTFjYmYyNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1vcGVubGVhcC1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoibXJzX3NlbmQgZW1haWwgcmVwb3J0LXNlcnZpY2UgcHJvZmlsZSIsImNsaWVudEhvc3QiOiIxOTIuMTY4LjY1LjEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1tZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiY2xpZW50QWRkcmVzcyI6IjE5Mi4xNjguNjUuMSIsImNsaWVudF9pZCI6Im1lc3NhZ2UtcmVjZWl2ZXItc2VydmljZS1jbGllbnQifQ.XPPqpJkgOZX0LsaN4pXDtny0HYdGrJvQlCsmTAhVdk1hEr8-dTvbkkp2moUIztcy8njmPb-bXdYm-1Q_6JYHgAPlIc2UQHJmYWk_LZnVMU_ghycVIH-cvsCiBUlD5mFIB39PrRI8U-nBOHp6MEQ763TXCxhOMO5X-Q6dAlZXTNWzR61QyNl8mGYHbbkH_um2A7dQcDStaMeMPoqp2FdIryKrTSvSQYIgtWyNMAw3jmMT3B3Alxs0v0bWIU0grQhkBMSrqUb5ny781qzRaclSWSI9nzcZrLyxDci-E23uAxGQif4CPVvCXhh6FJU1B9JRoFnK6m2vW4l-aEa9bfNLhg",
    "expires_in": 300,
    "refresh_expires_in": 0,
    "token_type": "Bearer",
    "not-before-policy": 0,
    "scope": "mrs_send email report-service profile"
}
```


### Teams message request using template message type
#### Request

```bash
curl --location 'localhost:8086/gw/mrs/message' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlS25RY3ZTSkxKYTIyOWMydEZZa0RpUjJyLXhLNGoxOUV5RlN6Z3ZuWGlzIn0.eyJleHAiOjE3NTEwMjA1NjEsImlhdCI6MTc1MTAyMDI2MSwianRpIjoiZjdjZGMyYzgtYzFmNi00NjkyLWJmNDUtNDY3ZjQ3ZDUwZjIzIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLXdlYjo4MDkwL3JlYWxtcy9vcGVubGVhcC1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5YzFhNmRmMy03OTY0LTQyNzAtYjBkZC01YzY4OTFjYmYyNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1vcGVubGVhcC1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoibXJzX3NlbmQgZW1haWwgcmVwb3J0LXNlcnZpY2UgcHJvZmlsZSIsImNsaWVudEhvc3QiOiIxOTIuMTY4LjY1LjEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1tZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiY2xpZW50QWRkcmVzcyI6IjE5Mi4xNjguNjUuMSIsImNsaWVudF9pZCI6Im1lc3NhZ2UtcmVjZWl2ZXItc2VydmljZS1jbGllbnQifQ.XPPqpJkgOZX0LsaN4pXDtny0HYdGrJvQlCsmTAhVdk1hEr8-dTvbkkp2moUIztcy8njmPb-bXdYm-1Q_6JYHgAPlIc2UQHJmYWk_LZnVMU_ghycVIH-cvsCiBUlD5mFIB39PrRI8U-nBOHp6MEQ763TXCxhOMO5X-Q6dAlZXTNWzR61QyNl8mGYHbbkH_um2A7dQcDStaMeMPoqp2FdIryKrTSvSQYIgtWyNMAw3jmMT3B3Alxs0v0bWIU0grQhkBMSrqUb5ny781qzRaclSWSI9nzcZrLyxDci-E23uAxGQif4CPVvCXhh6FJU1B9JRoFnK6m2vW4l-aEa9bfNLhg' \
--data-raw '{
  "recipients": [
    {
      "id": "kirilarsov@gmail.com",
      "channel": {
        "channelType": "TEAMS",
        "channelId": "1234567890",
        "tenantId": "1234567890"
      }
    }
  ],
  "message": {
    "messageType": "TEMPLATE",
    "name": "Teams message template",
    "templateParams": {
      "param1": "value1",
      "param2": "value2"
    },
    "attachments": [
      {
        "name": "host-A.log",
        "contentType": "text/plain",
        "base64Data": "dGVzdA==",
        "url": "https://example.com/test.txt"
      }
    ]
  }
}'
```
#### Response

200 OK

### Email message request using template message type

#### Request

```bash
curl --location 'localhost:8086/gw/mrs/message' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlS25RY3ZTSkxKYTIyOWMydEZZa0RpUjJyLXhLNGoxOUV5RlN6Z3ZuWGlzIn0.eyJleHAiOjE3NTEwMjA5ODYsImlhdCI6MTc1MTAyMDY4NiwianRpIjoiMDk1OWZhYWItMTc4Ny00OTVlLWI2NTItNDc4Y2EwZGRiZjhlIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLXdlYjo4MDkwL3JlYWxtcy9vcGVubGVhcC1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5YzFhNmRmMy03OTY0LTQyNzAtYjBkZC01YzY4OTFjYmYyNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1vcGVubGVhcC1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoibXJzX3NlbmQgZW1haWwgcmVwb3J0LXNlcnZpY2UgcHJvZmlsZSIsImNsaWVudEhvc3QiOiIxOTIuMTY4LjY1LjEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1tZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiY2xpZW50QWRkcmVzcyI6IjE5Mi4xNjguNjUuMSIsImNsaWVudF9pZCI6Im1lc3NhZ2UtcmVjZWl2ZXItc2VydmljZS1jbGllbnQifQ.ZeO6Kx3vKk208pvKYzZKz0dqDz4RFSZm1B3gvJVgiw6o8-8ZBiho5Z8OxWX1QdkPw1WLY3Lt-fjdmySo7RGVaMQOVOdAZXaHPXwE3vvYWjy9k75y-dmfIw0xnoQEb9X7B6NxPk9_Z1fVtb5gJQBw66lVAktrceNKQV5qGjJm_OzT7tAfB8eNoG4KX9CB0ZSbs9lOzc_EsZ9meAOm3hMmSm5xuaqm3VPT-D4aqQJyKpAUuhNLETxldkzRrPkRYUtpyym3OxelL30okYY_pJ_MY7twxvTlQk01lKTnFIhw1ESBrsat5GiC5bv9Pcxmr9YWpjZETnXI2yQi9EjeGaivCw' \
--data-raw '{
  "recipients": [
    {
      "id": "kirilarsov@gmail.com",
      "channel": {
        "channelType": "EMAIL",
        "bcc": "user@domain.com"
      }
    }
  ],
  "message": {
    "messageType": "TEMPLATE",
    "name": "Email message template",
    "templateParams": {
      "param1": "value1",
      "param2": "value2"
    },
    "attachments": [
      {
        "name": "host-A.log",
        "contentType": "text/plain",
        "base64Data": "dGVzdA==",
        "url": "https://example.com/test.txt"
      }
    ]
  }
}'
```

#### Response
202 Accepted

### Email message request using custom message type

#### Request

```bash
curl --location 'localhost:8086/gw/mrs/message' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlS25RY3ZTSkxKYTIyOWMydEZZa0RpUjJyLXhLNGoxOUV5RlN6Z3ZuWGlzIn0.eyJleHAiOjE3NTEwMjEwODIsImlhdCI6MTc1MTAyMDc4MiwianRpIjoiOWQxN2E5ZDctN2VmZS00ZTFiLThmM2MtMTU2YjQ2ZTM3NjY1IiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLXdlYjo4MDkwL3JlYWxtcy9vcGVubGVhcC1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5YzFhNmRmMy03OTY0LTQyNzAtYjBkZC01YzY4OTFjYmYyNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1vcGVubGVhcC1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoibXJzX3NlbmQgZW1haWwgcmVwb3J0LXNlcnZpY2UgcHJvZmlsZSIsImNsaWVudEhvc3QiOiIxOTIuMTY4LjY1LjEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1tZXNzYWdlLXJlY2VpdmVyLXNlcnZpY2UtY2xpZW50IiwiY2xpZW50QWRkcmVzcyI6IjE5Mi4xNjguNjUuMSIsImNsaWVudF9pZCI6Im1lc3NhZ2UtcmVjZWl2ZXItc2VydmljZS1jbGllbnQifQ.VBEWvOOkhcYr53Y5-FfpfxZ3o4UZbw9-26cmaOwbs7SfM7Ml6mB7XDgeeFXf8GNYF9p3Ip543pS1nA80k3Rna9AEk_S_lJQ5mXFFqKH1fWK7cvyF_u1EWxmlS6U3t51Fnc8N7Qfsl2SdMzGH9wAC-AXvy_bTVQzDpBWCT9HzvEka5_buWKZwuZSgHL9ta3eX5v8VbLDeFT7xQFQe38fhWZljs6l9gc2a4jjxaQxNGWWXXV3TZJ9LtUJ6bcvk5D2Bz9XMptlpZerP00zQpTJ9D6zzqrksrPfRXkv0whvPaLL4nOu7-j0D7-T1Gbs6MGv6NgxBcDEoiuTGn35f19mORg' \
--data-raw '{
  "recipients": [
    {
      "id": "kirilarsov@gmail.com",
      "channel": {
        "channelType": "EMAIL",
        "bcc": "user@domain.com"
      }
    }
  ],
  "message": {
    "messageType": "CUSTOM",
    "name": "Email message template",
    "subject": "Application Error Alert",
    "body": "Take action immediately!",
    "attachments": [
      {
        "name": "host-A.log",
        "contentType": "text/plain",
        "base64Data": "dGVzdA==",
        "url": "https://example.com/test.txt"
      }
    ]
  }
}
'
```

#### Response

202 Accepted