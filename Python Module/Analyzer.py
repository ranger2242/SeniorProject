from GP import GP
from Helpers import get_project_root
project_root = get_project_root()

class Analyzer:

    def __init__(self):
        self.gp_network = GP()

    def use_neural_networks(self, data):
        prediction = self.gp_network.som.make_prediction(data)

        if prediction == 0:
            print('Not a God Object!')
        else:
            print('God Object Detected!')

    # Trains GP Network from CSV file
    def train_network_from_csv(self):
        data_set = open(project_root + '\\training_dataset.csv', 'r')
        data_set = self.gp_network.csv_processing(data_set, shuffle=True)
        self.gp_network.train(data_set)

    # Plot CSV file data...
    def plot_CSV_data(self, file):
        data_set = open(project_root + '\\' + file, 'r')
        data_set, labels = self.gp_network.csv_processing(data_set)
        self.gp_network.plot(data_set, labels)

    def test_network(self, file):

        data_set = open(project_root + '\\' + file, 'r')
        data_set, labels = self.gp_network.csv_processing(data_set)
        n_checked = 0
        n_correct = 0

        for i in range(len(data_set)):
            prediction = self.gp_network.som.make_prediction([data_set[i]])
            if labels[i] == prediction:
                n_correct += 1
            n_checked += 1

        accuracy = float(n_correct) / float(n_checked)
        print('Accuracy: ' + str(accuracy) + ' - ' + str(n_checked) + ' elements checked, ' + str(n_correct) + ' predicted correctly.')



analyzer = Analyzer()
# analyzer.train_network_from_csv()

# test_data = [[20, 99, 99, 0.658]]
# analyzer.use_neural_networks(test_data)

# analyzer.test_network('Python Module\\labeled_dataset.csv')


analyzer.plot_CSV_data('Python Module\\labeled_dataset.csv')



