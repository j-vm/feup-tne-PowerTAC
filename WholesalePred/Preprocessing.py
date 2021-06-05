from WholesalePred.NoPrice import NoPrice
import csv

class Preprocessing:
    @staticmethod
    def format_transform(data):
        '''
        This method must take a json-like dictionary and transform what was received into a plain list of features.
        If there are any non-number like inputs, they should be mapped here as well.
        '''

        X_list = []
        y_list = []
        
        for k, v in data.items():
            if k == 'ListObjects':
                if v['clearedTradeJson'][0]['executionPrice'] == "none":
                    raise(NoPrice())

                exec_price = v['clearedTradeJson'][0]['executionPrice'] 

                y_list.append(exec_price)

            elif k == 'noCompetitors':
                number_competitors = v
            elif k == 'noCustomers':
                number_customers = v
            
            elif k == 'SingleObjects':
                temperature = v['weatherJson']['temperature']
                cloud_cover = v['weatherJson']['cloudCover']
                wind_direction = v['weatherJson']['windDirection']
                wind_speed = v['weatherJson']['windSpeed']

                weather_forecast_data = []
                
                for el in v['weatherForecastJson']['prediction']:
                    weather_forecast_data.append(el['temperature'])
                    weather_forecast_data.append(el['cloudCover'])
                    weather_forecast_data.append(el['windDirection'])
                    weather_forecast_data.append(el['windSpeed'])

        temp_list = []
        temp_list.append(number_competitors)
        temp_list.append(number_customers)
        temp_list.append(temperature)
        temp_list.append(cloud_cover)
        temp_list.append(wind_direction)
        temp_list.append(wind_speed)
        temp_list += weather_forecast_data

        X_list.append(temp_list)    

        '''
        # save data process
        with open("WholesalePred/data.csv", "a") as f:
            for el in temp_list:
                f.write(f"{el},")
            f.write(f'{exec_price}\n')
        '''

        return X_list, y_list

class PreprocessingClassification:
    @staticmethod
    def format_transform(data):
        '''
        This method must take a json-like dictionary and transform what was received into a plain list of features.
        If there are any non-number like inputs, they should be mapped here as well.
        '''

        X_list = []
        y_list = []
        
        for k, v in data.items():
            if k == 'ListObjects':
                exec_price = 1
                if v['clearedTradeJson'][0]['executionPrice'] == "none":
                    exec_price = 0      # No Trades happen
                
                y_list = [exec_price]

            elif k == 'noCompetitors':
                number_competitors = v
            elif k == 'noCustomers':
                number_customers = v
            
            elif k == 'SingleObjects':
                temperature = v['weatherJson']['temperature']
                cloud_cover = v['weatherJson']['cloudCover']
                wind_direction = v['weatherJson']['windDirection']
                wind_speed = v['weatherJson']['windSpeed']

                weather_forecast_data = []
                
                for el in v['weatherForecastJson']['prediction']:
                    weather_forecast_data.append(el['temperature'])
                    weather_forecast_data.append(el['cloudCover'])
                    weather_forecast_data.append(el['windDirection'])
                    weather_forecast_data.append(el['windSpeed'])

        temp_list = []
        temp_list.append(number_competitors)
        temp_list.append(number_customers)
        temp_list.append(temperature)
        temp_list.append(cloud_cover)
        temp_list.append(wind_direction)
        temp_list.append(wind_speed)
        temp_list += weather_forecast_data

        X_list.append(temp_list)    

        '''
        # save data process
        with open("WholesalePred/dataClassification.csv", "a") as f:
            for el in temp_list:
                f.write(f"{el},")
            f.write(f'{exec_price}\n')
        '''

        return X_list, y_list
