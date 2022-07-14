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
logout_map = map_path + "/login/logout.xml"

delay_time = DelayTime.short_time.value


class Logout():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(logout_map)


    def check_persion_list_page(self):
        self.reset.go_to_system_maintain()
        time.sleep(5)
        new_additional = self.version.getLocator(self.driver, "New_Additional").text
        inpu_name = self.version.getLocator(self.driver, 'Input').get_attribute('placeholder')
        search = self.version.getLocator(self.driver, 'Search').text
        navigate = self.version.getLocator(self.driver, 'Navigate').get_attribute('textContent')
        check.equal(new_additional,'新增账号','检查新增账号按钮')
        check.equal(inpu_name,'输入搜索内容','检查输入框默认内容')
        check.equal(search,'查询','检查查询按钮')
        check.equal(navigate,'id用户名邮箱手机号创建时间操作','检查菜单栏')
        log.info("页面元素正确")

    def logout(self):
        my_photo = self.version.getLocator(self.driver, 'My_Photo')
        my_photo.click()
        time.sleep(1)
        logout = self.version.getLocator(self.driver, 'Logout')
        logout.click()
        time.sleep(1)
        ensure = self.version.getLocator(self.driver, 'Ensure')
        ensure.click()
        time.sleep(1)
        title = self.version.getLocator(self.driver, 'Title').get_attribute('textContent').strip()
        return title




