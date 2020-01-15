package main

import (
	"bytes"
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
	"strconv"

	echo "github.com/labstack/echo/v4"
	middleware "github.com/labstack/echo/v4/middleware"
)

type (
	demoReq struct {
		VAL int `json:"val"`
	}
)

// curl -X GET localhost:1323/demo/123 --header "Content-Type: application/json;charset=utf-8" | jq

func main() {
	e := echo.New()

	// Middleware
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	// Routes
	e.GET("/demo/:val", demo)

	// Start server
	e.Logger.Fatal(e.Start(":1323"))
}

func demo(c echo.Context) error {

	val, _ := strconv.Atoi(c.Param("val"))
	request := demoReq{
		VAL: val,
	}

	// marshal
	buf := &bytes.Buffer{}
	if data, err := json.Marshal(request); err == nil {
		buf = bytes.NewBuffer([]byte(data))
	}

	// Springへリクエスト(container_nameでもアクセスできる)
	api := "http://spring:8080/demo"
	resp, err := http.Post(api, "application/json", buf)
	if err != nil {
		return err
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return err
	}
	log.Printf("API Response : %s", string(body))

	return c.String(http.StatusOK, string(body))

}
