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

type stringService struct{}

//This is an implementation for stringService. Takes in a String, returns a tuple?
func (stringService) Uppercase(input String) (string, error)
{
    //Another language that does string comparisons with ==.
    if input == ""
    {
        return "", ErrEmpty
    }

    return strings.ToUpper(s), nil
}

//Another implementation for stringService. Returns int
func (stringService) Count(input String) int
{
    return len(input)
}

//We need request and response structs for each call. Not sure why... yet
type uppercaseRequest struct
{
    S string `json:"s"`
}

type uppercaseResponse struct
{
    V string `json:"v"`
    Err string `json:"err, omitempty"`
}

type countRequest struct
{
    S string `json:"s"`
}

type countResponse struct
{
    V int `json:"v"`
}



