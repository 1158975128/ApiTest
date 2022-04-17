import os
import time
import unittest
from random import random

import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.department_preserve.department import Department_Type

log = MyLogging(__name__).logger


class TestDepartment():
    # def __init__(self):


    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Type = Department_Type(self.driver)
        # self.Type.delete_disease()
        self.driver.quit()

    def test_01_department_type_page(self):
        print("TestCase:验证部门页格式正确<br/>")

    # @parameterized.expand([("测试新增部门", True), ("", False)])
    data = [("测试新增部门", True)]
    @pytest.mark.parametrize("department, result",data)
    def test_02_add_department(self,department, result):
        # print("TestCase:" + test_case)
        # a = input("aa")
        self.type = Department_Type(self.driver)
        # time.sleep(3)
        # log.info("新增部门成功")
        # self.type = Department_Type(self.driver)
        # self.type.add_none_department()
        new_department = self.type.add_new_department()
        print(new_department)
        # b =input("bbb")
        # assert department == new_department
        # self.assertEqual(department, new_department+"输入空值失败")
