package main

import
(
    "context"
    "encoding/json"
    "log"
    "errors"
    "net/http"
    "strings"

    "github.com/go-kit/kit/endpoint"
    httptransport "github.com/go-kit/kit/transport/http"
)

//Main function
func main() {
    service := stringService{}

    uppercaseHandler := httptransport.NewServer(
        makeUppercaseEndpoint(service),
        decodeUppercaseRequest,
        encodeResponse,
    )

    countHandler := httptransport.NewServer(
        makeCountEndpoint(service),
        decodeCountRequest,
        encodeResponse,
    )

    http.Handle("/uppercase", uppercaseHandler)
    http.Handle("/count", countHandler)
    log.Fatal(http.ListenAndServe(":8080", nil))
}

func decodeUppercaseRequest(context context.Context, req *http.Request) (interface{}, error){
    var request uppercaseRequest

    if err := json.NewDecoder(req.Body).Decode(&request); err != nil{
        return nil, err
    }

    return request, nil
}

func decodeCountRequest(context context.Context, req *http.Request) (interface{}, error) {
    var request countRequest

    if err := json.NewDecoder(req.Body).Decode(&request); err != nil{
        return nil, err
    }

    return request, nil
}

func encodeResponse(context context.Context, writer http.ResponseWriter, response interface{}) error {
    return json.NewEncoder(writer).Encode(response)
}

//Returns an endpoint for converting to uppercase
func makeUppercaseEndpoint(service StringService) endpoint.Endpoint{

    return func(context context.Context, request interface{}) (interface{}, error) {

        req := request.(uppercaseRequest)
        v, err := service.Uppercase(req.S)

        if err != nil {
            return uppercaseResponse{v, err.Error()}, nil
        }

        return uppercaseResponse{v, ""}, nil
    }
}

//Returns an endpoint for counting characters
func makeCountEndpoint(service StringService) endpoint.Endpoint{

    return func(context context.Context, request interface{}) (interface{}, error){

        req := request.(countRequest)
        v := service.Count(req.S)
        return countResponse{v}, nil

    }
}

//StringService provides operations on strings
type StringService interface{
    Uppercase(string) (string, error)
    Count(string) int
}

type stringService struct{}

//This is an implementation for stringService. Takes in a String, returns a tuple?
func (stringService) Uppercase(input string) (string, error) {

    //Another language that does string comparisons with ==.
    if input == ""{
        return "", ErrEmpty
    }

    return strings.ToUpper(input), nil
}

//Another implementation for stringService. Returns int
func (stringService) Count(input string) int{
    return len(input)
}

// ErrEmpty is returned when input string is empty
var ErrEmpty = errors.New("Empty string")

//We need request and response structs for each call. Not sure why... yet
type uppercaseRequest struct{
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

