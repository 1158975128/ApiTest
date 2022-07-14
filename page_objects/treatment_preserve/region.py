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
        self.driver.implicitly_wait(20)
        new_additional = self.equipment.getLocator(self.driver, "New_Additional")
        new_additional.click()
        equ_number = self.equipment.getLocator(self.driver, "Number")
        equ_number.send_keys(number)
        equ_name = self.equipment.getLocator(self.driver, "Name")
        equ_name.send_keys(name)
        equ_shorter = self.equipment.getLocator(self.driver, "Shorter")
        equ_shorter.send_keys(shorter)
        ensure = self.equipment.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.equipment.getLocator(self.driver, 'Tips')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            log.info("新增成功")
            return tips.get_attribute('textContent')

    def close_login_tips(self):
        """
        关闭登录成功提示
        """
        close_tips = self.equipment.getLocator(self.driver, 'CloseTips')
        close_tips.click()
        time.sleep(1)

    def find_equipment_name(self,name):
        """
        查找部门名称是否在列表中
        :param name: 部门名称
        :return: True/Flase
        """
        self.equipment_page.go_to_treatment_equipment()
        disease_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.equipment.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            # print('执行了if')
            if len(disease_names) == 10 and next_page.is_displayed():
                for disease_name in disease_names:
                    name_list.append(disease_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
                # print(name_list,status,flag)
            else:
                # print('执行了else')
                for disease_name in disease_names:
                    # print(disease_name.get_attribute('textContent').strip(),flag)
                    name_list.append(disease_name.get_attribute('textContent').strip())
                for disease_name in name_list:
                    if disease_name == name:
                        status = 1
                # print(name_list)
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
