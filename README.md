# Root Navigator
[![G-Unit](https://github.com/stefanjb-it/elcapo-paris/actions/workflows/G-Unit.yml/badge.svg)](https://github.com/stefanjb-it/elcapo-paris/actions/workflows/G-Unit.yml)
[![G-Deployment](https://github.com/stefanjb-it/elcapo-paris/actions/workflows/G-Deployment.yml/badge.svg)](https://github.com/stefanjb-it/elcapo-paris/actions/workflows/G-Deployment.yml)

**Open API Documentation of Requests:**
- https://v5.db.transport.rest/api.html

The hafas backend is programmed in Javascript and uses the following packages:
- [hafas-client](https://www.npmjs.com/package/hafas-client)
- [hafas-rest-api](https://www.npmjs.com/package/hafas-rest-api)

There are also some additional packages for testing:
- [chai](https://www.npmjs.com/package/chai)
- [chai-http](https://www.npmjs.com/package/chai-http)

The Hafas backend is running in Google Cloud via Cloud Run. Also CI/CD pipelines/actions are
configured for the backend. These can be find in the .github/workflows folder in the Github repo
under the hafas-oebb branch.

**G-Unit Workflow:**
- runs automatically after any push
- uses the availability.js file and runs it with mocha
- checks for the correct response types
- checks for the correct response
- checks if API is still working

**G-Deployment Workflow:**
- runs automatically after a push is tagged with deployment
- runs the same test as G-Unit workflow
- creates an deployment for gcloud
- starts the prepared deployment 
- secrets for the deployment are stored in the secret store of the github repo

The additional Dockerfile is for the gcloud deployment, because by default gcloud
uses the NodeJS image for the container. This image has a raw size of 1.8GB, but due to
the fact that our backend only starts when a request is coming in we have to use a more
lightweight image to optimize startup time. This is why we are using the NodeJS apline image
and then installing our required packages.

## Local setup (no Docker)

### Requirements
- Minimum Nodejs version 16

### Setup
1. Download the project
2. Unzip it
3. Open a terminal or cmd
4. Navigate into the project folder
5. Run `npm ci --omit=dev` to install non-dev dependencies
6. Run `npm start`
7. Now the API should be available under http://localhost:8080

## Local setup (Docker)

### Requirements
- A system with Docker Desktop or Dockered.io installed

### Setup
1. Download the project
2. Unzip it
3. Open a terminal or cmd
4. Navigate into the project folder
5. Now run `docker build -t oebb-hafas .` (This will build an image with the name oebb-hafas)
6. After the build succeeded you should see the image via `docker images` command
7. Now run `docker run -d [NAME FOR YOUR CONTAINER] -e PORT=[YOUR PREFERRED PORT] -p [EXTERNAL ACCESS PORT FOR API]:[YOUR PREFERRED PORT (SAME AS BEFORE)]`
8. With `docker ps` you can check if your container is running
9. Now the API should be available under http://localhost:[EXTERNAL_ACCESS_PORT]


