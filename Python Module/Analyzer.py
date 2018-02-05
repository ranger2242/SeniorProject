from GP import GP
import os


def get_project_root():
    project_root = os.path.abspath(os.path.dirname(__file__))
    while not project_root.endswith('\\'):
        project_root = project_root[:-1]
    project_root = project_root[:-1]
    return project_root


project_root = get_project_root()


class Analyzer:

    def __init__(self):
        self.gp_network = GP()

    def use_neural_networks(self, data):
        print('Use neural networks || Not yet implemented...')

    def train_network_from_csv(self):
        data_set = open(project_root + '\\training_dataset.csv', 'r')
        data_set = self.gp_network.csv_processing(data_set)
        self.gp_network.train(data_set)

    def plot_CSV_data(self):
        data_set = open(project_root + '\\training_dataset.csv', 'r')
        data_set = self.gp_network.csv_processing(data_set)
        self.gp_network.plot(data_set)

    def test_network(self):
        return None


a = Analyzer()
a.gp_network.plot_reference_points()

# a.train_network_from_csv()
# print('Training complete')
# a.plot_CSV_data()


