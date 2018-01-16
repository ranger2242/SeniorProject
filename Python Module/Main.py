from Pipeline import Pipeline
from GP import GP


# ip = "localhost"
# port = "2242"
# pipeline = Pipeline()
# pipeline.connect_to_server(ip, port)


def run_neural_networks( data):
    gp = GP(data[0])
    gp.run()


test_data = [[[3, 1, 0, 0, 1], [3, 1, 3, 1, 5], [3, 2, 1, 6, 9]]]
run_neural_networks(test_data)

