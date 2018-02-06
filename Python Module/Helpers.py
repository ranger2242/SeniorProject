import os

# Gets project root of main folder
def get_project_root():
    project_root = os.path.abspath(os.path.dirname(__file__))
    while not project_root.endswith('\\'):
        project_root = project_root[:-1]
    project_root = project_root[:-1]
    return project_root

def get_python_project_root():
    return os.path.abspath(os.path.dirname(__file__))