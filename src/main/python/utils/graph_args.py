import argparse
from enum import Enum

class Mode(Enum):
    per_class = 'per_class'
    per_test = 'per_test'

    def __str__(self):
        return self.value

class RunArgs():

    def build_parser(self):

        parser = argparse.ArgumentParser()
        parser.add_argument('-p', '--project-name', type=str, help='Specify the name of the project. Will be used for output purpose')
        parser.add_argument('-d', '--data-path', type=str, help='Specify the path to the data folder.')
        parser.add_argument('mode', type=Mode, choices=list(Mode))

        return parser