import os
import time
import random
from selenium import webdriver
from utils.object_map import ObjectMap
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions


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
        wait = WebDriverWait(self.driver, 30)
        try:
            wait.until(lambda x: self.patient.getLocator(self.driver, 'New_patient'))
        except TimeoutException:
            raise TimeoutException("超时！没找到 新增患者 按钮！")
        else:
            add_patient_btn = self.patient.getLocator(self.driver, 'New_patient')
            add_patient_btn.click()

    def close_add_patient_dialog(self):
        """
        关闭新增患者对话框
        """
        close_btn = self.add_patient.getLocator(self.driver, 'CloseBtn')
        close_btn.click()
