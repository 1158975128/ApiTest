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

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Type = Department_Type(self.driver)
        # self.Type.delete_disease()
        self.driver.quit()

    data = [("新增部门", "部门名称","操作")]
    @pytest.mark.parametrize("new_additional, check_department_name,check_operation_name",data)
    def test_01_department_type_page(self,new_additional, check_department_name,check_operation_name):
        print("TestCase:验证部门页格式正确<br/>")
        self.type = Department_Type(self.driver)
        additional, department_name,operation_name = self.type.check_department_page()
        assert additional == new_additional and department_name == check_department_name and operation_name == check_operation_name
        log.info("TestCase:验证部门页格式正确<br/>")



    data = [("测试新增部门", True)]
    @pytest.mark.parametrize("department, result",data)
    def test_02_add_department(self,department, result):
        # print("TestCase:" + test_case)
        self.type = Department_Type(self.driver)
        new_department = self.type.add_new_department(department)
        log.info("新增部门成功")
        print(new_department)
        assert department == new_department
        self.type.delete_department()


    data = [("测试新增部门","测试修改部门")]
    @pytest.mark.parametrize("department,change_depar",data)
    def test_03_change_department(self,department,change_depar):
        self.type = Department_Type(self.driver)
        new_department = self.type.add_new_department(department)
        log.info("新增部门成功")
        print(new_department)
        self.driver.implicitly_wait(10)
        change_department = self.type.change_department(change_depar)
        print(change_department)
        assert change_department == change_depar
        self.type.delete_department()

    data = [("测试删除部门")]
    @pytest.mark.parametrize("delete_depar",data)
    def test_04_delete_department(self,delete_depar):
        self.type = Department_Type(self.driver)
        new_department = self.type.add_new_department(delete_depar)
        log.info("新增部门成功")
        print(new_department)
        self.driver.implicitly_wait(10)
        delete = self.type.delete_department()
        assert delete_depar != delete
