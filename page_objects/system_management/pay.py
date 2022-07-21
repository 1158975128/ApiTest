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
pay_map = map_path + "/system_management/pay.xml"

delay_time = DelayTime.short_time.value


class Pay():
    def __init__(self, driver):
        self.driver = driver
        self.pay = ObjectMap(pay_map)
        self.pay_page = NavigateBar(self.driver)


    def add_pay(self):
        self.pay_page.go_to_pay()
        time.sleep(1)
        operation = self.pay.getLocator(self.driver, "Operation")
        operation.click()
        time.sleep(1)
        ensure = self.pay.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.pay.getLocator(self.driver, 'Tips')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            log.info("新增成功")