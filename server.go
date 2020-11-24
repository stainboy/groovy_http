package main

import (
	"fmt"
	"io"
	"net/http"
	"time"
)

type flushWriter struct {
	f http.Flusher
	w io.Writer
}

func (fw *flushWriter) Write(p []byte) (n int, err error) {
	n, err = fw.w.Write(p)
	if fw.f != nil {
		fw.f.Flush()
	}
	return
}

func hello(w http.ResponseWriter, req *http.Request) {

	// https://stackoverflow.com/questions/19292113/not-buffered-http-responsewritter-in-golang
	fw := &flushWriter{w: w}
	if f, ok := w.(http.Flusher); ok {
		fw.f = f
	}

	for i := 1; i < 5; i++ {
		fmt.Fprintf(fw, "%d\n", i)
		time.Sleep(1 * time.Second)
	}

	fmt.Fprintf(fw, "11/#ERROR#4444\n")
	fmt.Fprintf(fw, "bye\n")
}

func main() {

	http.HandleFunc("/hello", hello)
	// http.HandleFunc("/headers", headers)

	http.ListenAndServe(":8090", nil)
}
