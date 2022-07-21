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
lease_map = map_path + "/system_management/lease.xml"

delay_time = DelayTime.short_time.value


class Lease():
    def __init__(self, driver):
        self.driver = driver
        self.lease = ObjectMap(lease_map)
        self.lease_page = NavigateBar(self.driver)


    def add_lease(self,name,number,price):
        self.lease_page.go_to_lease()
        time.sleep(1)
        new_additional = self.lease.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        device_name = self.lease.getLocator(self.driver, "Name")
        device_name.send_keys(name)
        device_number = self.lease.getLocator(self.driver, "Number")
        device_number.send_keys(number)
        device_price = self.lease.getLocator(self.driver, "Price")
        device_price.send_keys(price)
        ensure = self.lease.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.lease.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '添加成功':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败，提示语：%s"%tips
            else:
                log.info("新增成功")
                return tips


    def find_lease(self,name):
        """
        查找租赁设备是否在列表中
        :param name: 租赁设备
        :return: True/Flase
        """
        self.lease_page.go_to_lease()
        lease_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.lease.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            if len(lease_names) == 10 and next_page.is_displayed():
                for lease_name in lease_names:
                    name_list.append(lease_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for lease_name in lease_names:
                    name_list.append(lease_name.get_attribute('textContent').strip())
                for lease_name in name_list:
                    if lease_name == name:
                        status = 1
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
