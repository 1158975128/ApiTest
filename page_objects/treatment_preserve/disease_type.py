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
disease_type_map = map_path + "/treatment_preserve/disease_type.xml"


delay_time = DelayTime.short_time.value


class Disease_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(disease_type_map)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

    def add_new_disease(self):
        treatment_preserve = self.version.getLocator(self.driver, "Treatment_Preserve")
        treatment_preserve.click()
        disease_type = self.version.getLocator(self.driver, "Disease_Type")
        disease_type.click()
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys("测试新增疾病类型")
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        new_disease_name = self.version.getLocator(self.driver, 'Disease_name').text
        return new_disease_name
        log.info("新增成功")

    def add_none_disease(self):
        treatment_preserve = self.version.getLocator(self.driver, "Treatment_Preserve")
        treatment_preserve.click()
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
        next_page = self.version.getLocator(self.driver, "Next_Page")

