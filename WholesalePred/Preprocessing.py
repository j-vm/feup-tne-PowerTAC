class Preprocessing:
    @staticmethod
    def format_transform(data):
        '''
        This method must take a json-like dictionary and transform what was received into a plain list of features.
        If there are any non-number like inputs, they should be mapped here as well.
        '''
        print(data)

        for k, v in data.items():
            new_v = v
            if type(v) is dict:
                new_v = v.keys()
            
            print(f"{k}: {new_v}")

        
        list_of_features = []