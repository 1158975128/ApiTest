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
patient_map = map_path + "/patient/patient_manage.xml"

delay_time = DelayTime.short_time.value


class Patient_Manage():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(patient_map)
        self.patient = NavigateBar(self.driver)

    def check_patient_page(self):
        '''
        检查页面元素是否正确
        :return: new_patient,origin_droplist,patient_droplist
        '''
        self.patient.go_to_patient()
        time.sleep(1)
        new_patient = self.version.getLocator(self.driver, "New_patient").text
        origin_droplist = self.version.getLocator(self.driver, "Origin_droplist").get_attribute('value')
        patient_droplist = self.version.getLocator(self.driver, "Patient_droplist").get_attribute('value')
        return new_patient,origin_droplist,patient_droplist

    def add_new_patient(self,patient,disease,diagnosis):
        self.patient.go_to_patient()
        self.driver.implicitly_wait(20)
        new_patient = self.version.getLocator(self.driver, "New_patient")
        new_patient.click()
        patient_name = self.version.getLocator(self.driver, "Patient_name")
        patient_name.send_keys(patient)
        sex = self.version.getLocator(self.driver, "Sex_man")
        sex.click()
        disease_type = self.version.getLocator(self.driver, "Disease_type")
        disease_type.click()
        time.sleep(1)
        disease_type.send_keys(disease)
        disease_type_select = self.version.getLocator(self.driver, "Disease_type_select")
        disease_type_select.click()
        time.sleep(2)
        origin = self.version.getLocator(self.driver, "Origin")
        origin.click()
        time.sleep(1)
        function_diagnosis = self.version.getLocator(self.driver, "Function_diagnosis")
        function_diagnosis.click()
        time.sleep(1)
        function_diagnosis.send_keys(diagnosis)
        function_diagnosis_select = self.version.getLocator(self.driver, "Function_diagnosis_select")
        function_diagnosis_select.click()
        time.sleep(1)
        doctor = self.version.getLocator(self.driver, "Doctor")
        doctor.click()
        time.sleep(1)
        doctor_select = self.version.getLocator(self.driver, "Doctor_select")
        doctor_select.click()
        time.sleep(1)
        deparetment = self.version.getLocator(self.driver, "Department")
        deparetment.click()
        time.sleep(1)
        deparetment_select = self.version.getLocator(self.driver, "Department_select")
        deparetment_select.click()
        time.sleep(1)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        log.info("新增成功")

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

    def patient_name(self,patient_name):
        check_name = self.version.getLocator(self.driver, "Check_name")
        if patient_name == check_name.text:
            time.sleep(1)
        return True

    def add_treatment_item(self,patient_name,item):
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text,patient_name)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
            add_Treatment.click()
            item_name = self.version.getLocator(self.driver, "Item_name")
            item_name.click()
            time.sleep(1)
            item_name.send_keys(item)
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
            item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
            item_ensure.click()
            time.sleep(3)
        # add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
        # add_Treatment.click()
        # item_name = self.version.getLocator(self.driver, "Item_name")
        # item_name.click()
        # time.sleep(1)
        # item_name.send_keys(item)
        # time.sleep(1)
        # item_name_select = self.version.getLocator(self.driver, "Item_name_select")
        # item_name_select.click()
        # time.sleep(1)
        # region_name = self.version.getLocator(self.driver, "Region_name")
        # region_name.click()
        # time.sleep(1)
        # region_name_select = self.version.getLocator(self.driver, "Region_name_select")
        # region_name_select.click()
        # time.sleep(1)
        #
        # department_name = self.version.getLocator(self.driver, "Department_name")
        # department_name.click()
        # time.sleep(1)
        # department_name_select = self.version.getLocator(self.driver, "Department_name_select")
        # department_name_select.click()
        # time.sleep(1)
        # item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
        # item_ensure.click()
        # time.sleep(3)