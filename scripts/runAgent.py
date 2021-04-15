from shutil import copyfile
from os import system

#from runGame import run_agent

AGENT_NAME = "temporaryName"
VERSION = "1.7.0"

def compile_and_move(agent_name = AGENT_NAME, version = VERSION):
    system(f"cd {agent_name} && mvn clean package && cd ..")

    from_path = f"{agent_name}/target/{agent_name}-{version}.jar"
    to_path = f"brokers/{agent_name}/{agent_name}-{version}.jar"

    copyfile(from_path, to_path)

#compile_and_move()

#run_agent(AGENT_NAME)