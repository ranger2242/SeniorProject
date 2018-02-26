import os
import random
import _datetime as datetime
import sys

# Gets project root of main folder
def get_project_root():
    project_root = os.path.abspath(os.path.dirname(__file__))
    while not project_root.endswith('\\'):
        project_root = project_root[:-1]
    project_root = project_root[:-1]
    return project_root


def get_python_project_root():
    return os.path.abspath(os.path.dirname(__file__))


def shuffle_data(array1, array2):
    n = len(array1)
    for i in range(n - 1, 0, -1):
        j = random.randint(0, i)
        array1[i], array1[j] = array1[j], array1[i]
        array2[i], array2[j] = array2[j], array2[i]
    return array1, array2


def slog(message):
    fmt = '%Y-%m-%d::%H:%M:%S: '
    date_string = datetime.datetime.now().strftime(fmt)
    print(date_string + message)

def slog_replace(message):
    fmt = '%Y-%m-%d::%H:%M:%S: '
    date_string = datetime.datetime.now().strftime(fmt)
    sys.stdout.write('\r' + date_string + message)
    sys.stdout.flush()
