FROM node:lts-alpine

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm ci --omit=dev
COPY . .

EXPOSE $PORT

CMD [ "npm", "start"]