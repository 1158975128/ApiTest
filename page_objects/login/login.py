import os
import logging
from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException

# log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
login_map = map_path + "/login/login.xml"


class LoginPage(object):
    def __init__(self, driver):
        self.driver = driver
        self.login = ObjectMap(login_map)

    def login_fris(self, user_name, pwd):
        """
        登录
        :param user_name: 邮箱或电话
        :param pwd: 密码
        """
        logging.info("登录，用户名：%(user_name)s，密码：%(pwd)s" % {"user_name": user_name, "pwd": pwd})
        email_area = self.login.getLocator(self.driver, 'PhoneOrEmail')
        # email_area.click()
        email_area.clear()
        # time.sleep(1)
        email_area.send_keys(user_name)
        # time.sleep(delay_time)
        pwd_area = self.login.getLocator(self.driver, 'Pwd')
        pwd_area.send_keys(pwd)
        login_button = self.login.getLocator(self.driver, 'LoginButton')
        login_button.click()
        self.driver.implicitly_wait(3)
        login_confirm_button = self.login.getLocator(self.driver, 'LoginPrimary')
        self.driver.implicitly_wait(3)
        login_confirm_button.click()
        # try:
        #     login_confirm_button = self.login.getLocator(self.driver, 'LoginPrimary')
        # except NoSuchElementException:
        #     logging.info("直接登录，无需确认")
        # else:
        #     login_confirm_button.click()
        # finally:
        #     logging.info("已登录")

    def select_organization(self):
        """
        选择机构
        """
        logging.info("选择机构")
        organization = self.login.getLocator(self.driver, 'Organization')
        organization.click()
        logging.info("已选机构")
