import sys
from utils.cmd_utils import *
from utils.run_for_project_args import *
import csv

PREFIX_TMP = '/tmp/'
FOLDER_PATH_V1 = 'v1'
FOLDER_PATH_V2 = 'v2'
PATH_V1 = PREFIX_TMP + FOLDER_PATH_V1
PATH_V2 = PREFIX_TMP + FOLDER_PATH_V2

def get_tests_to_execute():
    path = PATH_V1 + '/' + VALUE_TEST_LISTS
    tests_to_execute = {}
    with open(path, 'r') as csvfile:
        file = csv.reader(csvfile, delimiter=';')
        for line in file:
            tests_to_execute[line[0]] = line[1:]
    return tests_to_execute

def run(nb_iteration, output_path):
    run_mvn_clean_test(PATH_V1)
    run_mvn_clean_test(PATH_V2)
    code = run_mvn_diff_select(PATH_V1, PATH_V2)
    if not code == 0:
        return -1
    copy(PATH_V1 + '/' + VALUE_TEST_LISTS, output_path + '/' + VALUE_TEST_LISTS)
    code = run_mvn_build_classpath_and_instrument(PATH_V1, PATH_V2)
    if not code == 0:
        return -1
    tests_to_execute = get_tests_to_execute()
    for i in range(nb_iteration):
        print(i)

        run_mvn_test(PATH_V1, tests_to_execute, True)
        v1_result_folder = output_path + '/v1/' + str(i)
        delete_directory(v1_result_folder)
        copy_jjoules_result(PATH_V1, v1_result_folder)

        run_mvn_test(PATH_V2, tests_to_execute, True)
        v2_result_folder = output_path + '/v2/' + str(i)
        delete_directory(v2_result_folder)
        copy_jjoules_result(PATH_V2, v2_result_folder)

    return 0

if __name__ == '__main__':

    args = RunArgs().build_parser().parse_args()

    project_name = args.project_name
    output_path = args.output
    commits_file_path = args.commits
    nb_iteration = int(args.iteration)
    nb_commits = int(args.nb_commits)

    commits = []
    with open(commits_file_path, 'r') as commits_file:
        lines = commits_file.readlines()
        repo_url = lines[0]
        for line in lines[1:]:
            commits.append(line[:-1])

    #commits.reverse()
    delete_directory(PATH_V1)
    delete_directory(PATH_V2)
    clone(repo_url[:-1], PATH_V1)
    clone(repo_url[:-1], PATH_V2)

    create_if_does_not_exist(output_path + project_name)

    current_nb_completed_commits = 0
    cursor_commits = 1

    while current_nb_completed_commits < nb_commits and cursor_commits < len(commits) - 1:
        commit_sha_v1 = commits[cursor_commits]
        commit_sha_v2 = commits[cursor_commits - 1]
        print(commits)
        print('Run for', project_name, commit_sha_v1, cursor_commits, commit_sha_v2, cursor_commits - 1, 'output_path', output_path)
        reset_hard(commit_sha_v1, PATH_V1)
        reset_hard(commit_sha_v2, PATH_V2)
        try:
            mkdir(output_path + '/' + project_name + '/' + commit_sha_v1[:6] + '_' + commit_sha_v2[:6])
        except FileExistsError:
            print('pass...')
        code = run(nb_iteration, output_path + '/' + project_name + '/' + commit_sha_v1[:6] + '_' + commit_sha_v2[:6])
        if code == 0:
            current_nb_completed_commits = current_nb_completed_commits + 1
            print('Success!', current_nb_completed_commits, '/', nb_commits)
        print(cursor_commits, '/', len(commits) - 1)
        cursor_commits = cursor_commits + 1