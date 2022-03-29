import os
import unittest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.version.version_page import VersionPage

log = MyLogging(__name__).logger


class TestVersionControl(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.driver = Browser.open_browser()
        cls.mylogin = LoginPage(cls.driver)
        cls.mylogin.login_fris()

    def test_01_version_control_page(self):
        print("TestCase:验证版本管理页格式正确<br/>")

    @parameterized.expand([("验证版本更新成功", "V 1.2.34", True), ("验证版本更新空数值失败", "", False)])
    def test_02_version_update(self, test_case, version_number, result):
        print("TestCase:" + test_case)
        # self.mylogin = LoginPage(self.driver)
        # self.mylogin.login_fris()

        self.version = VersionPage(self.driver)
        self.version.get_page_version()
        log.info("版本修改成功")

        # 获取更新后版本号
        page_version_number = self.version.send_none_version()
        self.assertEqual(version_number, page_version_number, "版本更新不成功")
        log.info("验证版本更新完成")

    @classmethod
    def tearDownClass(cls):
        print("开始恢复初始版本号")
        cls.version = VersionPage(cls.driver)
        cls.version.recover_page_version()
        log.info("版本号已恢复")
        cls.driver.quit()
