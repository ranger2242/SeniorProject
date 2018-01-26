from SOMObject import SOM

'''
Data Model from Client

data: [
    number of classes,
    number of refernces from other classes,
    number of refernces to other classes,
    number of functions in class, 
    number of varibles in class,
    total referecnes,
    ratio of total referecnes to referecnes to class,
    ratio of total referecnes to referecnes from class
]

'''


class GP:

    def __init__(self):
        m = 50
        n = 50
        input_size = 8
        iterations = 50

        self.som = SOM(m, n, input_size, iterations)

        if self.som.trained is True:
            self.map_network()

    def preprocessing(self, raw_data):
        # Do any preprocessing here :)
        return raw_data

    def train(self, raw_data):
        if self.som.trained is False:
            data = self.preprocessing(raw_data)
            self.som.train(data)
        else:
            print('Network Already Trained...')

    def map_network(self):
        print('Mapping not yet implemented')
        # some_mapping_points = []
        # self.som.map_colors(some_mapping_points)

    def make_prediction(self, raw_data):
        data = self.preprocessing(raw_data)
        return self.som.make_prediction(data)




