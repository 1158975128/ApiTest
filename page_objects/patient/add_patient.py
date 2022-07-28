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
from selenium.webdriver.common.by import By
from page_objects.navigate_bar import NavigateBar
from utils.droplist_select_tool import select_droplist
from selenium.common.exceptions import NoSuchElementException
from utils.close_tips_tool import right_corner_cancel
from utils.close_tips_tool import close_login_tips
import logging


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
        self.navigate = NavigateBar(driver)

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

    def expand_hospital_info_module(self):
        """
        展开住院信息模块
        """
        expand_btn = self.add_patient_map.getLocator(self.driver, 'HospitalInfoModuleExpandedBtn')
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
        time.sleep(5)
        # return
        return add_patient_object

    def add_new_patient(self,name,identity,phone,profession,linkman,relation,disease,function,doctor,depart,source):

        self.navigate.go_to_patient()
        time.sleep(1)
        new_patient = self.add_patient_map.getLocator(self.driver, "New_patient")
        new_patient.click()
        # 展开基本信息
        expand_btn = self.add_patient_map.getLocator(self.driver, 'BasicInfoModuleExpandedBtn')
        expand_btn.click()
        # 患者姓名
        patient_name = self.add_patient_map.getLocator(self.driver, "NameInput")
        patient_name.send_keys(name)
        # 身份证号
        identity_number = self.add_patient_map.getLocator(self.driver, 'IdentityNumberInput')
        identity_number.send_keys(identity)
        # 手机号
        phone_number = self.add_patient_map.getLocator(self.driver, 'PhoneInput')
        phone_number.send_keys(phone)
        # 职业
        profession_input = self.add_patient_map.getLocator(self.driver, 'ProfessionInput')
        profession_input.click()
        time.sleep(1)
        select_droplist(self.driver,profession)
        # 联系人
        linkman_input = self.add_patient_map.getLocator(self.driver, 'LinkmanInput')
        linkman_input.send_keys(linkman)
        # 关系
        relation_input = self.add_patient_map.getLocator(self.driver, 'RelationInput')
        relation_input.click()
        time.sleep(1)
        select_droplist(self.driver,relation)
        # 收起基本信息
        time.sleep(1)
        expand_btn = self.add_patient_map.getLocator(self.driver, 'BasicInfoModuleExpandedBtn')
        expand_btn.click()
        time.sleep(1)
        # 疾病类型
        disease_type = self.add_patient_map.getLocator(self.driver, "PatientTypeBox")
        disease_type.click()
        time.sleep(1)
        disease_type.send_keys(disease)
        disease_type_select = self.add_patient_map.getLocator(self.driver, "Disease_type_select")
        disease_type_select.click()
        # 功能诊断
        function_diagnosis = self.add_patient_map.getLocator(self.driver, "FunctionalBox")
        function_diagnosis.click()
        time.sleep(1)
        function_diagnosis.send_keys(function)
        function_diagnosis_select = self.add_patient_map.getLocator(self.driver, "Function_diagnosis_select")
        function_diagnosis_select.click()
        # 主治医生
        doctor1 = self.add_patient_map.getLocator(self.driver, "AttendingDoctorBox")
        doctor1.click()
        time.sleep(1)
        select_droplist(self.driver,doctor)
        # 就诊科室
        deparetment = self.add_patient_map.getLocator(self.driver, "Department")
        deparetment.click()
        time.sleep(1)
        select_droplist(self.driver,depart)
        # 来源-住院
        source_btn = self.driver.find_element(By.XPATH,
                                         '//label[@for="sourceId"]/following-sibling::div/descendant::span[text()="%s"]' % source)
        source_btn.click()
        time.sleep(1)
        # 确定
        ensure = self.add_patient_map.getLocator(self.driver, "Confirm")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.add_patient_map.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            if tips != '登记患者成功！':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                close_login_tips(self.driver)
                return tips
