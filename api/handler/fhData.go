package handler

import (
	"encoding/json"
	"io/ioutil"
	logger "mappdev/main/logger"
	"net/http"
	"strings"

	"github.com/gofiber/fiber/v2"
)

func HandleFHData(c *fiber.Ctx) error {

	// Splitting Parameters
	query, from, to := c.Params("query"), c.Params("from"), c.Params("to")
	if len(query) != 5 || len(from) != 10 || len(to) != 10 {
		return c.JSON(fiber.Map{
			"code": 10,
			"data": nil,
		})
	}

	// Init Data Retrieval
	data, state := StartRequest(query, from, to)
	if !state {
		return c.JSON(fiber.Map{
			"code": 11,
			"data": nil,
		})
	}

	//fmt.Println("worked through request:", string(data))
	return c.JSON(fiber.Map{
		"code": 0,
		"data": string(data),
	})

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

func StartRequest(study, from, to string) (string, bool) {
	study = studyProgram + study
	from = timeFrom + from
	to = timeTo + to
	resp, err := http.Get(DataAdress + study + from + to)
	if err != nil {
		logger.RetrieverLogger.Println("Invalid Request -->", study, from, to)
		return "", false
	}
	content, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		logger.RetrieverLogger.Println("Invalid Response -->", study, from, to)
		return "", false
	}

	var relevantContent []ExternalDataInstance
	err = json.Unmarshal(content, &relevantContent)
	if err != nil {
		logger.RetrieverLogger.Println("Invalid Parsing -->", study, from, to)
		return "", false
	}

	var resultList []InternalDataInstance
	// Process raw information
	for _, instance := range relevantContent {
		t, prof, typ, room := splitTitle(instance.Title)
		resultList = append(resultList, InternalDataInstance{
			Title: t, Prof: prof, Typ: typ, Room: room, Start: instance.Start, End: instance.End,
		})
	}
	d, err := json.Marshal(resultList)
	if err != nil {
		logger.RetrieverLogger.Println("Invalid Re-Parsing -->", resultList)
		return "", false
	}

	return string(d), true
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
