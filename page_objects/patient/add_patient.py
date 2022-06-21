import os
import time
import random
from selenium import webdriver
from utils.object_map import ObjectMap


map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
patient_map = map_path + "/patient/patient.xml"
add_patient_map = map_path + "/patient/add_patient.xml"


class AddPatient:
    def __init__(self, driver):
        self.driver = driver
        self.patient = ObjectMap(patient_map)
        self.add_patient = ObjectMap(add_patient_map)
        # self.patient = NavigateBar(self.driver)

    def open_add_patient_dialog(self):
        """
        打开新增患者对话框
        """
        add_patient_btn = self.patient.getLocator(self.driver, 'New_patient')
        add_patient_btn.click()

    def close_add_patient_dialog(self):
        """
        关闭新增患者对话框
        """
        close_btn = self.add_patient.getLocator(self.driver, 'CloseBtn')
        close_btn.click()
