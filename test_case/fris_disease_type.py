import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.treatment_preserve.disease_type import Disease_Type
import pytest_check as check


log = MyLogging(__name__).logger


class TestDisease():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Type = Disease_Type(self.driver)
        self.driver.quit()

    # 需求号S#1969
    data = [('新增疾病类型','疾病类型名称','创建时间','修改时间','操作')]
    @pytest.mark.parametrize('new_additional,position,create_time,modification_time,operation',data)
    def test_01_disease_page(self,new_additional,position,create_time,modification_time,operation):
        '''
        验证疾病类型页格式正确
        '''
        self.type = Disease_Type(self.driver)
        page_list = self.type.check_disease_page()
        check.equal(new_additional,page_list[0],"检查新增疾病类型按钮")
        check.equal(position,page_list[1],"检查疾病类型名称")
        check.equal(create_time,page_list[2],"检查创建时间")
        check.equal(modification_time,page_list[3],"检查修改时间")
        check.equal(operation,page_list[4],"检查操作")
        log.info("TestCase:验证疾病类型页格式正确<br/>")

    data = [("测试新增疾病类型")]
    @pytest.mark.parametrize('disease',data)
    def test_02_add_position(self,disease):
        '''
        测试新增疾病类型
        :param disease: 新增疾病类型名
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        disease = disease + now_time
        self.type = Disease_Type(self.driver)
        self.type.add_new_disease(disease)
        log.info("新增疾病类型成功")
        time.sleep(1)
        check.is_true(self.type.find_disease_name(disease),"判断疾病类型是否成功新增，返回true为成功")
        time.sleep(1)
        self.type.delete_disease(disease)
        time.sleep(1)

    data = [("测试修改新增疾病类型","测试修改疾病类型")]
    @pytest.mark.parametrize('disease,modify_disease',data)
    def test_03_modify_position(self,disease,modify_disease):
        '''
        测试修改疾病类型
        :param disease: 新增疾病类型名称
        :param modify_disease: 修改疾病类型名称
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        disease = disease + now_time
        modify_disease = modify_disease + now_time
        self.type = Disease_Type(self.driver)
        self.type.add_new_disease(disease)
        log.info("新增疾病类型成功")
        self.driver.implicitly_wait(10)
        self.type.find_disease_name(disease)
        time.sleep(1)
        self.type.modify_disease(disease,modify_disease)
        check.is_true(self.type.find_disease_name(modify_disease),"判断疾病类型是否成功修改，返回true为成功")
        time.sleep(1)
        self.type.delete_disease(modify_disease)
        time.sleep(1)

    data = [("测试删除新增疾病类型")]
    @pytest.mark.parametrize('disease',data)
    def test_04_delete_disease(self,disease):
        '''
        测试新增疾病类型
        :param disease: 新增疾病类型名
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        disease = disease + now_time
        self.type = Disease_Type(self.driver)
        self.type.add_new_disease(disease)
        log.info("新增疾病类型成功")
        time.sleep(1)
        self.type.find_disease_name(disease)
        time.sleep(1)
        self.type.delete_disease(disease)
        time.sleep(1)
        check.is_false(self.type.find_disease_name(disease),"判断疾病类型是否成功修改，返回false为成功")
