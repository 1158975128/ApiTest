import os
import time
import random
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.common.exceptions import NoSuchElementException


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
title_map = map_path + "/department_preserve/job_type.xml"

delay_time = DelayTime.short_time.value


class Title():
    def __init__(self, driver):
        self.driver = driver
        self.title = ObjectMap(title_map)
        self.title_page = NavigateBar(self.driver)


    def add_title(self,name):
        self.title_page.go_to_title()
        self.driver.implicitly_wait(20)
        new_additional = self.title.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        title_name = self.title.getLocator(self.driver, "Name")
        title_name.send_keys(name)
        ensure = self.title.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.title.getLocator(self.driver, 'Tips')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            log.info("新增成功")
            print(tips.get_attribute('textContent'))
            return tips.get_attribute('textContent')

    def close_login_tips(self):
        """
        关闭登录成功提示
        """
        close_tips = self.title.getLocator(self.driver, 'CloseTips')
        close_tips.click()
        time.sleep(1)

    def find_title(self,name):
        """
        查找职称是否在列表中
        :param name: 职称
        :return: True/Flase
        """
        self.title_page.go_to_title()
        title_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.title.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            # print('执行了if')
            if len(title_names) == 10 and next_page.is_displayed():
                for title_name in title_names:
                    name_list.append(title_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
                # print(name_list,status,flag)
            else:
                # print('执行了else')
                for title_name in title_names:
                    # print(disease_name.get_attribute('textContent').strip(),flag)
                    name_list.append(title_name.get_attribute('textContent').strip())
                for title_name in name_list:
                    if title_name == name:
                        status = 1
                # print(name_list)
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
