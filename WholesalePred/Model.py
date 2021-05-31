from os import path, mkdir
from joblib import dump, load

class Model:
    def __init__(self, name, model):
        self.name = name
        self.model = model
        self.save_model()

    def sample_train(self, data_x, data_y):
        self.model = Model.load_model(self.name)
        self.train(data_x, data_y)
        self.save_model()
    
    def single_sample_predict(self, data):
        self.model = Model.load_model(self.name)
        return self.predict(data)

    def train(self, data_x, data_y):
        if "train" not in dir(self.model):
            raise("The model should implement train method")

        self.model.train(data_x, data_y)
    
    def predict(self, data):
        if "predict" not in dir(self.model):
            raise("The model should implement predict method")

        return self.model.predict(data)

    def get_error(self,real, prediction):
        if "get_error" not in dir(self.model):
            raise("The model should implement get_error method")

        return self.model.get_error(real, prediction)
    
    def save_model(self):
        file_path = f'WholesalePred/model_data/{self.name}'

        if not path.exists(file_path):
            mkdir(file_path)

        dump(self.model, file_path + '/data')

    def train_csv(self):
        if "train_csv" not in dir(self.model):
            raise("The model should implement train_csv method")
            
        file_path = 'WholesalePred/data.csv'
        self.model.train_csv(file_path)

    @staticmethod
    def load_model(model_name):
        return load(f'WholesalePred/model_data/{model_name}/data')
