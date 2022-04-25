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
disease_type_map = map_path + "/department_preserve/department.xml"

delay_time = DelayTime.short_time.value


class Department_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(disease_type_map)
        self.department = NavigateBar(self.driver)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

    def check_department_page(self):
        self.department.go_to_department()
        time.sleep(1)
        new_additional = self.version.getLocator(self.driver, "New_Additional").text
        check_department_name = self.version.getLocator(self.driver, 'Check_Department_Name').text
        time.sleep(1)
        check_operation_name = self.version.getLocator(self.driver, 'Check_Operation_Name').text
        return new_additional,check_department_name,check_operation_name





    def add_new_department(self,dep_name):
        self.department.go_to_department()
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys(dep_name)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        # 防止新建部门名称获取失败
        time.sleep(1)
        new_epartment_Name = self.version.getLocator(self.driver, 'Department_Name').text
        return new_epartment_Name
        log.info("新增成功")

    def delete_department(self):
        self.department.go_to_department()
        delete = self.version.getLocator(self.driver, "Delete")
        delete.click()
        self.driver.implicitly_wait(10)
        ensure = self.version.getLocator(self.driver, "Delete_Ensure")
        ensure.click()
        time.sleep(1)
        new_epartment_Name = self.version.getLocator(self.driver, 'Department_Name').text
        return new_epartment_Name
        log.info("删除成功")

    def change_department(self,dep_name):
        self.department.go_to_department()
        change = self.version.getLocator(self.driver,"Change")
        change.click()
        self.driver.implicitly_wait(20)
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.clear()
        import_words.send_keys(dep_name)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        change_epartment_Name = self.version.getLocator(self.driver, 'Department_Name').text
        return change_epartment_Name
        log.info("修改成功")



