from runGame import *

def create_many_bootstrap(number, base_name):
    threads = [threading.Thread(target=run_bootstrap, args=(base_name + str(i), )) for i in range(number)]
    for thread in threads:
        thread.start()

def run_many_games(number, base_name, number_of_bootstrap):
    for i in range(number):
        game = threading.Thread(target=run_game, args=(BOOTSRAP_FILE,AGENTS))
        game.start()
        threads = [threading.Thread(target=run_agent, args=(agent_name,AGENT_INFO[agent_name][0], AGENT_INFO[agent_name][1])) for agent_name in AGENT_INFO.keys()]

        for thread in threads:
            thread.start()
        
        game.join()
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



create_many_bootstrap(3, "testingBootstrapGames")