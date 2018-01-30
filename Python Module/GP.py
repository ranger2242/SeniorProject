from SOMObject import SOM
import os
import matplotlib.pyplot as plt
import numpy as np

project_root = os.path.abspath(os.path.dirname(__file__))

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
        iterations = 500
        model_name = 'GP_model.ckpt'

        self.som = SOM(m, n, input_size, iterations, model_name=model_name)

        if self.som.trained is True:
            self.map_network()

    def csv_processing(self, raw_data):

        data = []
        labels = []
        for line in raw_data.readlines():
            elements = line.split(',')
            num_classes = float(elements[0])
            num_ref_in = float(elements[1])
            num_ref_out = float(elements[2])
            num_funcs = float(elements[3])
            num_vars = float(elements[4])
            total_refs = float(elements[5])
            ratio_total_in = float(elements[6])
            ratio_total_out = float(elements[7])

            label = None
            if len(elements) > 8:
                if elements[8] == 'YES':
                    label = 1
                else:
                    label = 0

            data_entry = [num_classes, num_ref_in, num_ref_out, num_funcs, num_vars, total_refs, ratio_total_in, ratio_total_out]
            data.append(data_entry)
            labels.append(label)

        return data, labels

    def preprocessing(self, raw_data):
        return raw_data

    def train(self, raw_data):
        data, _ = self.preprocessing(raw_data)
        self.som.train(data)

    def map_network(self):
        labeled_data_set = open(project_root + '\\god_object_labeled_data.csv', 'r')
        labeled_data, labels = self.csv_processing(labeled_data_set)
        labeled_data = self.preprocessing(labeled_data)
        self.som.map_refernces_points(labeled_data, labels)

    def make_prediction(self, raw_data):
        data = self.preprocessing(raw_data)
        return self.som.make_prediction(data)

    def plot(self, x):
        print('Preparing data to be plotted...')

        # Fit train data into SOM lattice
        mapped = self.som.map_vects(x)
        mappedarr = np.array(mapped)
        x1 = mappedarr[:, 0]
        y1 = mappedarr[:, 1]

        print('Plotting data points...')

        plt.figure(1, figsize=(12, 6))
        plt.subplot(121)

        for i in range(len(x1)):
            color = (0, 0, 0)
            plt.scatter(x1[i], y1[i])

        plt.title('Colors')
        plt.show()
