import os
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar_all.navigate_version import VersionExpanded

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
version_page_map = map_path + "/version/version.xml"


delay_time = DelayTime.short_time.value


class VersionPage(object):
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(version_page_map)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

    def get_page_version(self):
        system_management = self.version.getLocator(self.driver, 'System_Management')
        system_management.click()
        time.sleep(delay_time)
        version_management = self.version.getLocator(self.driver, 'Version_Management')
        version_management.click()
        time.sleep(delay_time)
        amend = self.version.getLocator(self.driver, 'Amend')
        amend.click()
        time.sleep(delay_time)
        import_version_number = self.version.getLocator(self.driver, 'Import_Version_Number')
        import_version_number.clear()
        time.sleep(delay_time)
        import_version_number.send_keys("V 1.2.34")
        save = self.version.getLocator(self.driver, 'Save')
        save.click()
        time.sleep(delay_time)

        page_version_number = self.version.getLocator(self.driver, 'Version_Number').text
        log.info('更新后版本号：' + page_version_number)
        return page_version_number

    def send_none_version(self):
        amend = self.version.getLocator(self.driver, 'Amend')
        amend.click()
        time.sleep(delay_time)
        import_version_number = self.version.getLocator(self.driver, 'Import_Version_Number')
        import_version_number.clear()
        time.sleep(delay_time)
        import_version_number.send_keys("")
        save = self.version.getLocator(self.driver, 'Save')
        save.click()
        time.sleep(delay_time)
        page_version_number = self.version.getLocator(self.driver, 'Version_Number').text
        log.info('更新后版本号：' + page_version_number)
        return page_version_number

    def recover_page_version(self):
        # system_management = self.version.getLocator(self.driver, 'System_Management')
        # system_management.click()
        # time.sleep(delay_time)
        # version_management = self.version.getLocator(self.driver, 'Version_Management')
        # version_management.click()
        # time.sleep(delay_time)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()
        amend = self.version.getLocator(self.driver, 'Amend')
        amend.click()
        time.sleep(delay_time)
        import_version_number = self.version.getLocator(self.driver, 'Import_Version_Number')
        import_version_number.clear()
        time.sleep(delay_time)
        import_version_number.send_keys("V 1.2.33")
        save = self.version.getLocator(self.driver, 'Save')
        save.click()
        time.sleep(delay_time)
        page_version_number = self.version.getLocator(self.driver, 'Version_Number').text
        log.info('恢复后版本号：' + page_version_number)
