from os import path, mkdir
from joblib import dump, load

class Model:
    def __init__(self, name, model):
        self.name = name
        self.model = model
        self.save_model()

    def single_sample_train(self, data):
        self.model = Model.load_model(self.name)
        self.train(data)
        self.save_model()
    
    def single_sample_predict(self, data):
        self.model = Model.load_model(self.name)
        return self.predict(data)

    def train(self, data):
        if "train" not in self.model.__dir__():
            raise("The model should implement train method")

        self.model.train(data)
    
    def predict(self, data):
        if "predict" not in self.model.__dir__():
            raise("The model should implement predict method")

        return self.model.predict(data)
    
    def save_model(self):
        file_path = f'WholesalePred/model_data/{self.name}'

        if not path.exists(file_path):
            mkdir(file_path)

        dump(self.model, file_path + '/data')

    @staticmethod
    def load_model(model_name):
        return load(f'WholesalePred/model_data/{model_name}/data')
