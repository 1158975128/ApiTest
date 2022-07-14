import os
import logging
from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException, TimeoutException, StaleElementReferenceException
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.common.by import By


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
        self.driver.implicitly_wait(5)
        wait = WebDriverWait(self.driver, 30)
        try:
            login_confirm_button = self.login.getLocator(self.driver, 'LoginPrimary')
            wait.until(expected_conditions.visibility_of(login_confirm_button))
        except (NoSuchElementException, TimeoutException, StaleElementReferenceException):
            logging.info("未找到登录确认按钮，继续！")
        else:
            logging.info("需要点击登录确认按钮")
            login_confirm_button = self.login.getLocator(self.driver, 'LoginPrimary')
            login_confirm_button.click()
        logging.info("登录成功！")


    def select_organization(self):
        """
        选择机构
        """
        logging.info("选择机构")
        # org_ul = self.driver.find_element(By.CSS_SELECTOR, value='.organization-checkout-list')
        # org_lis =org_ul.find_elements(By.TAG_NAME, value='li')
        # print('-----',len(org_lis),'-----')
        # for org_li in org_lis:
        #     print(org_li.get_attribute('textContent').strip())
        organization = self.login.getLocator(self.driver, 'Organization')
        organization.click()
        logging.info("已选机构")
