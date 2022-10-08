package handler

import (
	"github.com/gofiber/fiber/v2"
)

// Unit-Test checking if server is running
func HandleTest(c *fiber.Ctx) error {
	return c.SendString("Working...")
}
