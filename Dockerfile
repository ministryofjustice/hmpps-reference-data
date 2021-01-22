FROM node:14-alpine3.12

RUN npm install --global --no-audit csvtojson

WORKDIR /data
COPY *.csv ./

ENTRYPOINT ["csvtojson"]
