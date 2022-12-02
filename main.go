package main

import (
	api "mappdev/main/api"

	"github.com/gofiber/fiber/v2"
)

// Starting / Monitoring Gateway Be Process
func main() {
	// Main Process of Gateway
	mainProcess := fiber.New()

	// Initialising Setup
	api.SetupGateway(mainProcess)

	// Starting Server
	mainProcess.Listen(":8080")
}
