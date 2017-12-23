
import tensorflow as tf
import numpy as np


number_nodes_input = 1000
number_nodes_hl1 = 500
number_nodes_hl2 = 500
number_nodes_hl3 = 500
number_nodes_output = 2

batch_size = 100

x = tf.placeholder('float', [None, number_nodes_input])
y = tf.placeholder('float')

def neural_network_model(data):
    hidden_1_layer = {'weights': tf.Variable(tf.random_normal([number_nodes_input, number_nodes_hl1])), 'biases': tf.Variable(tf.random_normal([number_nodes_hl1]))}

    hidden_2_layer = {'weights': tf.Variable(tf.random_normal([number_nodes_hl1, number_nodes_hl2])), 'biases': tf.Variable(tf.random_normal([number_nodes_hl2]))}

    hidden_3_layer = {'weights': tf.Variable(tf.random_normal([number_nodes_hl2, number_nodes_hl3])), 'biases': tf.Variable(tf.random_normal([number_nodes_hl3]))}

    output_layer = {'weights': tf.Variable(tf.random_normal([number_nodes_hl3, number_nodes_output])), 'biases': tf.Variable(tf.random_normal([number_nodes_output]))}

    layer1 = tf.add(tf.matmul(data, hidden_1_layer['weights']), hidden_1_layer["biases"])
    layer1 = tf.nn.relu(layer1)

    layer2 = tf.add(tf.matmul(layer1, hidden_2_layer['weights']), hidden_2_layer["biases"])
    layer2 = tf.nn.relu(layer2)

    layer3 = tf.add(tf.matmul(layer2, hidden_3_layer['weights']), hidden_3_layer["biases"])
    layer3 = tf.nn.relu(layer3)

    output = tf.add(tf.matmul(layer3, output_layer['weights']), output_layer["biases"])
    return output


def train_neural_network(x):
    prediction = neural_network_model(x)
    cost = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=prediction, labels=y))
    optimizer = tf.train.AdamOptimizer().minimize(cost)

    hm_epochs = 10

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())

        # for epoch in range(hm_epochs):
        #     epoch_loss = 0
        #     for _ in range(int(mnist.train.num_examples/batch_size)):
        #         epoch_x, epoch_y = mnist.train.next_batch(batch_size=batch_size)
        #         _, c = sess.run([optimizer, cost], feed_dict={x: epoch_x, y: epoch_y})
        #         epoch_loss += c
        #     print("Training Cycle:", epoch + 1, "  Loss:", epoch_loss)


        # correct = tf.equal(tf.argmax(prediction, 1), tf.argmax(y, 1))
        # accuracy = tf.reduce_mean(tf.cast(correct, 'float'))
        # print('Accuracy:', accuracy.eval({x: mnist.test.images, y: mnist.test.labels}))

        # print("Saving...")
        # saver = tf.train.Saver()
        # saver.save(sess, 'C:\\Users\\Ross\\Desktop\\Numbers\\model.ckpt')
        # print("Model Saved!")



def use_neural_network(data):
    prediction = neural_network_model(x)

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        # saver = tf.train.Saver()
        # saver.restore(sess, "model.ckpt")

        result = (sess.run(tf.argmax(prediction.eval(feed_dict={x: [data]}), 1)))
        print(result)


