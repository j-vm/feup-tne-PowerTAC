import os
import threading
from time import sleep

PATH_SERVER = "Server"
AGENTS = "TUC_TAC,temporaryName"
# Key: Agent's name. Value: [Agent's path, Binaries]
# AGENT_INFO = {"TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"],
#               "DiCaprio": ["brokers/DiCaprio", "DiCaprio-1.7.0.jar"],
#               }
AGENT_INFO = {"temporaryName": ["brokers/temporaryName", "temporaryName-1.7.0.jar"],
              "TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}

SERVER_BOOT_TIME = 0


def run_bootstrap(game_name):
    return_value = os.system("cd " + PATH_SERVER + " && mvn -Pcli -Dexec.args=\"--boot " + game_name + " --game-id " + game_name + "\"")
    print(f"Bootstrap for {game_name} thread returned {return_value}")

def run_game(game_name, agents = AGENTS):
    return_value = os.system("cd " + PATH_SERVER + " && mvn -Pcli -Dexec.args=\"--sim --boot-data " + game_name + " --game-id " + game_name + " --brokers " + agents + "\"")
    print(f"Run game for {game_name} thread returned {return_value}")

def run_agent(agent_name, agentPath, agentJar, number_of_games):
    sleep(SERVER_BOOT_TIME)
    return_value = os.system(f"java -jar {agentPath}/{agentJar} --repeat-count {str(number_of_games)}")
    print(f"{agent_name} thread returned {return_value}")

def run_game_and_agents(game_name):
    game = threading.Thread(target=run_game, args=(game_name,))
    threads = [threading.Thread(target=run_agent, args=(agent_name,AGENT_INFO[agent_name][0], AGENT_INFO[agent_name][1], 1)) for agent_name in AGENT_INFO.keys()]
    threads.append(game)

    for thread in threads:
        thread.start()

#game_name = "game1"
#run_bootstrap(game_name)

#run_game_and_agents(game_name)

#run_agent("temporaryName")