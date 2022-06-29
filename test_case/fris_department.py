import os
import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.login.logout import Logout
from parameterized import parameterized
from page_objects.department_preserve.department import Department_Type
import pytest_check as check

log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin')
    yield
    logout = Logout(driver)
    logout.logout()

class TestDepartment():
    # 需求号S#1958
    data = [("新增部门", "部门名称","操作")]
    @pytest.mark.parametrize('new_additional, check_department_name,check_operation_name',data)
    def test_01_department_type_page(self,new_additional, check_department_name,check_operation_name,driver):
        '''
        验证部门页格式正确
        :param new_additional: 新增部门按钮
        :param check_department_name: 部门名称
        :param check_operation_name: 操作
        '''
        print("TestCase:验证部门页格式正确<br/>")
        self.type = Department_Type(driver)
        additional, department_name,operation_name = self.type.check_department_page()
        check.equal(additional,new_additional,"检查新增部门按钮")
        check.equal(department_name,check_department_name,"检查部门名称")
        check.equal(operation_name,check_operation_name,"检查操作")
        log.info("TestCase:验证部门页格式正确<br/>")

    data = [("测试新增部门")]
    @pytest.mark.parametrize('department',data)
    def test_02_add_department(self,department,driver):
        '''
        测试新增部门
        :param department: 需要传入新增部门名称
        '''
        print("TestCase:入参传入department，查看新增部门是否成功" )
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        department = department + now_time
        self.type = Department_Type(driver)
        self.type.add_new_department(department)
        log.info("新增部门成功")
        time.sleep(1)
        check.is_true(self.type.find_department_name(department),"判断部门是否成功新增，返回true为成功")
        time.sleep(1)
        self.type.delete_department(department)
        time.sleep(1)

    data = [("测试修改新增部门","测试修改部门")]
    @pytest.mark.parametrize('department,change_depar',data)
    def test_03_change_department(self,department,change_depar,driver):
        '''
        测试修改部门名称
        :param department: 传入新增部门名称
        :param change_depar: 传入修改部门名称
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        department = department + now_time
        change_depar = change_depar + now_time
        self.type = Department_Type(driver)
        self.type.add_new_department(department)
        log.info("新增部门成功")
        driver.implicitly_wait(10)
        self.type.change_department(department,change_depar)
        check.is_true(self.type.find_department_name(change_depar),"判断部门是否成功修改，返回true为成功")
        time.sleep(1)
        self.type.delete_department(change_depar)

    data = [("测试删除新增部门")]
    @pytest.mark.parametrize('delete_depar',data)
    def test_04_delete_department(self,delete_depar,driver):
        '''
        测试删除部门
        :param delete_depar: 传入删除的部门名称
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        delete_depar = delete_depar + now_time
        self.type = Department_Type(driver)
        self.type.add_new_department(delete_depar)
        log.info("新增部门成功")
        time.sleep(1)
        self.type.delete_department(delete_depar)
        time.sleep(1)
        check.is_false(self.type.find_department_name(delete_depar),"判断部门是否成功删除，false为成功")