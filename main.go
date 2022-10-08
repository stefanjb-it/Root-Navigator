package main

import (
	"github.com/gofiber/fiber/v2"
)

// Starting / Monitoring Gateway Be Process
func main() {
	// Main Process of Gateway
	mainProcess := fiber.New()
	mainProcess.Listen(":12000")
}
