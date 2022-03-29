import os
import unittest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException
from page_objects.navigate_bar import NavigateBar
log = MyLogging(__name__).logger


class TestLogin(unittest.TestCase):
    def setUp(self) -> None:
        self.driver = Browser.open_browser()

    def test_login(self):
        print("确认登录<br/>")
        self.mylogin = LoginPage(self.driver)
        log.info("before login")
        self.mylogin.login_fris()
        log.info("after login")
        try:
            self.driver.find_element_by_class_name('iconfont.icon-icon_shouye')
            # self.navigate = NavigateBar(self.driver)
            # self.navigate.is_element_present('iconfont.icon-icon_shouye')
        except NoSuchElementException:
            self.assertFalse(True, "not find xxx element")

    def test_cancel_login(self):
        print("取消登录<br/>")
        self.cancel_login = LoginPage(self.driver)
        self.cancel_login.cancel()
        log.info("已取消登录，成功")
        
    def tearDown(self) -> None:
        self.driver.quit()