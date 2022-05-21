import os
import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.patient.patient_manage import Patient_Manage
from datetime import datetime


log = MyLogging(__name__).logger


class Test_Patient():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Patient = Patient_Manage(self.driver)
        self.driver.quit()

    data = [("新增患者", "门诊", "全部患者")]
    @pytest.mark.parametrize('new_patient,origin_droplist,patient_droplist', data)
    def test_01_department_type_page(self,new_patient,origin_droplist,patient_droplist):
        '''
        检查患者管理页面是否正确
        :param new_patient: 新增患者按钮
        :param origin_droplist: 默认患者来源为门诊
        :param patient_droplist:默认为全部患者
        :return:
        '''
        self.patient = Patient_Manage(self.driver)
        page_list = self.patient.check_patient_page()
        print(page_list)
        assert new_patient == page_list[0] and origin_droplist == page_list[1] and patient_droplist == page_list[2]
        log.info("TestCase:验证检查患者管理页面<br/>")

    data = [("测试患者","关节炎","关")]
    @pytest.mark.parametrize('patient,disease,diagnosis',data)
    def test_02_add_department(self,patient,disease,diagnosis):
        '''
        测试新增住院患者
        :param patient: 患者姓名
        :param disease: 疾病类型
        :param diagnosis: 功能诊断
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = patient + now_time
        self.patient = Patient_Manage(self.driver)
        self.patient.add_new_patient(patient,disease,diagnosis)
        self.patient.find_patient(patient)
        check_name = self.patient.patient_name(patient)
        log.info("新增患者成功")
        time.sleep(1)
        assert check_name is True

    data = [("测试患者","关节炎","关","ui_test")]
    @pytest.mark.parametrize('patient,disease,diagnosis,item_name',data)
    def test_02_add_department(self,patient,disease,diagnosis,item_name):
        '''
        测试新增住院患者
        :param patient: 患者姓名
        :param disease: 疾病类型
        :param diagnosis: 功能诊断
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = patient + now_time
        self.patient = Patient_Manage(self.driver)
        self.patient.add_new_patient(patient,disease,diagnosis)
        self.patient.find_patient(patient)
        self.patient.add_treatment_item(patient,item_name)
        print(patient)



    # data = [("测试修改新增部门","测试修改部门")]
    # @pytest.mark.parametrize('department,change_depar',data)
    # def test_03_change_department(self,department,change_depar):
    #     '''
    #     测试修改部门名称
    #     :param department: 传入新增部门名称
    #     :param change_depar: 传入修改部门名称
    #     '''
    #     now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
    #     department = department + now_time
    #     change_depar = change_depar + now_time
    #     self.type = Department_Type(self.driver)
    #     self.type.add_new_department(department)
    #     log.info("新增部门成功")
    #     self.driver.implicitly_wait(10)
    #     self.type.change_department(department,change_depar)
    #     assert self.type.find_department_name(change_depar)  is True
    #     time.sleep(1)
    #     self.type.delete_department(change_depar)
    #
    # data = [("测试删除新增部门")]
    # @pytest.mark.parametrize('delete_depar',data)
    # def test_04_delete_department(self,delete_depar):
    #     '''
    #     测试删除部门
    #     :param delete_depar: 传入删除的部门名称
    #     '''
    #     print("TestCase:入参传入delete_depar，查看删除部门是否成功")
    #     now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
    #     delete_depar = delete_depar + now_time
    #     self.type = Department_Type(self.driver)
    #     self.type.add_new_department(delete_depar)
    #     log.info("新增部门成功")
    #     time.sleep(1)
    #     self.type.delete_department(delete_depar)
    #     time.sleep(1)
    #     assert self.type.find_department_name(delete_depar) is False