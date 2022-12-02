FROM golang:alpine

WORKDIR /app

COPY go.mod ./
COPY go.sum ./

RUN go mod download

COPY *.go ./
COPY *.json ./

RUN mkdir api
COPY /api/setup.go ./api/

RUN mkdir ./api/handler
COPY /api/handler/*.go ./api/handler/

RUN go build -o /root-nv-backend

EXPOSE 8080

CMD [ "/root-nv-backend" ]