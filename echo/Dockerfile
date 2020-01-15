
####### build CONTAINER #######
FROM golang:1.13.6-alpine3.11 AS build
WORKDIR /go/src
COPY ./ .

ENV GO111MODULE=on
ENV CGO_ENABLED=0
RUN apk add git tzdata ca-certificates
# RUN git config --global url."https://{{GitHubPersonalAccessToken for PrivateRepository}}:x-oauth-basic@github.com/".insteadOf "https://github.com/"
RUN go get -d -v ./...
# RUN go test -v ./...
RUN go build -a -installsuffix cgo -o echo .

####### production CONTAINER #######
FROM scratch AS runtime

COPY --from=build /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt
COPY --from=build /usr/share/zoneinfo /usr/share/zoneinfo
ENV TZ=Asia/Tokyo

COPY --from=build /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=build /go/src/echo ./

EXPOSE 1323/tcp
ENTRYPOINT ["./echo"]
