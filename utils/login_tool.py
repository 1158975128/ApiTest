from utils.request.sys_login_request import *
from jsonpath import jsonpath
from common.logger import MyLogging


log = MyLogging(__name__).logger


def get_login_headers(user=defaultInfo_config.adminEmail, pwd=defaultInfo_config.pwd):
    headers = {"Content-Type": "application/json"}
    login_data = {
        "email": user,
        "phone": "",
        "password": pwd,
        "rememberMe": 1,
        "isLogin": 1
    }
    log.info("login data is %s" % str(login_data))
    res = SysLoginRequest.login_request(data=login_data, headers=headers)
    token = jsonpath(res, '$.token')[0]
    log.info("get login token: %s" % str(token))
    login_headers = {
        "Accept": "application/json, text/plain, */*",
        "Content-Type": "application/json; charset=UTF-8",
        "token": token
    }
    return login_headers
