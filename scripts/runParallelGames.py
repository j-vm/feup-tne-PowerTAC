from os import getcwd, chdir
from os.path import join
from shutil import copytree
from tempfile import TemporaryDirectory

from runGame import *

PATH_SERVER = "server"
PATH_AGENTS = "brokers"
# Key: Agent's name. Value: [Agent's path, Binaries]
AGENT_INFO = {"TUC_TAC": ["brokers/TUC_TAC", "TUC_TAC_2020.jar"]}
PROPERTIES_SERVER = join('server','config','server.properties')


def change_port(path, port):
    with open(path, 'r') as file:
        actual_address = 'tcp://localhost:61616'
        new_address = f'tcp://localhost:{port}'

        raw = file.read()
        processed = raw.replace(actual_address, new_address)

    with open(path, 'w') as file:
        file.flush()
        file.write(processed)


def launch_simulation(port):
    with TemporaryDirectory() as directory:
        new_server_directory = join(directory, PATH_SERVER)
        old_server_directory = join(getcwd(), PATH_SERVER)
        new_agent_directory = join(directory, PATH_AGENTS)
        old_agent_directory = join(getcwd(), PATH_AGENTS)


        copytree(old_server_directory, new_server_directory)
        copytree(old_agent_directory, new_agent_directory)

        change_port(join(directory, PROPERTIES_SERVER), port)

        for agent_value in AGENT_INFO.values():
            path = join(directory, agent_value[0], 'broker.properties')
            change_port(path, port)
        
        chdir(directory)
        run_game_and_agents(f'Game{port}')


launch_simulation(60060)