import os
import threading
from time import sleep
from runPythonAgent import run_python_agent

PATH_SERVER = "server"
AGENTS = {"default-broker","TUC_TAC"}
PYTHON_AGENTS = {"temporaryName"}
# Key: Agent's name. Value: [Agent's path, Binaries]
AGENT_INFO = {"TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}
PYTHON_AGENT_INFO = {"temporaryName": ["temporaryName", ""]}

SERVER_BOOT_TIME = 10


def run_bootstrap(game_name):
    return_value = os.system("cd .. && cd " + PATH_SERVER + " && mvn -Pcli -Dexec.args=\"--boot " + game_name + " --game-id " + game_name + "\"")
    print(return_value)

def run_game(game_name):
    agent_list = AGENTS.union(PYTHON_AGENTS)
    all_agents = ','.join([str(elem) for elem in agent_list])
    return_value = os.system("cd .. && cd " + PATH_SERVER + " && mvn -X -Pcli -Dexec.args=\"--sim --boot-data " + game_name + " --game-id " + game_name + " --brokers " + all_agents + "\"")
    print(return_value)

def run_agent(agent_name):
    return_value = os.system(f"cd .. && java -jar {AGENT_INFO[agent_name][0]}/{AGENT_INFO[agent_name][1]}")
    print(return_value)


def start_python_agent(agent_name):
    run_python_agent(agent_name, PYTHON_AGENT_INFO[agent_name][0], PYTHON_AGENT_INFO[agent_name][1])


def run_game_and_agents(game_name):
    game = threading.Thread(target=run_game, args=(game_name,))

    agent_threads = [threading.Thread(target=run_agent, args=(agent_name,)) for agent_name in AGENT_INFO.keys()]
    
    game.start()
    sleep(SERVER_BOOT_TIME)

    for agent_thread in agent_threads:
        agent_thread.start()

    for agent in PYTHON_AGENTS:
        start_python_agent(agent)


#run_bootstrap('game1')
run_game_and_agents('game1')