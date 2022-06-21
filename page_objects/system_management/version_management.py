import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from page_objects.navigate_bar_all.navigate_version import VersionExpanded

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
version_map = map_path + "/system_management/version_management.xml"

delay_time = DelayTime.short_time.value


class Version_Manage():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(version_map)
        self.version_page = NavigateBar(self.driver)


    def check_version_page(self):
        '''
        检查页面元素是否正确
        :return: menu_bar
        '''
        self.version_page.go_to_system_management()
        time.sleep(1)
        menu_bar = self.version.getLocator(self.driver, "Menu_Bar").text.strip()
        # menu_tr = self.driver.find_element(By.CSS_SELECTOR,value='[class="el-table__row"]')
        # menu_tds = menu_tr.find_elements(By.TAG_NAME, value='td')
        # for menu_td in menu_tds:
        #     menu_list.append(menu_td.text.strip())

        return menu_bar

    def modify_version(self,version,desc):
        '''
        修改系统版本号和描述
        :param version: 传入修改的版本号
        :param desc: 传入修改的描述
        :return: 返回修改前的版本号和描述
        '''
        self.version_page.go_to_system_management()
        time.sleep(1)
        menu_list = []
        menu_tr = self.driver.find_element(By.CSS_SELECTOR,value='[class="el-table__row"]')
        menu_tds = menu_tr.find_elements(By.TAG_NAME, value='td')
        for menu_td in menu_tds:
            menu_list.append(menu_td.text.strip())
        modify_button = self.version.getLocator(self.driver, "Modify_button")
        modify_button.click()
        time.sleep(1)
        version_number = self.version.getLocator(self.driver, "Version_Number")
        version_number.clear()
        version_number.send_keys(version)
        describe = self.version.getLocator(self.driver, "Describe")
        describe.clear()
        describe.send_keys(desc)
        time.sleep(1)
        save = self.version.getLocator(self.driver, "Save")
        save.click()
        time.sleep(1)
        return menu_list
