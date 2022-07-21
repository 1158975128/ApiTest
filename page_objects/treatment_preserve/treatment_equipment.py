import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.common.exceptions import NoSuchElementException
from utils.close_tips_tool import right_corner_cancel
from utils.close_tips_tool import close_login_tips

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
equipment_map = map_path + "/treatment_preserve/treatment_equipment.xml"

delay_time = DelayTime.short_time.value


class Equipment():
    def __init__(self, driver):
        self.driver = driver
        self.equipment = ObjectMap(equipment_map)
        self.equipment_page = NavigateBar(self.driver)

    def add_new_equipment(self,number,name,shorter):
        self.equipment_page.go_to_treatment_equipment()
        time.sleep(1)
        new_additional = self.equipment.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        equ_number = self.equipment.getLocator(self.driver, "Number")
        equ_number.send_keys(number)
        equ_name = self.equipment.getLocator(self.driver, "Name")
        equ_name.send_keys(name)
        equ_shorter = self.equipment.getLocator(self.driver, "Shorter")
        equ_shorter.send_keys(shorter)
        ensure = self.equipment.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.equipment.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '新增成功':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败，提示语：%s"%tips
            else:
                log.info("新增成功")
                return tips

    def find_equipment_name(self,name):
        """
        查找治疗设备是否在列表中
        :param name: 治疗设备
        :return: True/Flase
        """
        self.equipment_page.go_to_treatment_equipment()
        disease_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.equipment.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            if len(disease_names) == 10 and next_page.is_displayed():
                for disease_name in disease_names:
                    name_list.append(disease_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for disease_name in disease_names:
                    name_list.append(disease_name.get_attribute('textContent').strip())
                for disease_name in name_list:
                    if disease_name == name:
                        status = 1
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
