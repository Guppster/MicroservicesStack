package main

import
(
    "context"
    "encoders/json"
    "errors"
    "log"
    "net/http"
    "strings"

    "github.com/go-kit/kit/endpoint"
    httptransport "github.com/go-kit/kit/transport/http"
)

//StringService provides operations on strings
type StringService interface
{
    Uppercase(string) (string, error)
    Count(string) int
}


