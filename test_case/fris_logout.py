import os
import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.login.logout import Logout
import pytest_check as check

log = MyLogging(__name__).logger


class TestDepartment():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Logout = Logout(self.driver)
        self.driver.quit()



    def test_02_reset_pwd(self):
        self.logout = Logout(self.driver)
        time.sleep(2)
        self.logout.logout()