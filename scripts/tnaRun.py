from runGame import *

BOOTSRAP_FILE = "Game1" 
AGENTS = "temporaryName,TUC_TAC"
AGENT_INFO = {"temporaryName": ["brokers/temporaryName", "temporaryName-1.7.0.jar"],
              "TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}
#AGENTS = "TUC_TAC"              
#AGENT_INFO = {"TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}

#run_bootstrap(BOOTSRAP_FILE)
game = threading.Thread(target=run_game, args=(BOOTSRAP_FILE,AGENTS))
game.start()
threads = [threading.Thread(target=run_agent, args=(agent_name,AGENT_INFO[agent_name][0], AGENT_INFO[agent_name][1])) for agent_name in AGENT_INFO.keys()]

for thread in threads:
    thread.start()


import atexit
import sys
import time

import psutil


def cleanup():
    timeout_sec = 5
        
    current_process = psutil.Process()
    children = current_process.children(recursive=True)
    for p in children:
        p_sec = 0
        for second in range(timeout_sec):
            if p.poll() == None:
                time.sleep(1)
                p_sec += 1
        if p_sec >= timeout_sec:
            p.kill() # supported from python 2.6
    print('cleaned up!')


atexit.register(cleanup)
