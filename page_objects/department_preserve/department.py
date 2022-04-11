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
department_type_map = '/Users/pca001/Downloads/devops_test/page_element/department_preserve/department.xml'


delay_time = DelayTime.short_time.value


class Department_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(department_type_map)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

    def add_new_department(self):
        department_Preserv = self.version.getLocator(self.driver, "Department_Preserve")
        department_Preserv.click()
        department = self.version.getLocator(self.driver, "Department")
        department.click()
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys("测试新增部门")
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        new_epartment_Name = self.version.getLocator(self.driver, 'Disease_name').text
        return new_epartment_Name
        log.info("新增成功")

    def add_none_department(self):
        department_preserve = self.version.getLocator(self.driver, "Treatment_Preserve")
        department_preserve.click()
        disease_type = self.version.getLocator(self.driver, "Disease_Type")
        disease_type.click()
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys("")
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        new_disease_name = self.version.getLocator(self.driver, 'Disease_name').text
        return new_disease_name

    def delete_disease(self):
        change_org = self.version.getLocator(self.driver, "Change_org")
        change_org.click()

    def add_new_count(self):
        department_Preserv = self.version.getLocator(self.driver, "Person_Preserve")
        department_Preserv.click()
        department = self.version.getLocator(self.driver, "Add_count")
        department.click()
        # new_additional = self.version.getLocator(self.driver, "New_Additional")
        # new_additional.click()
        # import_words = self.version.getLocator(self.driver, "Import_Words")
        # import_words.send_keys("测试新增部门")
        # ensure = self.version.getLocator(self.driver, "Ensure")
        # ensure.click()
        # new_epartment_Name = self.version.getLocator(self.driver, 'Disease_name').text
        # return new_epartment_Name
        # log.info("新增成功")

    # def delete_disease(self):
    #     next_page = self.version.getLocator(self.driver, "Next_Page")