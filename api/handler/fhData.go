package handler

import (
	"encoding/json"
	"io"
	"net/http"
	"strings"

	"github.com/gofiber/fiber/v2"
)

// Process FH-Data Requests
func HandleFHData(c *fiber.Ctx) error {
	userId := c.Cookies("userId")
	if !authenticateIdToken(userId) {
		return c.Status(http.StatusUnauthorized).JSON(nil)
	}

	// Splitting Parameters
	query, from, to := c.Params("query"), c.Params("from"), c.Params("to")
	if len(query) != 5 || len(from) != 10 || len(to) != 10 {
		c.Status(fiber.StatusBadRequest)
		return c.JSON(nil)
	}

	// Init Data Retrieval
	rawList, state := StartRequest(query, from, to)
	if !state {
		c.Status(fiber.StatusInternalServerError)
		return c.JSON(nil)
	}
	return c.JSON(rawList)
}

// ----- Data Types for Request -----
type ExternalDataInstance struct {
	Title string `json:"title"`
	Start string `json:"start"`
	End   string `json:"end"`
}

type InternalDataInstance struct {
	Title string `json:"title"`
	Prof  string `json:"prof"`
	Typ   string `json:"typ"`
	Room  string `json:"room"`
	Start string `json:"start"`
	End   string `json:"end"`
}

// ----- ----- ----- -----

const DataAdress string = "https://almaty.fh-joanneum.at/stundenplan/json.php?"

const studyProgram = "q="
const timeFrom = "&start="
const timeTo = "&end="

// !!! ONLY USE return list, otherwise double JSON conversion due to fiber !!!
func StartRequest(study, from, to string) ([]InternalDataInstance, bool) {
	study = studyProgram + study
	from = timeFrom + from
	to = timeTo + to
	resp, err := http.Get(DataAdress + study + from + to)
	if err != nil {
		return nil, false
	}
	content, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, false
	}

	var relevantContent []ExternalDataInstance
	err = json.Unmarshal(content, &relevantContent)
	if err != nil {
		return nil, false
	}

	var resultList []InternalDataInstance
	// Process raw information
	for _, instance := range relevantContent {
		t, prof, typ, room := splitTitle(instance.Title)
		resultList = append(resultList, InternalDataInstance{
			Title: t, Prof: prof, Typ: typ, Room: room, Start: instance.Start, End: instance.End,
		})
	}

	return resultList, true
}

// Splits Title into 4 main parts
func splitTitle(title string) (string, string, string, string) {
	parts := strings.Split(title, ",")
	switch len(parts) {
	case 1:
		//fmt.Println(parts)
		return strings.Trim(parts[0], " "), "", "", ""
	case 2:
		//fmt.Println(parts)
		return strings.Trim(parts[0], " "), strings.Trim(parts[1], " "), "", ""
	case 3:
		//fmt.Println(parts)
		return strings.Trim(parts[0], " "), strings.Trim(parts[1], " "), strings.Trim(parts[2], " "), ""
	case 5:
		//fmt.Println(parts)
		return strings.Trim(parts[0], " "), "", strings.Trim(parts[len(parts)-2], " "), strings.Trim(parts[len(parts)-1], " ")
	default:
		t := strings.Trim(parts[0], " ")
		prof := strings.Trim(parts[1], " ")
		typ := strings.Trim(parts[2], " ")
		room := strings.Trim(strings.Split(parts[3], "(")[0], " ")
		return t, prof, typ, room
	}
}
