from os import path, mkdir
from joblib import dump, load

class Model:
    def __init__(self, name, model):
        self.name = name
        self.model = model
    
    def save_model(self):
        file_path = f'WholesalePred/model_data/{self.name}'

        if not path.exists(file_path):
            mkdir(file_path)

        dump(self.model, file_path + '/data')

    @staticmethod
    def load_model(model_name):
        return load(f'WholesalePred/model_data/{model_name}/data')
