# Root-Navigator Backend

## Purpose
This part of our backend is responsible for the authentication and forwarding of data requests.
Since certain data actions are quite expensive tasks (HAFAS requests, ...), we want to make sure only authorized parties have the ability to use our instance.

## Implementation
Contrary to the other backend, I focused on the Go Fiber framework for backend applications.
Requests are dynamically authenticated by using our Firebase instance and then forwarded.
It was built to be highly scalable and performant as to impact data flows the least possible.

## Hosting
Just as the other backend, we are relying on GCloud serverless computing.
