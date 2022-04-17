import os
import time
from common.logger import MyLogging
from utils.object_map import ObjectMap
from common.logger import MyLogging
from config.public_data.delay_time import *
from page_objects.navigate_bar import NavigateBar
from config import defaultInfo_config

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
login_map = map_path + "/login/login.xml"

delay_time = DelayTime.short_time.value


def getUser(user):
    # 管理员
    if user.lower() == "admin":
        return defaultInfo_config.adminEmail
    # 康复医师
    elif user.lower() == "doctor":
        return defaultInfo_config.doctorEmail
    # 治疗师长，治疗师（OT, PT, ST, 物理，心理，传统，西医，常规，推拿，器械）
    elif user.lower() == "master":
        return defaultInfo_config.masterEmail
    # 治疗师all，治疗师（OT,PT,ST,物理，心理，传统，西医，常规，推拿，器械）
    elif user.lower() == "therapistall":
        return defaultInfo_config.therapistAllEmail
    # 治疗师1（OT,PT,ST）
    elif user.lower() == "therapist1":
        return defaultInfo_config.therapist1Email
    # 治疗师2（物理，心理，传统，西医，常规，推拿，器械）
    elif user.lower() == "therapist2":
        return defaultInfo_config.therapist2Email
    # 治疗师3（OT)
    elif user.lower() == "therapist3":
        return defaultInfo_config.therapist3Email
    # 护士
    elif user.lower() == "nurse":
        return defaultInfo_config.nurseEmail
    else:
        raise Exception("传入的user："  + user)

def getPwd(pwd):
    # 管理员
    if pwd.lower() == "admin":
        return defaultInfo_config.pwd
    # 康复医师
    elif pwd.lower() == "doctor":
        return defaultInfo_config.doctorPwd
    # 治疗师长，治疗师（OT, PT, ST, 物理，心理，传统，西医，常规，推拿，器械）
    elif pwd.lower() == "master":
        return defaultInfo_config.masterPwd
    # 治疗师all，治疗师（OT,PT,ST,物理，心理，传统，西医，常规，推拿，器械）
    elif pwd.lower() == "therapistall":
        return defaultInfo_config.therapistAllPwd
    # 治疗师1（OT,PT,ST）
    elif pwd.lower() == "therapist1":
        return defaultInfo_config.therapist1Pwd
    # 治疗师2（物理，心理，传统，西医，常规，推拿，器械）
    elif pwd.lower() == "therapist2":
        return defaultInfo_config.therapist2Pwd
    # 治疗师3（OT)
    elif pwd.lower() == "therapist3":
        return defaultInfo_config.therapist3Pwd
    # 护士
    elif pwd.lower() == "nurse":
        return defaultInfo_config.nursePwd
    else:
        raise Exception("传入的user："  + pwd)


class LoginPage(object):
    def __init__(self, driver):
        self.driver = driver
        self.login = ObjectMap(login_map)

    def login_fris(self,login_role):
        # log.info("login_page - login method")
        email_area = self.login.getLocator(self.driver, 'PhoneOrEmail')
        email_area.send_keys(getUser(login_role))
        time.sleep(delay_time)
        # log.info("after email!")
        pwd_area = self.login.getLocator(self.driver, 'Pwd')
        # pwd_area.send_keys("123456abc")
        pwd_area.send_keys(getPwd(login_role))
        # pwd_area.send_keys(defaultInfo_config.pwd)
        time.sleep(delay_time)
        login_button = self.login.getLocator(self.driver, 'LoginButton')
        login_button.click()
        time.sleep(delay_time)
        primary = self.login.getLocator(self.driver, 'LoginPrimary')
        primary.click()
        organization = self.login.getLocator(self.driver, 'Organization')
        organization.click()
        time.sleep(delay_time)
        log.info("登录成功")
        self.driver.implicitly_wait(10)

    def cancel(self):
        email_area = self.login.getLocator(self.driver, 'PhoneOrEmail')
        email_area.send_keys("17602173306")
        time.sleep(1)
        pwd_area = self.login.getLocator(self.driver, 'Pwd')
        pwd_area.send_keys("123456")
        time.sleep(1)
        login_button = self.login.getLocator(self.driver, 'LoginButton')
        login_button.click()
        time.sleep(1)
        cancel = self.login.getLocator(self.driver, 'Cancel')
        cancel.click()

    '''
    def confirm_login(self):
        try:
            login_reminder = Alert()
    '''

