package api

import (
	"context"
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
	opt := option.WithCredentialsJSON([]byte(os.Getenv("FIREBASE_CREDENTIALS")))
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

	/*
		// Cookie Encryption for safer Storage
		proc.Use(encryptcookie.Config{
			Key: cookieSecret,
		})
	*/

	// Testing Basic Connectivity
	proc.Get("/test", handler.HandleTest)

	// Return relevant Fh Data on Request
	proc.Get("/fhData/:query/:from/:to", handler.HandleFHData)

	// ----- Interaction Data End-Points

	// Route Request
	// TODO

	// Verbund Request
	// TODO

	// Pic&Push Request
	// TODO

	// Alarm Request
	// TODO
}
