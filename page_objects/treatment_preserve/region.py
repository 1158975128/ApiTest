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
from utils.close_tips_tool import cancel_button
from utils.close_tips_tool import close_login_tips

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
        time.sleep(1)
        new_additional = self.region.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        region_name = self.region.getLocator(self.driver, "Name")
        region_name.send_keys(name)
        ensure = self.region.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.region.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            cancel_button(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '新增成功':
                cancel_button(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败，提示语：%s"%tips
            else:
                log.info("新增成功")
                return tips

    def find_region(self,name):
        """
        查找部位是否在列表中
        :param name: 部位
        :return: True/Flase
        """
        self.region_page.go_to_region()
        region_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.region.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            if len(region_names) == 10 and next_page.is_displayed():
                for region_name in region_names:
                    name_list.append(region_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for region_name in region_names:
                    name_list.append(region_name.get_attribute('textContent').strip())
                for region_name in name_list:
                    if region_name == name:
                        status = 1
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
