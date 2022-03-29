import os
import configparser
from common import glo


# 当前文件所在路径
curr_path = os.path.dirname(os.path.realpath(__file__))
# 路径拼接
configPath = os.path.join(curr_path, "url_config.ini")
conf = configparser.ConfigParser()
conf.read(configPath, encoding='UTF-8')

server_url = conf.get("urlconf", "server_url")

# glo._init()
input_port = glo.get_value('port')
if input_port == "" or input_port is None:
    port = conf.get("urlconf", "port")
else:
    port = input_port