package handler

import (
	"context"

	"firebase.google.com/go/auth"
)

var FbClient *auth.Client

// validate UserID from Firebase
func authenticateIdToken(idToken string) bool {
	_, err := FbClient.VerifyIDTokenAndCheckRevoked(context.Background(), idToken)
	return err == nil
}

func recogniseTestUser(idToken string) bool {
	return idToken == TESTAPIKEY
}
