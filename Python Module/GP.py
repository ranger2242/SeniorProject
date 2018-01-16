import tensorflow as tf


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

# This model is obsolete...

class GP:

    number_of_input_nodes = 5
    number_of_hidden_layer_1_nodes = 100
    number_of_hidden_layer_2_nodes = 100
    number_of_hidden_layer_3_nodes = 100
    number_of_output_nodes = 2

    def __init__(self, data):
        tf.initialize_all_variables()
        self.data = data


    def preprocessing(self, data):
        # do any nessary preprocessing here
        return data


    def run(self):
        input_data = self.preprocessing(self.data)
        predication = self.network_model(input_data)

        sess = tf.Session()


        # Do network stuff here

    def network_model(self, data):

        hidden_layer_1 = { 'weights': tf.Variable(tf.random_normal([self.number_of_input_nodes, self.number_of_hidden_layer_1_nodes])),
            'biases': tf.Variable(tf.random_normal([self.number_of_hidden_layer_1_nodes]))}

        hidden_layer_2 = {'weights': tf.Variable(tf.random_normal([self.number_of_hidden_layer_1_nodes, self.number_of_hidden_layer_2_nodes])),
            'biases': tf.Variable(tf.random_normal([self.number_of_hidden_layer_2_nodes]))}

        hidden_layer_3 = { 'weights': tf.Variable(tf.random_normal([self.number_of_hidden_layer_2_nodes, self.number_of_hidden_layer_3_nodes])),
            'biases': tf.Variable(tf.random_normal([self.number_of_hidden_layer_3_nodes]))}

        output_layer = {'weights': tf.Variable(tf.random_normal([self.number_of_hidden_layer_3_nodes, self.number_of_output_nodes])),
            'biases': tf.Variable(tf.random_normal([self.number_of_output_nodes]))}

        hidden_1 = tf.add(tf.matmul(data, hidden_layer_1['weights']), hidden_layer_1["biases"])
        hidden_1 = tf.nn.relu(hidden_1)

        hidden_2 = tf.add(tf.matmul(hidden_1, hidden_layer_2['weights']), hidden_layer_2["biases"])
        hidden_2 = tf.nn.relu(hidden_2)

        hidden_3 = tf.add(tf.matmul(hidden_2, hidden_layer_3['weights']), hidden_layer_3["biases"])
        hidden_3 = tf.nn.relu(hidden_3)

        output = tf.add(tf.matmul(hidden_3, output_layer['weights']), output_layer["biases"])
        return output






