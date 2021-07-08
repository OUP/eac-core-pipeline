'''
Created on 3 Jul 2012

@author: keelinw
'''
from suds.client import Client
from suds.wsse import Security, UsernameToken


def get_resource_paths(environment, args):
    """Prints the paths for the e-rights resources associated with a list of product ids.
    
    Args:
        environment: The e-rights environment to perform the operation against.
        args: A list containing the command arguments. In this case the list
            should contain one element - a comma separated list of product ids.
    
    """
    product_ids = [prod_id.strip() for prod_id in args[0].split(",")]
    for product_id in product_ids:
        product_id = args[0]
        product = _get_product(environment, product_id)
        if product:
            print("%s: %s" % (product_id, [url.path for url in product.urls])) 
    
    
def _get_paths(product):
    paths = []
    for url in product.urls:
        paths.append(url.path)
    return paths    


def delete_products(environment, args):
    """Deletes products from e-rights from a supplied list of product ids.
    
    Args:
        environment: The e-rights environment to perform the operation against.
        args: A list containing the command arguments. In this case the list
            should contain one element - a comma separated list of product ids.
    
    """
    product_ids = [prod_id.strip() for prod_id in args[0].split(",")]
    for product_id in product_ids:
        response = environment.service.deleteProduct(product_id)
        if response.status != "SUCCESS":
            print("Unable to delete product %s - does product exist?" % product_id)
        else:
            print("Deleted product %s" % product_id)
            
            
def get_product(environment, args):
    """Prints information about a product for a given product id.
    
    Args:
        environment: The e-rights environment to perform the operation against.
        args: A list containing the command arguments. In this case the list
            should contain one element - the id of the product.
    
    """
    product_id = args[0]
    product = _get_product(environment, product_id)
    if product:
        print("%s: %s" % (product_id, product.name))
        
        
def _get_product(environment, product_id):
    product = None
    response = environment.service.getProduct(product_id)
    if response.status == "SUCCESS":
        product = response.product
    else:
        print("Unable to get product with id %s - does product exist?" % product_id)
    return product


class Environment:
    
    def __init__(self, name, wsdl, ws_user, ws_pass, rightcare_url, adapter_host, adapter_port):
        self.name = name
        self.wsdl = wsdl
        self._ws_user = ws_user
        self._ws_pass = ws_pass 
        self.rightcare_url = rightcare_url
        self.adapter_host = adapter_host
        self.adapter_port = adapter_port
        
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
    
dev = Environment("dev", "https://oup-eac-newdev.literatumonline.com/oup/ws/OUPRightAccessService?wsdl", "oup", "oup123", "https://oup-eac-newdev.literatumonline.com/ui/Welcome.do", "oup-eac-newdev.literatumonline.com", 16123)
uat = Environment("uat", "https://oup-eac-uat.literatumonline.com/oup/ws/OUPRightAccessService?WSDL", "oupUAT", "T35t1nG", "https://oup-eac-uat.literatumonline.com/ui/Welcome.do", "oup-eac-uat.literatumonline.com", 16123)
prod = Environment("prod", "https://oup-eac.literatumonline.com/oup/ws/OUPRightAccessService?WSDL", "oupPROD", "P3&Nu154a1l", "https://oup-eac.literatumonline.com/ui/Welcome.do", "oup-eac.literatumonline.com", 6123)
#uat = Environment("uat", "http://localhost/erights_uat.wsdl", "oupUAT", "T35t1nG", "https://oup-eac-uat.literatumonline.com/ui/Welcome.do")
#prod = Environment("prod", "http://localhost/erights_prod.wsdl", "oupPROD", "P3&Nu154a1l", "https://oup-eac.literatumonline.com/ui/Welcome.do")
environments = {"dev": dev, "uat": uat, "prod": prod}