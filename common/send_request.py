import requests
import json

class RunMethod:
    # post
    def do_post(self, url, data, headers=None):
        res = None
        if headers != None:
            res = requests.post(url=url, json=data,headers=headers)
        else:
            res = requests.post(url=url, json=data)
        return res.json()

    # get
    def do_get(self, url, data=None, headers=None):
        res = None
        if headers != None:
            res = requests.get(url=url, params=data, headers=headers)
        else:
            res = requests.get(url=url, params=data)
        return res.json()

    # put
    def do_put(self, url, data=None, headers=None):
        res = None
        if headers != None:
            res = requests.put(url=url, json=data, headers=headers)
        else:
            res = requests.put(url=url, json=data)
        return res.json()

    def run_method(self, method, url, data=None, headers=None):
        res = None
        if method == "POST" or method == "post":
            res = self.do_post(url, data, headers)
        elif method == "PUT" or method == "put":
            res = self.do_put(url, data, headers)
        else:
            res = self.do_get(url, data, headers)
        return res
