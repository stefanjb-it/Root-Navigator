package api

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"

	handler "mappdev/main/api/handler"
)

// Setup for Routes, CORS
func SetupGateway(proc *fiber.App) {
	/*
		// Loading Cookie Secret from DB
		cookieSecret := "unglaublichHerrlicher92Schluessel"
	*/

	// Setup CORS for User Access Management
	proc.Use(cors.New(cors.Config{
		AllowCredentials: true,
	}))

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
}
