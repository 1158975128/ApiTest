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
from selenium.common.exceptions import StaleElementReferenceException
import pytest_check as check


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
my_work_map = map_path + "/work_order/my_work.xml"

delay_time = DelayTime.short_time.value

j = 0

class My_Work():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(my_work_map)
        self.my_work = NavigateBar(self.driver)

    def check_my_work_page(self):
        check.equal(1,2,"判断是否相等")
        # self.my_work.go_to_work_order()
        # my_order_controller = self.version.getLocator(self.driver, "My_Order_Controller").get_attribute('textContent').strip()
        # gutter = self.version.getLocator(self.driver, "Gutter").get_attribute('textContent').strip()
        # if my_order_controller == '选中 0 条 开始  结束  授权  排班' and gutter == '姓名疾病类型项目名称治疗设备排班开始治疗师操作':
        #     return True
        # else:
        #     return False

    def start_work(self,patient_name):
        try:
            self.my_work.go_to_work_order()
            time.sleep(5)
            # 测试患者2022_05_21_19_13_16
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_tds = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td_name in table_tr_tds:
                    print(table_tr_td_name.get_attribute('textContent'))
                    if table_tr_td_name.get_attribute('textContent').strip() == patient_name:
                        table_tr_td_name.click()
                        time.sleep(1)
                        treatment_scheduling = self.version.getLocator(self.driver, "Treatment_Scheduling")
                        treatment_scheduling.click()
                        time.sleep(3)
                        treatment_time = self.version.getLocator(self.driver, "Treatment_Time")
                        treatment_time.click()
                        treatment_equipment = self.version.getLocator(self.driver, "Treatment_Equipment")
                        treatment_equipment.click()
                        choose_treatment = self.version.getLocator(self.driver, "Choose_Treatment")
                        choose_treatment.click()
                        save = self.version.getLocator(self.driver, "Save")
                        save.click()
                        time.sleep(3)
                        start_work = self.version.getLocator(self.driver, "Start_Work")
                        start_work.click()
                        confirm = self.version.getLocator(self.driver, "Confirm")
                        confirm.click()
                        time.sleep(1)
                        start_work_tips = self.version.getLocator(self.driver, "Start_Work_Tips").text
                        print(start_work_tips)
                        if start_work_tips == '开始工单成功':
                            return True
                        log.info("开始工单成功")
                        # 防止删除成功后少行超出范围
                        break
        except StaleElementReferenceException:
            pass

    def reroll_work(self,patient_name):
        try:
            self.my_work.go_to_work_order()
            time.sleep(5)
            # 测试患者2022_05_21_19_13_16
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_tds = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td_name in table_tr_tds:
                    print(table_tr_td_name.get_attribute('textContent'))
                    if table_tr_td_name.get_attribute('textContent').strip() == patient_name:
                        table_tr_td_name.click()
                        time.sleep(1)
                        reroll_work = self.version.getLocator(self.driver, "Reroll_Work")
                        reroll_work.click()
                        confirm = self.version.getLocator(self.driver, "Confirm")
                        confirm.click()
                        time.sleep(1)
                        start_work_tips = self.version.getLocator(self.driver, "Start_Work_Tips").text
                        print(start_work_tips)
                        if start_work_tips == '撤回工单成功':
                            return True
                        # 防止删除成功后少行超出范围
                        log.info("撤回工单成功")
                        break
        except StaleElementReferenceException:
            pass

    def end_work(self,patient_name):
        try:
            self.my_work.go_to_work_order()
            time.sleep(5)
            # 测试患者2022_05_21_19_13_16
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_tds = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td_name in table_tr_tds:
                    print(table_tr_td_name.get_attribute('textContent'))
                    if table_tr_td_name.get_attribute('textContent').strip() == patient_name:
                        table_tr_td_name.click()
                        time.sleep(1)
                        end_work = self.version.getLocator(self.driver, "End_Work")
                        end_work.click()
                        confirm = self.version.getLocator(self.driver, "Confirm")
                        confirm.click()
                        time.sleep(1)
                        start_work_tips = self.version.getLocator(self.driver, "Start_Work_Tips").text
                        print(start_work_tips)
                        if start_work_tips == '结束工单成功':
                            return True
                        # 防止删除成功后少行超出范围
                        log.info("撤回工单成功")
                        break
        except StaleElementReferenceException:
            pass

