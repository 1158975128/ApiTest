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
project_map = map_path + "/treatment_preserve/project_template.xml"

delay_time = DelayTime.short_time.value


class ProjectTemplate():
    def __init__(self, driver):
        self.driver = driver
        self.project = ObjectMap(project_map)
        self.project_page = NavigateBar(self.driver)

    def add_new_project(self,number,name,shorter):
        self.project_page.go_to_project_template()
        self.driver.implicitly_wait(20)
        new_additional = self.project.getLocator(self.driver, "New_Additional")
        new_additional.click()
        equ_number = self.project.getLocator(self.driver, "Number")
        equ_number.send_keys(number)
        equ_name = self.project.getLocator(self.driver, "Name")
        equ_name.send_keys(name)
        equ_shorter = self.project.getLocator(self.driver, "Shorter")
        equ_shorter.send_keys(shorter)
        ensure = self.project.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.project.getLocator(self.driver, 'Tips')
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
        close_tips = self.project.getLocator(self.driver, 'CloseTips')
        close_tips.click()
        time.sleep(1)

    def find_project_template(self,name):
        """
        查找项目模板是否在列表中
        :param name: 项目模板
        :return: True/Flase
        """
        self.project_page.go_to_project_template()
        project_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.project.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            # print('执行了if')
            if len(project_names) == 10 and next_page.is_displayed():
                for project_name in project_names:
                    name_list.append(project_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
                # print(name_list,status,flag)
            else:
                # print('执行了else')
                for project_name in project_names:
                    name_list.append(project_name.get_attribute('textContent').strip())
                for project_name in name_list:
                    if project_name == name:
                        status = 1
                # print(name_list)
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
