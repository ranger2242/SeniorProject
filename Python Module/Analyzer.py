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

    def test_network(self):
        return None
        # god_test_data = [[3.0, 1.0, 0.0, 0.0, 1.0, 4.0, 0.25, 0.0],
        #                  [3.0, 1.0, 3.0, 1.0, 5.0, 4.0, 0.25, 0.75],
        #                  [3.0, 2.0, 1.0, 6.0, 9.0, 4.0, 0.5, 0.25]]


        # test_data = [[[3, 1, 0, 0, 1], [3, 1, 3, 1, 5], [3, 2, 1, 6, 9]]]
        # run_neural_networks(test_data)

a = Analyzer()
a.train_network_from_csv()