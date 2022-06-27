import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
import pytest_check as check


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
reset_pwd_map = map_path + "/login/reset_password.xml"

delay_time = DelayTime.short_time.value


class Reset_pwd():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(reset_pwd_map)
        self.reset = NavigateBar(self.driver)


    def check_persion_list_page(self):
        self.reset.go_to_system_maintain()
        time.sleep(5)
        new_additional = self.version.getLocator(self.driver, "New_Additional").text
        inpu_name = self.version.getLocator(self.driver, 'Input').get_attribute('placeholder')
        search = self.version.getLocator(self.driver, 'Search').text
        navigate = self.version.getLocator(self.driver, 'Navigate').get_attribute('textContent')
        check.equal(new_additional,'新增账号','检查新增账号按钮')
        check.equal(inpu_name,'请输入姓名','检查输入框默认内容')
        check.equal(search,'查询','检查查询按钮')
        check.equal(navigate,'id用户名邮箱手机号创建时间操作','检查菜单栏')
        log.info("页面元素正确")

    def reset_pwd(self,user_name):
        self.reset.go_to_system_maintain()
        time.sleep(1)
        print(123)
        inpu_name = self.version.getLocator(self.driver, 'Input')
        inpu_name.click()
        time.sleep(1)
        inpu_name.send_keys(user_name)
        search = self.version.getLocator(self.driver, 'Search')
        search.click()
        time.sleep(1)
        update = self.version.getLocator(self.driver, 'Update')
        update.click()
        time.sleep(1)
        reset_btn = self.version.getLocator(self.driver, 'Reset_Btn')
        reset_btn.click()
        time.sleep(1)
        tips = self.version.getLocator(self.driver, 'Tips').text
        check.equal(tips,'重置密码成功','检查重置密码成功弹窗提示')
        time.sleep(1)
        close = self.version.getLocator(self.driver, 'Close')
        close.click()

        log.info("重置密码成功")


