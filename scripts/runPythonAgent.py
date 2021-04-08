import os
import threading
from time import sleep

GRPC_BOOT_TIME = 5

def run_python_agent(agent_name, path, args):
    java_interface = threading.Thread(target=run_maven, args=(path, args,))
    java_interface.run()
    sleep(GRPC_BOOT_TIME)
    python_agent = threading.Thread(target=run_main, args=(path,))
    python_agent.run()
    

def run_main(path):
    return_value = os.system(f"cd .. && cd {path}/python_broker && python3 main.py ")
    print(return_value)


def run_maven(path, args):
    return_value = os.system(f"cd .. && cd {path} && mvn exec:exec -Dexec.args=\"{args}\"")
    print(return_value)