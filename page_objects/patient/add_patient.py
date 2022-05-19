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
patient_map = map_path + "/patient/add_patient.xml"

delay_time = DelayTime.short_time.value


class Patient_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(patient_map)
        self.patient = NavigateBar(self.driver)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

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

    def add_new_patient(self):
        '''
        新增住院患者
        :param patient_name: 新增患者名称
        '''
        self.patient.go_to_patient()
        self.driver.implicitly_wait(20)
        new_patient = self.version.getLocator(self.driver, "New_patient")
        new_patient.click()
        patient_name = self.version.getLocator(self.driver, "Patient_name")
        patient_name.send_keys('patient_name')
        sex = self.version.getLocator(self.driver, "Sex_man")
        sex.click()

        disease_type = self.version.getLocator(self.driver, "Disease_type")
        disease_type.click()
        time.sleep(1)
        disease_type.send_keys('关节炎')
        # time.sleep(1)
        disease_type_select = self.version.getLocator(self.driver, "Disease_type_select")
        disease_type_select.click()
        time.sleep(2)

        origin = self.version.getLocator(self.driver, "Origin")
        origin.click()
        time.sleep(1)

        function_diagnosis = self.version.getLocator(self.driver, "Function_diagnosis")
        function_diagnosis.click()
        time.sleep(1)
        function_diagnosis.send_keys('关')
        # time.sleep(1)
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
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(3)
        # assert patient_name == check_name
