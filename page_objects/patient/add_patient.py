import os
import time
import random
from selenium import webdriver
from utils.object_map import ObjectMap
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
# from selenium.webdriver.support import expected_conditions
from utils.format_tool import FormatTool
from utils.drop_down_tool import DropDown
from utils.fris_object.add_patient_object import AddPatientObject
from selenium.common.exceptions import TimeoutException, StaleElementReferenceException
from selenium.webdriver.support.wait import WebDriverWait
# from selenium.webdriver.support import expected_conditions
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.remote.webelement import WebElement


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

    def select_drop_down_item_one(self):
        """
        选择下拉菜单第一个
        """
        item_one = self.add_patient_map.getLocator(self.driver, 'DropDownItemOne')
        item_one.click()
        ActionChains(self.driver).move_to_element(item_one).perform()
        try:
            item_one.click()
        except StaleElementReferenceException:
            logging.info("下拉菜单变化，重新点击")
            item_one = self.add_patient_map.getLocator(self.driver, 'DropDownItemOne')
            item_one.click()

    def add_patient_required(self):
        """
        添加患者（仅填写必填项）
        """
        add_patient_object = AddPatientObject()
        name = FormatTool.generate_patient_name()
        key_word = "a"
        # 姓名
        name_input = self.add_patient_map.getLocator(self.driver, 'NameInput')
        name_input.send_keys(name)
        add_patient_object.name = name
        # 疾病类型
        patient_type_box = self.add_patient_map.getLocator(self.driver, 'PatientTypeBox')
        patient_type_box.send_keys(key_word)
        time.sleep(3)
        drop_down = self.add_patient_map.getLocator(self.driver, 'DropDown')
        select_patient_type = DropDown.get_any_item_from_drop_down(drop_down)
        add_patient_object.patient_type = select_patient_type.get_attribute('innerText')
        select_patient_type.click()
        # add_patient_object.patient_type = select_patient_type.text
        # 功能诊断
        functional_box = self.add_patient_map.getLocator(self.driver, 'FunctionalBox')
        functional_box.send_keys(key_word)
        time.sleep(3)
        drop_down = self.add_patient_map.getLocator(self.driver, 'DropDown')
        select_functional = DropDown.get_any_item_from_drop_down(drop_down)
        add_patient_object.functional = select_functional.get_attribute('textContent')
        select_functional.click()
        # 主治医生
        attending_doctor_box = self.add_patient_map.getLocator(self.driver, 'AttendingDoctorBox')
        attending_doctor_box.click()
        time.sleep(3)
        drop_down = self.add_patient_map.getLocator(self.driver, 'DropDown')
        select_doctor = DropDown.get_any_item_from_drop_down(drop_down)
        add_patient_object.attending_doctor = select_doctor.get_attribute('textContent')
        select_doctor.click()
        # 确定
        confirm_btn = self.add_patient_map.getLocator(self.driver, 'Confirm')
        confirm_btn.click()
        # return
        return add_patient_object


