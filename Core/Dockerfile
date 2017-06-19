FROM golang

ADD . /go/src/github.ibm.com/gsingh/MDMCore

WORKDIR /go/src/github.ibm.com/gsingh/MDMCore

RUN go get .
RUN go build .

ENTRYPOINT ./MDMCore

EXPOSE 8080

