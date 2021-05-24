from http.server import BaseHTTPRequestHandler, HTTPStatus, ThreadingHTTPServer
import json

from WholesalePred.Preprocessing import Preprocessing

# from WholesalePred.Algorithms.LinearRegression import LinearRegression
# from WholesalePred.Algorithms.RandomForest import RandomForest

# list_of_models = [LinearRegression, RandomForest]
# for model in list_of_models:
#     m = Model(model_name, model.__init__())

class Server:
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

            data_dict = json.loads(json_string)
            Preprocessing.format_transform(data_dict)

            self._set_OK_response()
            self.wfile.write("POST request for {}".format(self.path).encode('utf-8'))

    @staticmethod
    def serve_endpoint(address="localhost", port=4443):
        server_address = (address, port)
        server = ThreadingHTTPServer(server_address, Server.OurBaseHandler)

        server.serve_forever()
