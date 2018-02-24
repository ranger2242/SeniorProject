from SOMObject import SOM
import os
import matplotlib.pyplot as plt
import numpy as np
from Helpers import shuffle_data
import sys

project_root = os.path.abspath(os.path.dirname(__file__))

'''
Data Model from Client

data: [
    number of refernces to other classes,
    number of functions in class, 
    number of varibles in class,
    ratio of total referecnes to referecnes to class,
    Optional: label with 1 or 0 to indicate God Class
]

'''


class GP:

    def __init__(self):
        m = 50
        n = 50
        input_size = 4
        iterations = 30
        model_name = 'GP_model.ckpt'

        self.som = SOM(m, n, input_size, iterations, model_name=model_name)

        if self.som.trained is True:
            self.map_network()
            print('References mapped')
        print('\t--- Map Created ---\n')

    # Converts CSV data into raw data ready to be preprocessed
    def csv_processing(self, raw_data, shuffle=False):

        data = []
        labels = []
        for line in raw_data.readlines():
            elements = line.split(',')
            num_ref_out = float(elements[0])
            num_funcs = float(elements[1])
            num_vars = float(elements[2])
            ratio_total_out = float(elements[3])

            label = None
            if len(elements) > 4:
                if elements[4] is not '\n':
                    label = int(elements[4])

            data_entry = [ num_ref_out, num_funcs, num_vars, ratio_total_out]
            data.append(data_entry)
            labels.append(label)

        if shuffle is True:
            data, labels = shuffle_data(data, labels)

        return data, labels


    # Does any preprocessing needed.. currently unimplemented
    def preprocessing(self, raw_data, from_client=False):
        if from_client:
            return raw_data[:4]
        return raw_data

    # Trains the network on data provided
    def train(self, raw_data):
        data, _ = self.preprocessing(raw_data)
        self.som.train(data)

    # Maps points from a labeled CSV file as refernces points
    def map_network(self):
        print(project_root)
        labeled_data_set = open(project_root + '\\labeled_dataset.csv', 'r')
        labeled_data, labels = self.csv_processing(labeled_data_set)
        labeled_data = self.preprocessing(labeled_data)
        self.som.map_references_points(labeled_data, labels)

    # Makes a predication on a data element
    def make_prediction(self, raw_data):
        data = self.preprocessing(raw_data)
        return self.som.make_prediction(data)

    def map_vectors(self, data, logging=True):
        # Fit train data into SOM lattice
        mapped = self.som.map_vects(data)
        if logging:
            print('Mapped ->', end=' ')
        mappedarr = np.array(mapped)
        if logging:
            print('Tranformed ->', end=' ')
        x1 = mappedarr[:, 0]
        if logging:
            print('X mapped ->', end=' ')
        y1 = mappedarr[:, 1]
        if logging:
            print('Y mapped')
        return x1, y1

    # Plots data give. Optional: Pass labels in for color formatting
    def plot(self, data, labels=None):

        print('Preparing data to be plotted...')
        x1, y1 = self.map_vectors(data)

        print('Plotting data points...')
        plt.figure(1, figsize=(12, 6))
        plt.subplot(121)

        for i in range(len(x1)):
            color = (0, 1, 0)
            if labels is not None:
                if len(labels) > i:
                    if labels[i] == 1:
                        color = (1, 0, 0)
                    elif labels[i] == 0:
                        color = (0, 0, 0)
                    else:
                        color = (0, 1, 0)


            plt.scatter(x1[i], y1[i], c=color)

        plt.axis([0.0, 50.0, 0.0, 50.0])
        plt.title('Data')
        plt.show()

    # Plots map refernces points assigned by the map_network function
    def plot_reference_points(self):
        labeled_data = self.som.references_points
        labels = self.som.labels
        self.plot(labeled_data, labels)

    # Testing plot method used to visualize regions on the map
    def test_plot(self, data):
        print('Preparing data to be plotted...')
        x1 , y1 = self.map_vectors(data)

        print('Plotting data points...')
        plt.figure(1, figsize=(12, 6))
        plt.subplot(121)

        for i in range(len(x1)):
            normalized_i = float(i / len(x1))
            color = (normalized_i, normalized_i, normalized_i)
            plt.scatter(x1[i], y1[i], c=color)

        plt.axis([0.0, 50.0, 0.0, 50.0])
        plt.title('Data')
        plt.show()
