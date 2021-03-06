import tensorflow as tf
import numpy as np
from _datetime import datetime
import math
import sys
from Helpers import get_python_project_root
from Helpers import slog
import os

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'


class SOM(object):
    # To check if the SOM has been trained
    trained = False

    def __init__(self, m, n, dim, n_iterations=100, alpha=None, sigma=None, model_name='model.ckpt'):

        slog('\t--- Creating Map ---')

        # Assign required variables first
        self.m = m
        self.n = n
        self.time_stamp = None
        self.model_name = model_name
        self.references_points = None
        self.labels = None

        if alpha is None:
            alpha = 0.2
        else:
            alpha = float(alpha)
        if sigma is None:
            sigma = max(m, n) / 2.0
        else:
            sigma = float(sigma)
        self.n_iterations = abs(int(n_iterations))

        self.graph = tf.Graph()

        with self.graph.as_default():

            # To save data, create weight vectors and their location vectors
            self.weightage_vects = tf.Variable(tf.random_normal([m * n, dim]), name='weightage_vects')

            self.location_vects = tf.constant(np.array(list(self.neuron_locations(m, n))), name='location_vects')

            # Training inputs
            # The training vector
            self.vect_input = tf.placeholder("float", [dim], name='vect_input')
            # Iteration number
            self.iter_input = tf.placeholder("float", name='iter_input')

            # Training Operation  # tf.stack result will be [ (m*n),  dim ]
            bmu_index = tf.argmin(tf.sqrt(tf.reduce_sum(
                tf.pow(tf.subtract(self.weightage_vects, tf.stack(
                    [self.vect_input for _ in range(m * n)])), 2), 1)), 0)

            slice_input = tf.pad(tf.reshape(bmu_index, [1]), np.array([[0, 1]]))
            bmu_loc = tf.reshape(
                tf.slice(self.location_vects, slice_input, tf.constant(np.array([1, 2]), dtype=tf.int64)), [2])


            # To compute the alpha and sigma values based on iteration number
            learning_rate_op = tf.subtract(1.0, tf.div(self.iter_input, self.n_iterations))
            alpha_op = tf.multiply(alpha, learning_rate_op)
            sigma_op = tf.multiply(sigma, learning_rate_op)


            # learning rates for all neurons, based on iteration number and location w.r.t. BMU.
            bmu_distance_squares = tf.reduce_sum(tf.pow(tf.subtract(
                self.location_vects, tf.stack([bmu_loc for _ in range(m * n)])), 2), 1)


            neighbourhood_func = tf.exp(tf.negative(tf.div(tf.cast(
                bmu_distance_squares, "float32"), tf.pow(sigma_op, 2))))
            learning_rate_op = tf.multiply(alpha_op, neighbourhood_func)

            # Finally, the op that will use learning_rate_op to update the weightage vectors of all neurons
            to_stack = []
            loading_seg = int((m * n) / 10)
            for i in range(m*n):
                to_stack.append(tf.tile(tf.slice(learning_rate_op, np.array([i]), np.array([1])), [dim]))

                if (i + 1) % loading_seg == 0:

                    num_stars = int((i + 1) / loading_seg)
                    message = '\t['

                    for j in range(num_stars):
                        message += '*'
                    for j in range(14 - num_stars):
                        message += ' '

                    message += ']'

                    slog(message, replace=True)

            learning_rate_multiplier = tf.stack(to_stack)

            # Calculate learning rate Backup
            # learning_rate_multiplier = tf.stack([tf.tile(tf.slice(
            #     learning_rate_op, np.array([i]), np.array([1])), [dim]) for i in range(m * n)])

            ### Strucutre of updating weight ###
            ### W(t+1) = W(t) + W_delta ###
            ### wherer, W_delta = L(t) * ( V(t)-W(t) ) ###

            # W_delta = L(t) * ( V(t)-W(t) )
            weightage_delta = tf.multiply(
                learning_rate_multiplier,
                tf.subtract(tf.stack([self.vect_input for _ in range(m * n)]), self.weightage_vects))
            slog('\t[***********   ]', replace=True)
            # W(t+1) = W(t) + W_delta
            new_weightages_op = tf.add(self.weightage_vects, weightage_delta)
            slog('[************  ]', replace=True)
            # Update weightge_vects by assigning new_weightages_op to it.
            self.training_op = tf.assign(self.weightage_vects, new_weightages_op)
            self.sess = tf.Session()
            self.saver = tf.train.Saver()
            slog('\t[************* ]', replace=True)
            try:
                self.saver.restore(self.sess, 'tmp\\' + self.model_name)
                self.store_centroid_grid()
                self.trained = True
                slog('\t[**************]', replace=True)
                print()
                slog('Model loaded from checkpoint')
            except:
                init_op = tf.global_variables_initializer()
                self.sess.run(init_op)
                slog('\t[**************]', replace=True)
                print()

    def neuron_locations(self, m, n):
        for i in range(m):
            for j in range(n):
                yield np.array([i, j])

    def train(self, input_vects):
        slog('Training Beginning')
        self.time_stamp = datetime.now()
        # Training iterations
        for iter_no in range(self.n_iterations):
            self.print_time_remaining(iter_no)
            # Train with each vector one by one
            for i in range(len(input_vects)):
                sys.stdout.write("\rCycle: %i - %i out of %i" % (iter_no, i, len(input_vects)))
                sys.stdout.flush()
                self.sess.run(self.training_op, feed_dict={self.vect_input: input_vects[i], self.iter_input: iter_no})
            self.save_model(self.sess)

        self.store_centroid_grid()
        self.trained = True
        slog('Training complete. %i Iteration' % self.n_iterations)

    def store_centroid_grid(self):
        # Store a centroid grid for easy retrieval later on
        centroid_grid = [[] for i in range(self.m)]
        self.weightages = list(self.sess.run(self.weightage_vects))
        self.locations = list(self.sess.run(self.location_vects))
        for i, loc in enumerate(self.locations):
            centroid_grid[loc[0]].append(self.weightages[i])

        self.centroid_grid = centroid_grid

    def get_centroids(self):
        if not self.trained:
            raise ValueError("SOM not trained yet")
        return self.centroid_grid

    def map_vects(self, input_vects, logging=True):

        if not self.trained:
            raise ValueError("SOM not trained yet")

        to_return = []
        k = 0
        for vect in input_vects:
            if logging:
                slog("Mapping: Vector %i out of %i" % (k+1, len(input_vects)), replace=True)
            k += 1
            min_index = min([i for i in range(len(self.weightages))],
                            key=lambda x: np.linalg.norm(vect - self.weightages[x]))
            to_return.append(self.locations[min_index])
        if logging:
            print()  # Formatting (make new line)
        return to_return

    def print_time_remaining(self, cycle_number):
        time_of_last_cycle = datetime.now() - self.time_stamp
        time_left = time_of_last_cycle * (self.n_iterations - cycle_number)
        slog('Cycle:', cycle_number, '- Time Remaining:', time_left, ' - Time of Last Cycle:', time_of_last_cycle)
        self.time_stamp = datetime.now()

    def save_model(self, sess):
        slog("Saving...")
        project_root = get_python_project_root()
        file_path = project_root + '\\tmp\\' + self.model_name
        self.saver.save(sess, file_path)
        slog("Model Saved!")

    def map_references_points(self, data, labels):
        self.references_points = self.map_vects(data)
        self.labels = labels

    def make_prediction(self, input_vector):
        mapped_vector = self.map_vects([input_vector], logging=False)[0]

        distances = []
        for c in self.references_points:
            distances.append(self.calculate_distance(mapped_vector, c))

        min_index = 0
        min1 = 0
        min2 = 0
        for i in range(len(distances)):
            if distances[min_index] > distances[i]:
                min2 = min1
                min1 = min_index
                min_index = i

        if self.labels[min_index] == self.labels[min1]:
            return self.labels[min_index]
        elif self.labels[min_index] == self.labels[min2]:
            return self.labels[min_index]
        else:
            return self.labels[min2]

    def calculate_distance(self, x, y):

        x1 = x[0]
        x2 = y[0]
        y1 = x[1]
        y2 = y[1]

        delta_x = abs(x1 - x2)
        delta_x_squared = delta_x * delta_x

        delta_y = abs(y1 - y2)
        delta_y_squared = delta_y * delta_y

        return math.sqrt(delta_x_squared + delta_y_squared)
