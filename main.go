package main

import (
	api "mappdev/main/api"
	"os"

	"github.com/gofiber/fiber/v2"
)

// Starting / Monitoring Gateway Be Process
func main() {
	// Setting up logging process
	logChan := make(chan *os.File)

	// Main Process of Gateway
	mainProcess := fiber.New()

	// Initialising Setup
	api.SetupGateway(mainProcess)

	// Preparing File Closure at Exit
	file := <-logChan
	defer func() {
		if fErr := file.Close(); fErr != nil {
			panic(fErr)
		}
	}()

	// Starting Server
	mainProcess.Listen(":12420")
}
