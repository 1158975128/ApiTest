import os
import configparser


# 当前文件所在路径
curr_path = os.path.dirname(os.path.realpath(__file__))
# 路径拼接
configPath = os.path.join(curr_path, "defaultInfo_config.ini")
conf = configparser.ConfigParser()
conf.read(configPath, encoding='UTF-8')

adminEmail = conf.get("defaultUser", "adminEmail")
pwd = conf.get("defaultUser", "pwd")
admId = conf.get("defaultUser", "admId")
doctorEmail = conf.get("defaultUser", "doctorEmail")
doctorPwd = conf.get("defaultUser", "doctorPwd")
masterEmail = conf.get("defaultUser", "masterEmail")
masterPwd = conf.get("defaultUser", "masterPwd")
therapistAllEmail = conf.get("defaultUser", "therapistAllEmail")
therapistAllPwd = conf.get("defaultUser", "therapistAllPwd")
therapistAllId = conf.get("defaultUser", "therapistAllId")
therapist1Email = conf.get("defaultUser", "therapist1Email")
therapist1Pwd = conf.get("defaultUser", "therapist1Pwd")
therapist2Email = conf.get("defaultUser", "therapist2Email")
therapist2Pwd = conf.get("defaultUser", "therapist2Pwd")
therapist3Email = conf.get("defaultUser", "therapist3Email")
therapist3Pwd = conf.get("defaultUser", "therapist3Pwd")
nurseEmail = conf.get("defaultUser", "nurseEmail")
nursePwd = conf.get("defaultUser", "nursePwd")
otherEmail = conf.get("defaultUser", "other")
otherPwd = conf.get("defaultUser", "otherpwd")
# 机构
organizationId = conf.get("defaultUser", "organizationId")
# login 路径
login_path = conf.get("defaultUser", "login_path")