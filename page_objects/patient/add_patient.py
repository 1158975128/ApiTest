import os
import time
import random
from selenium import webdriver
from utils.object_map import ObjectMap
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
# from selenium.webdriver.support import expected_conditions


map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
patient_map = map_path + "/patient/patient.xml"
add_patient_map = map_path + "/patient/add_patient.xml"


class AddPatient:
    """
    新增患者对话框
    """
    def __init__(self, driver):
        self.driver = driver
        self.patient_map = ObjectMap(patient_map)
        self.add_patient_map = ObjectMap(add_patient_map)
        # self.patient = NavigateBar(self.driver)

    def open_add_patient_dialog(self):
        """
        打开新增患者对话框
        """
        wait = WebDriverWait(self.driver, 30)
        try:
            wait.until(lambda x: self.patient_map.getLocator(self.driver, 'New_patient'))
        except TimeoutException:
            raise TimeoutException("超时！没找到 新增患者 按钮！")
        else:
            add_patient_btn = self.patient_map.getLocator(self.driver, 'New_patient')
            add_patient_btn.click()

    def close_add_patient_dialog(self):
        """
        关闭新增患者对话框
        """
        close_btn = self.add_patient_map.getLocator(self.driver, 'CloseBtn')
        close_btn.click()

    def click_add_patient_dialog_title(self):
        """
        单击 新增患者 对话框标题
        """
        title = self.add_patient_map.getLocator(self.driver, 'AddPatientTitle')
        title.click()

    def expand_basic_info_module(self):
        """
        展开基本信息模块
        """
        expand_btn = self.add_patient_map.getLocator(self.driver, 'BasicInfoModuleExpandedBtn')
        expand_btn.click()

    def expand_treatment_info_module(self):
        """
        展开疗程信息模块
        """
        expand_btn = self.add_patient_map.getLocator(self.driver, 'TreatmentInfoModuleExpandedBtn')
        expand_btn.click()
