from runGame import *

BOOTSRAP_FILE = "Game1" 
AGENTS = "temporaryName,TUC_TAC"
AGENT_INFO = {"temporaryName": ["brokers/temporaryName", "temporaryName-1.7.0.jar"],
              "TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}


game = threading.Thread(target=run_game, args=(BOOTSRAP_FILE,AGENTS))
game.start()
threads = [threading.Thread(target=run_agent, args=(agent_name,AGENT_INFO[agent_name][0], AGENT_INFO[agent_name][1])) for agent_name in AGENT_INFO.keys()]

for thread in threads:
    thread.start()