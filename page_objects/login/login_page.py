import os
import time
from common.logger import MyLogging
from utils.object_map import ObjectMap
from common.logger import MyLogging
from config.public_data.delay_time import *

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
login_map = map_path + "/login/login.xml"

delay_time = DelayTime.short_time.value


class LoginPage(object):
    def __init__(self, driver):
        self.driver = driver
        self.login = ObjectMap(login_map)

    def login_fris(self):
        # log.info("login_page - login method")
        email_area = self.login.getLocator(self.driver, 'PhoneOrEmail')
        email_area.send_keys("912764695@qq.com")
        time.sleep(delay_time)
        # log.info("after email!")
        pwd_area = self.login.getLocator(self.driver, 'Pwd')
        pwd_area.send_keys("123456abc")
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
