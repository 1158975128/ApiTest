import os
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
version_page_map = map_path + "/login/version.xml"

delay_time = DelayTime.short_time.value


class VersionExpanded(object):
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(version_page_map)

    def navigate_version_control(self):
        expanded = self.driver.find_element_by_name('系统管理').get_attribute("aria-expanded")
        if expanded == "Ture":
            version_management = self.version.getLocator(self.driver, 'Version_Management')
            version_management.click()
        else:
            system_management = self.version.getLocator(self.driver, 'System_Management')
            system_management.click()
            time.sleep(delay_time)
            version_management = self.version.getLocator(self.driver, 'Version_Management')
            version_management.click()
            time.sleep(delay_time)



    '''
    def confirm_login(self):
        try:
            login_reminder = Alert()
    '''