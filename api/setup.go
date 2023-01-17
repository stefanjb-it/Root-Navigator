package api

import (
	"context"
	"encoding/base64"
	"log"
	"os"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"

	firebase "firebase.google.com/go"

	"google.golang.org/api/option"

	handler "mappdev/main/api/handler"
)

// Setup for Routes, CORS
func SetupGateway(proc *fiber.App) {
	// Setup CORS for User Access Management
	proc.Use(cors.New(cors.Config{
		AllowCredentials: true,
	}))

	// Connect to Firebase Instance
	plainText, err := base64.StdEncoding.DecodeString(os.Getenv("FIREBASE_CONFIG_BASE64"))
	if err != nil {
		log.Fatal(err)
	}

	// Setting URI for Forwarding
	handler.URI = os.Getenv("URI")

	// Setting Firebase Config
	os.Setenv("FIREBASE_CONFIG", string(plainText))

	// Creating Firebase App Instance
	opt := option.WithCredentialsJSON([]byte(plainText))
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		log.Fatal(err)
	}

	// Creating Firebase Authentication Instance
	client, err := app.Auth(context.Background())
	if err != nil {
		log.Fatalf("error getting Auth client: %v\n", err)
	}

	// Setting Auth Client to current Instance
	handler.FbClient = client

	// Testing Basic Connectivity
	proc.Get("/test", handler.HandleTest)

	// Return relevant Fh Data on Request
	proc.Get("/fhData/:query/:from/:to", handler.HandleFHData)

	//proc.Get("/api/hafas/v1/:endpoint", handler.ForwardData)
	proc.Get("/api/hafas/v1/:ep1?/:ep2?/:ep3?", handler.ForwardDataDynamic)
}
