package main

import (
	"errors"
	"strings"
)

// CoreService provides operations on MDMServices
type CoreService interface {
	GetPropertyMap(string) (string, error)
	Run(string) int
}

type coreService struct{}

func (coreService) GetPropertyMap(input string) (string, error) {
	if input == "" {
		return "", ErrEmpty
	}
	return strings.ToUpper(input), nil
}

func (coreService) Run(input string) int {
	return len(input)
}

// ErrEmpty is returned when an input string is empty.
var ErrEmpty = errors.New("empty string")
