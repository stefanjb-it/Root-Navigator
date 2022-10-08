package api

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
)

// Setup for Routes, CORS
func SetupGateway(proc *fiber.App) {
	/*
		// Loading Cookie Secret from DB
		cookieSecret := "unglaublichHerrlicher92Schluessel"
	*/

	// Setup CORS for User Access Management
	proc.Use(cors.Config{
		AllowCredentials: true,
	})

	/*
		// Cookie Encryption for safer Storage
		proc.Use(encryptcookie.Config{
			Key: cookieSecret,
		})
	*/
}
