import os
import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.login.reset_password import Reset_pwd
from page_objects.login.logout import Logout
from page_objects.arrange_order.work_clockin import Work_Clock_In
import pytest_check as check

log = MyLogging(__name__).logger


class TestDepartment():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')
        # self.work = Work_Clock_In(self.driver)
        # self.work.start_work('OT')

    def teardown(self):
        self.Reset = Reset_pwd(self.driver)
        self.driver.quit()

    # 需求号S#1650
    def test_01_check_persion_list_page(self):
        self.reset = Reset_pwd(self.driver)
        self.reset.check_persion_list_page()

    data = [("周治疗师全")]
    @pytest.mark.parametrize('user_name',data)
    def test_02_reset_pwd(self,user_name):
        self.reset = Reset_pwd(self.driver)
        print(user_name)
        self.reset.reset_pwd(user_name)
        time.sleep(1)
        self.logout = Logout(self.driver)
        self.logout.logout()
        time.sleep(1)
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('other')
