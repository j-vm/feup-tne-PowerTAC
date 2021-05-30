from runGame import *
from random import randint

AGENTS = "temporaryName,TUC_TAC"
AGENT_INFO = {"temporaryName": ["brokers/temporaryName", "temporaryName-1.7.0.jar"],
              "TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}
def create_many_bootstrap(number, base_name):
    threads = [threading.Thread(target=run_bootstrap, args=(base_name + str(i), )) for i in range(number)]
    for thread in threads:
        thread.start()
    for thread in threads:
        thread.join()

def run_many_games(number, base_name, n_of_bootstrap):
    
    #Start agent threads
    threads = [threading.Thread(target=run_agent, args=(agent_name,AGENT_INFO[agent_name][0], AGENT_INFO[agent_name][1], number)) for agent_name in AGENT_INFO.keys()]

    for thread in threads:
        thread.start()
        
    for i in range(number):
        game = threading.Thread(target=run_game, args=(base_name + str(randint(0, n_of_bootstrap-1)),AGENTS))
        game.start()
        
        game.join()
        delete_logs()

    cleanup()
        
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

import os, shutil

def delete_logs():
    folder = './log/'
    for filename in os.listdir(folder):
        file_path = os.path.join(folder, filename)
        try:
            if os.path.isfile(file_path) or os.path.islink(file_path):
                os.unlink(file_path)
            elif os.path.isdir(file_path):
                shutil.rmtree(file_path)
        except Exception as e:
            print('Failed to delete %s. Reason: %s' % (file_path, e))

#create_many_bootstrap(3, "testingBootstrapGames")
run_many_games(5, "testingBootstrapGames", 3)
#delete_logs()
