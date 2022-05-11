import os
import time
import unittest
<<<<<<< HEAD
from random import random

import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
=======
import pytest
>>>>>>> ca9524a... pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.department_preserve.department import Department_Type
from datetime import datetime


log = MyLogging(__name__).logger


class TestDepartment():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Type = Department_Type(self.driver)
        self.driver.quit()

    data = [("新增部门", "部门名称","操作")]
<<<<<<< HEAD
    @pytest.mark.parametrize("new_additional, check_department_name,check_operation_name",data)
=======
    @pytest.mark.parametrize('new_additional, check_department_name,check_operation_name',data)
>>>>>>> ca9524a... pytest
    def test_01_department_type_page(self,new_additional, check_department_name,check_operation_name):
        '''
        Author：LX
        Logic：验证部门页格式正确
        '''
        print("TestCase:验证部门页格式正确<br/>")
        self.type = Department_Type(self.driver)
        additional, department_name,operation_name = self.type.check_department_page()
<<<<<<< HEAD
        assert additional == new_additional and department_name == check_department_name and operation_name == check_operation_name
        log.info("TestCase:验证部门页格式正确<br/>")

<<<<<<< HEAD


    data = [("测试新增部门", True)]
    @pytest.mark.parametrize("department, result",data)
    def test_02_add_department(self,department, result):
        # print("TestCase:" + test_case)
=======
    @parameterized.expand([("测试新增部门")])
=======
        assert additional==new_additional and department_name==check_department_name and operation_name==check_operation_name
        log.info("TestCase:验证部门页格式正确<br/>")

    data = [("测试新增部门")]
    @pytest.mark.parametrize('department',data)
>>>>>>> ca9524a... pytest
    def test_02_add_department(self,department):
        '''
        Author：LX
        Logic：测试新增部门
        '''
        print("TestCase:入参传入department，查看新增部门是否成功" )
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        department = department + now_time
>>>>>>> 9aedfd0... 修改、删除方法
        self.type = Department_Type(self.driver)
        self.type.add_new_department(department)
        log.info("新增部门成功")
        time.sleep(1)
<<<<<<< HEAD
<<<<<<< HEAD
        new_department = self.type.find_department_name(department)
        print(new_department)
<<<<<<< HEAD
        assert department == new_department
        self.type.delete_department()
=======
        self.assertEqual(department,new_department)
        self.type.delete_department(department)
>>>>>>> 9aedfd0... 修改、删除方法


    data = [("测试新增部门","测试修改部门")]
    @pytest.mark.parametrize("department,change_depar",data)
=======
        self.assertTrue(self.type.find_department_name(department))
=======
        assert self.type.find_department_name(department) is True
>>>>>>> ca9524a... pytest
        time.sleep(1)
        self.type.delete_department(department)
        time.sleep(1)

<<<<<<< HEAD
    @parameterized.expand([("测试修改新增部门","测试修改部门")])
>>>>>>> c7f9b90... 注释
=======
    data = [("测试修改新增部门","测试修改部门")]
    @pytest.mark.parametrize('department,change_depar',data)
>>>>>>> ca9524a... pytest
    def test_03_change_department(self,department,change_depar):
        '''
        Author：LX
        Logic：测试修改部门名称
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        department = department + now_time
        change_depar = change_depar + now_time
        self.type = Department_Type(self.driver)
        self.type.add_new_department(department)
        log.info("新增部门成功")
        self.driver.implicitly_wait(10)
        self.type.change_department(department,change_depar)
<<<<<<< HEAD
<<<<<<< HEAD
        change_department = self.type.find_department_name(change_depar)
        print(change_department)
<<<<<<< HEAD
        assert change_department == change_depar
        self.type.delete_department()
=======
        self.assertEqual(change_department,change_depar)
        self.type.delete_department(change_depar)
>>>>>>> 9aedfd0... 修改、删除方法

    data = [("测试删除部门")]
    @pytest.mark.parametrize("delete_depar",data)
=======
        self.assertTrue(self.type.find_department_name(change_depar))
=======
        assert self.type.find_department_name(change_depar)  is True
>>>>>>> ca9524a... pytest
        time.sleep(1)
        self.type.delete_department(change_depar)

<<<<<<< HEAD
    @parameterized.expand([("测试删除新增部门")])
>>>>>>> c7f9b90... 注释
=======
    data = [("测试删除新增部门")]
    @pytest.mark.parametrize('delete_depar',data)
>>>>>>> ca9524a... pytest
    def test_04_delete_department(self,delete_depar):
        '''
        Author：LX
        Logic：测试删除部门
        '''
        print("TestCase:入参传入delete_depar，查看删除部门是否成功")
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        delete_depar = delete_depar + now_time
        self.type = Department_Type(self.driver)
        self.type.add_new_department(delete_depar)
        log.info("新增部门成功")
<<<<<<< HEAD
        self.driver.implicitly_wait(10)
<<<<<<< HEAD
        delete = self.type.delete_department()
        assert delete_depar != delete
=======
        self.type.delete_department(delete_depar)
        new_department = self.type.find_department_name(delete_depar)
        self.assertFalse(new_department)


    # 调试函数@parameterized.expand([("测试新增部门","测试修改部门")])
    # def test_03_change_department(self):
    #     self.type = Department_Type(self.driver)
        # self.type.find_department_name('测试新增部门2022_05_03_18_09_55')
        # self.type.delete_department('测试新增部门2022_05_03_18_31_50')


if __name__ == '__main__':
    unittest.main()
>>>>>>> 9aedfd0... 修改、删除方法
=======
        time.sleep(1)
        self.type.delete_department(delete_depar)
        time.sleep(1)
<<<<<<< HEAD
        self.assertFalse(self.type.find_department_name(delete_depar))
>>>>>>> c7f9b90... 注释
=======
        assert self.type.find_department_name(delete_depar) is False
>>>>>>> ca9524a... pytest
