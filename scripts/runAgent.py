from shutil import copyfile
from os import system

from runGame import run_agent

AGENT_NAME = "DiCaprio"
VERSION = "1.7.0"

system(f"cd {AGENT_NAME} && mvn clean package && cd ..")

from_path = f"{AGENT_NAME}/target/{AGENT_NAME}-{VERSION}.jar"
to_path = f"brokers/{AGENT_NAME}/{AGENT_NAME}-{VERSION}.jar"

copyfile(from_path, to_path)

run_agent(AGENT_NAME)