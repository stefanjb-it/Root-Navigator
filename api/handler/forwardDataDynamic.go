package handler

import (
	"io"
	"log"
	"net/http"

	"github.com/gofiber/fiber/v2"
)

var URI string

const TESTAPIKEY = "_iouwozhAOPJUWz821jpiUpo&ai2woz1442hjpoijhwpqujjqpWAFR"

func ForwardDataDynamic(c *fiber.Ctx) error {
	userId := c.Cookies("userId")

	if !recogniseTestUser(userId) {
		if !authenticateIdToken(userId) {
			return c.Status(http.StatusUnauthorized).JSON(nil)
		}
	}

	queryString := c.Request().URI().QueryArgs()

	localURI := URI

	paramList := []string{c.Params("ep1"), c.Params("ep2"), c.Params("ep3")}

	for _, param := range paramList {
		if param != "" {
			localURI += "/" + param
		}
	}

	localURI += "?" + queryString.String()

	resp, err := http.Get(localURI)
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
