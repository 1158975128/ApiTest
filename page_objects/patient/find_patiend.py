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
find_patient_map = map_path + "/patient/find_patient.xml"

delay_time = DelayTime.short_time.value


class Find_patient():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(find_patient_map)
        self.patient = NavigateBar(self.driver)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

    def find_patient(self,patient_name):
        self.patient.go_to_patient()
        time.sleep(3)
        origin_select = self.version.getLocator(self.driver, "Origin_select")
        origin_select.click()
        time.sleep(1)
        hospital_select = self.version.getLocator(self.driver, "Hospital_select")
        hospital_select.click()
        time.sleep(1)
        name_input = self.version.getLocator(self.driver, "Name_input")
        name_input.send_keys(patient_name)
        time.sleep(1)
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)

    def add_treatment_item(self):
        add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
        add_Treatment.click()
        item_name = self.version.getLocator(self.driver, "Item_name")
        item_name.click()
        time.sleep(1)
        item_name.send_keys('ui_test')
        time.sleep(1)
        item_name_select = self.version.getLocator(self.driver, "Item_name_select")
        item_name_select.click()
        time.sleep(1)
        region_name = self.version.getLocator(self.driver, "Region_name")
        region_name.click()
        time.sleep(1)
        region_name_select = self.version.getLocator(self.driver, "Region_name_select")
        region_name_select.click()
        time.sleep(1)

        department_name = self.version.getLocator(self.driver, "Department_name")
        department_name.click()
        time.sleep(1)
        department_name_select = self.version.getLocator(self.driver, "Department_name_select")
        department_name_select.click()
        time.sleep(1)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()


