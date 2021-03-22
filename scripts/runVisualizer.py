import os
import threading
import time

PATH_SERVER = "server-distribution-master"

def run_vizualizer():
    return_value = os.system("cd.. && cd " + PATH_SERVER + " && mvn -Pweb2")

vizualizer = threading.Thread(target=run_vizualizer)

vizualizer.start()