import os
import time
import random
from selenium import webdriver
from utils.object_map import ObjectMap
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
# from selenium.webdriver.support import expected_conditions


map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
home_page_path = map_path + "/home_page/home_page_modify_info.xml"
# add_patient_map = map_path + "/patient/add_patient.xml"


class HomePage:
    """
    新增患者对话框
    """
    def __init__(self, driver):
        self.driver = driver
        self.my_photo_map = ObjectMap(home_page_path)
        # self.my_photo_map = ObjectMap(home_page_path)
        # self.patient = NavigateBar(self.driver)

    def click_my_photo(self):
        """
        打开新增患者对话框
        """
        wait = WebDriverWait(self.driver, 30)
        try:
            wait.until(lambda x: self.my_photo_map.getLocator(self.driver, 'My_Photo'))
        except TimeoutException:
            raise TimeoutException("超时！没找到 新增患者 按钮！")
        else:
            my_photo_btn = self.my_photo_map.getLocator(self.driver, 'My_Photo')
            my_photo_btn.click()

    def close_add_patient_dialog(self):
        """
        关闭新增患者对话框
        """
        close_btn = self.add_patient_map.getLocator(self.driver, 'CloseBtn')
        close_btn.click()

    def click_info_title(self):
        """
        单击 新增患者 对话框标题
        """
        title = self.my_photo_map.getLocator(self.driver, 'InfoTitle')
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
