# Convert to JSON
FROM node:14-alpine3.14 AS build

COPY bin/* /build/bin/
RUN /build/bin/install

WORKDIR /build/data
COPY registers/ registers/
RUN /build/bin/convert_all

# Static website
# This can be deployed as a tiny reference data service, serving static files
# OR it can be mounted up as a sidecar container containing the files for direct access for any app
FROM bitnami/nginx:1.19

LABEL maintainer="HMPPS Digital Studio <info@digital.justice.gov.uk>"

COPY --from=build /build/data/registers/*.csv /data/
COPY --from=build /build/data/registers/*.json /data/
COPY /app/nginx.conf /opt/bitnami/nginx/conf/server_blocks/default.conf
