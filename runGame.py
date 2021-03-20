import os
import threading
import time

PATH_SERVER = "server-distribution-master"
PATH_BROKERS = "brokers/TUC_TAC"
AGENTS = "default-broker,TUC_TAC"


def run_bootstrap(name):
    return_value = os.system("cd " + PATH_SERVER + " && mvn -Pcli -Dexec.args=\"--boot " + name + " --game-id " + name + "\"")

def run_game(name):
    return_value = os.system("cd " + PATH_SERVER + " && mvn -Pcli -Dexec.args=\"--sim --boot-data " + name + " --brokers " + AGENTS + "\"")

def run_TUC_TAC():
    return_value = os.system("cd " + PATH_BROKERS + " java -jar TUC_TAC_2020.jar")


game_name = "Game1"

run_bootstrap(game_name)

game = threading.Thread(target=run_game, args=(game_name,))

game.start()

TUC_TAC = threading.Thread(target=run_TUC_TAC)
TUC_TAC.start()