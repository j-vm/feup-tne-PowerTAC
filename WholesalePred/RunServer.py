from http.server import BaseHTTPRequestHandler, HTTPStatus, ThreadingHTTPServer
import json


class OurBaseHandler(BaseHTTPRequestHandler):
    def _set_OK_response(self):
        self.send_response(HTTPStatus.OK)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
    
    def do_GET(self):
        self._set_OK_response()
        self.wfile.write("GET request for {}".format(self.path).encode('utf-8'))

    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        json_string = post_data.decode('utf-8')

        print(f"""POST request,
        Headers:
        {str(self.headers)}
        """)

        aDict = json.loads(json_string)
        print(aDict)

        self._set_OK_response()
        self.wfile.write("POST request for {}".format(self.path).encode('utf-8'))

def serve_endpoint(address, port):
    server_address = (address, port)

    server = ThreadingHTTPServer(server_address, OurBaseHandler)

    server.serve_forever()

serve_endpoint('localhost', 4443)