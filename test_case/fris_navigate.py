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


class TestNavigate(unittest.TestCase):
    def setUp(self) -> None:
        self.driver = Browser.open_browser()

    def test_navigate(self):
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris()
        # log.info("login successful")
        self.navigate = NavigateBar(self.driver)
        # log.info("before navigate bar")
        self.navigate.navigate_bar()
        log.info('navigator run over')
        print("检验导航栏，成功<br/>")

    def tearDown(self) -> None:
        self.driver.quit()

    # def test_logout(self):
    #     self.assertTrue(True)