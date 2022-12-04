package handler

import (
	"io"
	"log"
	"net/http"

	"github.com/gofiber/fiber/v2"
)

// Process HAFAS Requests
func ForwardData(c *fiber.Ctx) error {

	// Request Authentication
	userId := c.Cookies("userId")
	if !authenticateIdToken(userId) {
		return c.Status(http.StatusUnauthorized).JSON(nil)
	}

	// Split query elements
	queryString := c.Request().URI().QueryArgs()
	endPoint := c.Params("endpoint")

	// Reqeust URI
	uri := "https://oebb-hafas.fastcloud-it.net/" + endPoint + "?" + queryString.String()

	// Request / error handling
	resp, err := http.Get(uri)
	if err != nil {
		log.Println(err)
		return c.Status(500).Send(nil)
	}
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		log.Println(err)
		return c.Status(500).Send(nil)
	}

	// successful case
	return c.Status(200).Send(body)
}
