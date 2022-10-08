package logger

import (
	"log"
	"os"
)

const fileLocation = "C:/Users/timok/Documents/Coding/MAPPDEV_Backend/logs"

var (
	DBLogger      *log.Logger
	MainLogger    *log.Logger
	HandlerLogger *log.Logger
	//SecurityLogger  *log.Logger
	RetrieverLogger *log.Logger
)

// Setting up Logger
func SetupLogger(ch chan<- *os.File) {
	// Basic checking before Logger Setup
	logFile := checkFileExistance()

	DBLogger = log.New(logFile, "DB:\t\t\t", log.Ldate|log.Ltime|log.Lshortfile)
	MainLogger = log.New(logFile, "Main:\t\t\t", log.Ldate|log.Ltime|log.Lshortfile)
	HandlerLogger = log.New(logFile, "Handler:\t\t", log.Ldate|log.Ltime|log.Lshortfile)
	//SecurityLogger = log.New(file, "Security: ", log.Ldate|log.Ltime|log.Lshortfile)
	RetrieverLogger = log.New(logFile, "Retriever: ", log.Ldate|log.Ltime|log.Lshortfile)

	// Declaring new instance / returning pointer for deferred close
	MainLogger.Println("----- NEW INSTANCE -----")
	ch <- logFile
}

// Check for existing Logfile / Create new one
func checkFileExistance() *os.File {
	// Opening LogFile
	file, err := os.OpenFile(fileLocation+"/logFile.txt", os.O_APPEND|os.O_WRONLY, 0666)
	if err != nil {
		err = os.WriteFile(fileLocation+"/logFile.txt", []byte("----- new File created -----"), 0644)
		if err != nil {
			log.Fatal("Logfile Creation Failed")
		}
	}
	return file
}
