import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from utils.exist_tool import Element
from page_objects.navigate_bar_all.navigate_version import VersionExpanded
from selenium.common.exceptions import StaleElementReferenceException
import pytest_check as check


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
work_clock_in = map_path + "/arrange_order/work_clock_in.xml"

delay_time = DelayTime.short_time.value

class Work_Clock_In():
    def __init__(self, driver):
        self.driver = driver
        self.login = ObjectMap(work_clock_in)
        self.my_work = NavigateBar(self.driver)

    def start_work(self,work_name):
        self.element = Element(self.driver)
        result = self.element.isElementPresent('.submit-box-title')
        if result:
            comfirm = self.login.getLocator(self.driver, 'Confirm')
            input_work = self.login.getLocator(self.driver, 'Input')
            input_work.click()
            time.sleep(1)
            input_work.send_keys(work_name)
            time.sleep(1)
            select_work = self.login.getLocator(self.driver, 'Select_work')
            select_work.click()
            time.sleep(1)
            comfirm.click()
            print('岗位打卡成功！')
        else:
            print("已经打过卡")



