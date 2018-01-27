from Pipeline import Pipeline
from GP import GP


# ip = "localhost"
# port = "2242"
# pipeline = Pipeline()
# pipeline.connect_to_server(ip, port)

gp_network = GP()

god_test_data = [[3.0, 1.0, 0.0, 0.0, 1.0, 4.0, 0.25, 0.0],
                 [3.0, 1.0, 3.0, 1.0, 5.0, 4.0, 0.25, 0.75],
                 [3.0, 2.0, 1.0, 6.0, 9.0, 4.0, 0.5, 0.25]]


gp_network.train(god_test_data)



def run_neural_networks(data):
    print('Hi')

# test_data = [[[3, 1, 0, 0, 1], [3, 1, 3, 1, 5], [3, 2, 1, 6, 9]]]
# run_neural_networks(test_data)

