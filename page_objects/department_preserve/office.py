import os
import time
import random
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
office_map = map_path + "/department_preserve/office.xml"

delay_time = DelayTime.short_time.value


class Office():
    def __init__(self, driver):
        self.driver = driver
        self.office = ObjectMap(office_map)
        self.office_page = NavigateBar(self.driver)


    def add_office(self,name):
        self.office_page.go_to_office()
        time.sleep(1)
        new_additional = self.office.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        office_name = self.office.getLocator(self.driver, "Name")
        office_name.send_keys(name)
        ensure = self.office.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.office.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '新增成功':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s"%tips
            else:
                log.info("新增成功")
                return tips

    def close_login_tips(self):
        """
        关闭登录成功提示
        """
        close_tips = self.office.getLocator(self.driver, 'CloseTips')
        close_tips.click()
        time.sleep(1)

    def find_office(self,name):
        """
        查找科室是否在列表中
        :param name: 科室
        :return: True/Flase
        """
        self.office_page.go_to_office()
        office_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        # next_page = self.office.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        print(len(office_names))
        while flag:
            if len(office_names) == 10 and next_page.is_displayed():
                for office_name in office_names:
                    name_list.append(office_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for office_name in office_names:
                    name_list.append(office_name.get_attribute('textContent').strip())
                for office_name in name_list:
                    if office_name == name:
                        status = 1
                # print(name_list)
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
