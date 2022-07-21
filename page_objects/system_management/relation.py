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
relation_map = map_path + "/system_management/unit.xml"

delay_time = DelayTime.short_time.value


class Relation():
    def __init__(self, driver):
        self.driver = driver
        self.relation = ObjectMap(relation_map)
        self.relation_page = NavigateBar(self.driver)


    def add_relation(self,name):
        self.relation_page.go_to_relation()
        time.sleep(1)
        new_additional = self.relation.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        title_name = self.relation.getLocator(self.driver, "Name")
        title_name.send_keys(name)
        ensure = self.relation.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.relation.getLocator(self.driver, 'Tips')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            log.info("新增成功")
            print(tips.get_attribute('textContent'))


    def find_relation(self,name):
        """
        查找关系是否在列表中
        :param name: 关系
        :return: True/Flase
        """
        self.relation_page.go_to_relation()
        relation_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.relation.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            if len(relation_names) == 10 and next_page.is_displayed():
                for relation_name in relation_names:
                    name_list.append(relation_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for relation_name in relation_names:
                    name_list.append(relation_name.get_attribute('textContent').strip())
                for relation_name in name_list:
                    if relation_name == name:
                        status = 1
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
