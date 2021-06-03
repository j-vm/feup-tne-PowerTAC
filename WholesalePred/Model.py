from os import path, mkdir
from joblib import dump, load

class Model:
    def __init__(self, name, model):
        self.name = name
        self.model = model
        self.save_model()
        self.predictions = []
        self.real_values = []

    def sample_train(self, data_x, data_y):
        self.model = Model.load_model(self.name)
        self.train(data_x, data_y)
        self.save_model()
    
    def sample_predict(self, data):
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

    def get_total_error(self):
        if self.real_values is None or self.predictions is None:
            print("real_values or self.predictions is empty")
            return None
        print("Total Error:")
        return self.model.get_error(self.real_values, self.predictions)

    def get_error(self,real, prediction):
        if "get_error" not in dir(self.model):
            raise("The model should implement get_error method")
        self.real_values.append(real)
        self.predictions.append(prediction)
        return self.model.get_error(real, prediction)
    
    def save_model(self):
        file_path = f'WholesalePred/model_data/{self.name}'

        if not path.exists(file_path):
            mkdir(file_path)

        dump(self.model, file_path + '/data')

    def train_csv(self,file_path):
        if "train_csv" not in dir(self.model):
            raise("The model should implement train_csv method")
        
        self.model = Model.load_model(self.name)
        self.model.train_csv(file_path)
        self.save_model()

    @staticmethod
    def load_model(model_name):
        return load(f'WholesalePred/model_data/{model_name}/data')
