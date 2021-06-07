import os
import threading
from time import sleep

PATH_SERVER = "server"
AGENTS = "default-broker,TUC_TAC,DiCaprio"
# Key: Agent's name. Value: [Agent's path, Binaries]
# AGENT_INFO = {"TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"],
#               "DiCaprio": ["brokers/DiCaprio", "DiCaprio-1.7.0.jar"],
#               }
AGENT_INFO = {"DiCaprio": ["brokers/DiCaprio", "DiCaprio-1.7.0.jar"]
              }

SERVER_BOOT_TIME = 10


def run_bootstrap(game_name):
    return_value = os.system("cd " + PATH_SERVER + " && mvn -Pcli -Dexec.args=\"--boot " + game_name + " --game-id " + game_name + "\"")
    print(f"Bootstrap for {game_name} thread returned {return_value}")

def run_game(game_name):
    return_value = os.system("cd " + PATH_SERVER + " && mvn -X -Pcli -Dexec.args=\"--sim --boot-data " + game_name + " --game-id " + game_name + " --brokers " + AGENTS + "\"")
    print(f"Run game for {game_name} thread returned {return_value}")

def run_agent(agent_name):
    sleep(SERVER_BOOT_TIME)
    return_value = os.system(f"java -jar {AGENT_INFO[agent_name][0]}/{AGENT_INFO[agent_name][1]}")
    print(f"{agent_name} thread returned {return_value}")

def run_game_and_agents(game_name):
    game = threading.Thread(target=run_game, args=(game_name,))
    threads = [threading.Thread(target=run_agent, args=(agent_name,)) for agent_name in AGENT_INFO.keys()]
    threads.append(game)

    for thread in threads:
        thread.start()

# game_name = "bootstrap"
# # run_bootstrap(game_name)

# run_game_and_agents(game_name)

# run_agent("DiCaprio")