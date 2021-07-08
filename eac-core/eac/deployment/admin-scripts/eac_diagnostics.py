#!/usr/bin/python
from sys import stdout
import erights
import urllib2
import logging
import sys
import os
import subprocess
import time
import socket
from suds.client import Client
from suds.wsse import Security, UsernameToken
logging.basicConfig(filename="/local/home/custadm/eac_diagnostics/eac_diagnostics.log")

class BaseConfig(object):
    
    def __init__(self, name, host, base_url, ws_user, ws_pass):
        self.name = name
        self.host = host
        self.base_url = base_url
        self.version_url = base_url + "/eac/version.htm"
        self.wsdl = base_url + "/eacWebService/services/access-service-v2.0.wsdl"
        self._ws_user = ws_user
        self._ws_pass = ws_pass
        
        
    @property
    def factory(self):
        if not hasattr(self, "_client"):
            self._create_client(self.wsdl, self._ws_user, self._ws_pass)
        return self._client.factory
        
    @property
    def service(self):
        if not hasattr(self, "_client"):
            self._create_client(self.wsdl, self._ws_user, self._ws_pass)
        return self._client.service
    
    def _create_client(self, wsdl, ws_user, ws_pass):
        client = Client(wsdl)
        security = Security()
        token = UsernameToken(ws_user, ws_pass)
        security.tokens.append(token)
        client.set_options(wsse=security)
        self._client = client        
        
        
class EACEnvironment(BaseConfig):
    
    def __init__(self, name, host, base_url, db_host, ws_user, ws_pass, *nodes):
        super(EACEnvironment, self).__init__(name, host, base_url, ws_user, ws_pass)
        self.db_host = db_host
        self.nodes = nodes
        
        
DEV = EACEnvironment("EAC DEV", "dev.eac.uk.oup.com", "http://dev.eac.uk.oup.com", "10.114.63.144", "admin", "Passw0rd")
UAT = EACEnvironment("EAC UAT", "access.uat.oup.com", "https://access.uat.oup.com", "10.114.63.144", "admin", "Passw0rd",
                     BaseConfig("EAC UAT 1", "10.114.63.142", "http://10.114.63.142:8080", "admin", "Passw0rd"), 
                     BaseConfig("EAC UAT 2", "10.114.63.143", "http://10.114.63.143:8080", "admin", "Passw0rd"))
PROD = EACEnvironment("EAC PROD", "access.oup.com", "https://access.oup.com", "10.114.63.145", "admin", "0n$tr4ng3r$t1d3",
                      BaseConfig("EAC PROD 1", "10.114.63.140", "http://10.114.63.140:8080", "admin", "0n$tr4ng3r$t1d3"), 
                      BaseConfig("EAC PROD 2", "10.114.63.141", "http://10.114.63.141:8080", "admin", "0n$tr4ng3r$t1d3"))
EAC_ENVIRONMENTS = {'dev': DEV, 'uat': UAT, 'prod': PROD}

def check_rightcare(env_names):
    for env_name in env_names:
        environment = erights.environments.get(env_name)
        if environment is erights.dev:
            stdout.write("Checking RightCare %s %s ...... " % (environment.name, environment.rightcare_url))
            try:
                resp = urllib2.urlopen(environment.rightcare_url)
                if resp.code != 200:
                    _write_fail()
                else:
                    data = resp.read()
                    if 'Please Log Into eRights' in data:
                        _write_ok()
                    else:
                        _write_fail()
            except urllib2.URLError, e:
                _write_fail(e)
            print("")
            
def check_erights(env_names, use_proxy=False):
    prod_ids = {
        erights.dev: 46862,
        erights.uat: 27401,
        erights.prod: 12001}
    for env_name in env_names:
        environment = erights.environments.get(env_name)
        if environment:
            stdout.write("Checking eRights %s %s ...... " % (environment.name, environment.wsdl))
            try:
                prod_id = prod_ids[environment]
                response = environment.service.getProduct(prod_id)
                if response.status == "SUCCESS":
                    _write_ok()
                else:
                    _write_fail()
            except Exception, e:
                _write_fail(e)
            print("")

def check_eac_web(env_names, version=None, use_proxy=False):
    check_str = "EAC Version: "
    if (version):
        if not isinstance(version, str):
            version = str(version)
        check_str += version
    for env_name in env_names:
        environment = EAC_ENVIRONMENTS.get(env_name)
        if environment:
            proxy_handler = None
            if use_proxy:
                proxy_handler = urllib2.ProxyHandler({"http": "http://prx02.uk.oup.com:8080/array.dll?Get.Routing.Script"})
            urlopener = urllib2.build_opener(proxy_handler)
            if environment.nodes:
                for node in environment.nodes:
                    stdout.write("Checking %s Web %s ...... " % (node.name, node.version_url))
                    _make_web_request(urlopener, node.version_url, check_str)
            else:
                stdout.write("Checking %s Web %s ...... " % (environment.name, environment.version_url))
                _make_web_request(urlopener, environment.version_url, check_str)
                
def check_adapter(env_names):
    for env_name in env_names:
        environment = erights.environments.get(env_name)
        if environment:
            stdout.write("Checking Atypon %s adapter traffic ...... " % (environment.name))
            try:
                f = open('/local/home/custadm/eac_diagnostics/erights-req.bin', 'rb')
                req = []
                b = f.read(1)
                while b != "":
                    req.append(b)
                    b = f.read(1)
                f.close()
                sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                sock.connect((environment.adapter_host, environment.adapter_port))
                data = ''.join(i for i in req)
                sock.send(data)
                resp = sock.recv(1024)
                sock.close()
                if 'decision=redirect' in repr(resp):
                    _write_ok()
                else:
                    _write_fail()
            except Exception, e:
                _write_fail(e)
            print("")
        
def check_eac_ws(env_names):
    for env_name in env_names:
        environment = EAC_ENVIRONMENTS.get(env_name)
        if environment:
            if environment.nodes:
                for node in environment.nodes:
                    _search_activation_code(node)
            else:
                _search_activation_code(environment)
    
def _search_activation_code(nodeOrEnvironment):
    stdout.write("Checking %s WS %s ...... " % (nodeOrEnvironment.name, nodeOrEnvironment.wsdl))
#    nodeOrEnvironment.service
#    print(nodeOrEnvironment._client)
    try:
        response = nodeOrEnvironment.service.SearchActivationCode(activationCode='609912669018', likeMatch='false')
        if hasattr(response, 'activationCodeInfo'):
            _write_ok()
        else:
            _write_fail()
    except Exception, e:
        _write_fail(e)
    print("")
    
def _make_web_request(urlopener, url, check_str=None):
    try:
        resp = urlopener.open(url)
        if resp.code != 200:
            _write_fail()
        elif check_str is not None:
            data = resp.read()
            if check_str in data:
                _write_ok()
            else:
                _write_fail()
        else:
            _write_ok()
        resp.close()
    except urllib2.URLError, e:
        _write_fail(e)
    print("")
        
def _write_ok():
    stdout.write("[ OK ]")
    
def _write_fail(exception=None):
    stdout.write("[ FAIL ]")
    if exception:
        logging.exception(exception)
    
    
if __name__ == '__main__':
    env_names = sys.argv[1].split(',')
    version = None
    if len(sys.argv) > 2:
        version = sys.argv[2]
        
    use_proxy = os.name == 'nt'
    check_rightcare(env_names)
    check_erights(env_names, use_proxy)
    check_adapter(env_names)
    check_eac_web(env_names, version, use_proxy)
    check_eac_ws(env_names)