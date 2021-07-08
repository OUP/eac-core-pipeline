#!/bin/env python
import json
import argparse

def main(file, arr):
 with open(file) as f:
     for line in f:
         if line.startswith('#'):
             pass
         else:
             fields = map(lambda s: s.strip(), line.split('='))
             if len(fields) == 2:
                 arr['EnvVariables'][fields[0]] = fields[1].strip('"').replace("\\/","/").replace("\\","")
                 print('%s=%s' % (fields[0],arr['EnvVariables'][fields[0]]))
             if len(fields) == 3:
                 fields[1] = fields[1] + '='
                 del fields[2]
                 arr['EnvVariables'][fields[0]] = fields[1].strip('"').replace("\\/","/").replace("\\","")
                 print('%s=%s' % (fields[0],arr['EnvVariables'][fields[0]]))

if __name__ == '__main__':
    arr = {}
    arr['EnvVariables'] = {}
    arr['run_list'] = []
    arr['run_list'].append('role_app_shib::postprocess')

    parser = argparse.ArgumentParser()
    parser.add_argument('environment',help='the cloudformation environment to use')
    args = parser.parse_args()

    with open('node.json', 'w+') as nf:
        if args.environment:
            main(file='EnvVariables-%s.properties' % (args.environment), arr=arr)
        else:
            main(file='EnvVariables.properties', arr=arr)
        main(file='Cloudformation.properties', arr=arr)
        json.dump(arr, nf)