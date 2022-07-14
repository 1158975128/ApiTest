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
region_map = map_path + "/treatment_preserve/region.xml"

delay_time = DelayTime.short_time.value


class Region():
    def __init__(self, driver):
        self.driver = driver
        self.region = ObjectMap(region_map)
        self.region_page = NavigateBar(self.driver)

    def add_new_region(self,name):
        self.region_page.go_to_region()
        self.driver.implicitly_wait(20)
        new_additional = self.region.getLocator(self.driver, "New_Additional")
        new_additional.click()
        region_name = self.region.getLocator(self.driver, "Name")
        region_name.send_keys(name)
        ensure = self.region.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.region.getLocator(self.driver, 'Tips')
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
        close_tips = self.region.getLocator(self.driver, 'CloseTips')
        close_tips.click()
        time.sleep(1)

    def find_region(self,name):
        """
        查找部门名称是否在列表中
        :param name: 部门名称
        :return: True/Flase
        """
        self.region_page.go_to_region()
        region_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.region.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            # print('执行了if')
            if len(region_names) == 10 and next_page.is_displayed():
                for region_name in region_names:
                    name_list.append(region_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
                # print(name_list,status,flag)
            else:
                # print('执行了else')
                for region_name in region_names:
                    # print(disease_name.get_attribute('textContent').strip(),flag)
                    name_list.append(region_name.get_attribute('textContent').strip())
                for region_name in name_list:
                    if region_name == name:
                        status = 1
                # print(name_list)
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
