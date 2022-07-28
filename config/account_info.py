import os
import configparser


# 当前文件所在路径
curr_path = os.path.dirname(os.path.realpath(__file__))
# 路径拼接
configPath = os.path.join(curr_path, "account_info.ini")
conf = configparser.ConfigParser()
conf.read(configPath, encoding='UTF-8')

doctor2Email = conf.get("userInfo", "doctor2Email")
doctor2Pwd = conf.get("userInfo", "doctor2Pwd")

adminEmail = conf.get("userInfo", "adminEmail")
adminPwd = conf.get("userInfo", "adminPwd")

masterEmail = conf.get("userInfo", "masterEmail")
masterPwd = conf.get("userInfo", "masterPwd")

therapistLiuEmail = conf.get("userInfo", "therapistLiuEmail")
therapistLiuPwd = conf.get("userInfo", "therapistLiuPwd")

therapistOTEmail = conf.get("userInfo", "therapistOTEmail")
therapistOTPwd = conf.get("userInfo", "therapistOTPwd")



