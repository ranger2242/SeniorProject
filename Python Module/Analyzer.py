from GP import GP
from Helpers import get_project_root
project_root = get_project_root()

class Analyzer:

    def __init__(self):
        self.gp_network = GP()

    def use_neural_networks(self, data):
        print('Use neural networks || Not yet implemented...')

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

    def test_network(self):
        return None


a = Analyzer()
a.plot_CSV_data('Python Module\\labeled_dataset.csv')

# a.train_network_from_csv()

# Generate a test matrix from testing
def generate_test_matrix(x):
    mat = []
    for i in range(500):
        t = [0] * x
        t.append(i)
        while len(t) != 4:
            t.append(0)
        mat.append(t)
    return mat

# for j in range(4):
#     test_matrix = generate_test_matrix(j)
#     a.gp_network.test_plot(test_matrix)


# a.train_network_from_csv()
# print('Training complete')


