import os
import configparser


# 当前文件所在路径
curr_path = os.path.dirname(os.path.realpath(__file__))
# 路径拼接
configPath = os.path.join(curr_path, "account_info.ini")
conf = configparser.ConfigParser()
conf.read(configPath, encoding='UTF-8')

doctorZhaoEmail = conf.get("userInfo", "doctorZhaoEmail")
doctorZhaoPwd = conf.get("userInfo", "doctorZhaoPwd")