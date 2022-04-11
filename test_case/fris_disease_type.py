import os
import unittest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.treatment_preserve.disease_type import Disease_Type

log = MyLogging(__name__).logger

def my_sum(a,b):
    return  a+b
class TestDiseaseType(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.driver = Browser.open_browser()
        cls.mylogin = LoginPage(cls.driver)
        cls.mylogin.login_fris()

    def test_01_disease_type_page(self):
        print("TestCase:验证疾病类型页格式正确<br/>")



    @parameterized.expand([("","测试新增疾病类型", True), ("", "", False)])
    def test_02_version_update(self, test_case, disease_name, result):
        print("TestCase:" + test_case)
        self.type = Disease_Type(self.driver)
        self.type.add_new_disease()
        log.info("新增疾病类型成功")
        self.type = Disease_Type(self.driver)
        self.type.add_none_disease()
        new_disease_name = self.type.add_none_disease()
        self.assertEqual(disease_name, new_disease_name+"输入空值失败")

    @classmethod
    def tearDownClass(cls) -> None:
        cls.Type = Disease_Type(cls.driver)
        cls.Type.delete_disease()
        cls.driver.quit()