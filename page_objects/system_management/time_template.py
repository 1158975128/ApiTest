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
time_template_map = map_path + "/system_management/time_template.xml"

delay_time = DelayTime.short_time.value


class TimeTemplate():
    def __init__(self, driver):
        self.driver = driver
        self.time_template = ObjectMap(time_template_map)
        self.time_template_page = NavigateBar(self.driver)


    def add_time_template(self,mstart,mend,mlength,mgap,astart,aend,alength,agap):
        self.time_template_page.go_to_timetemplate()
        time.sleep(1)
        Mstart = self.time_template.getLocator(self.driver, "MStart")
        Mstart.send_keys(mstart)
        Mend = self.time_template.getLocator(self.driver, "MEnd")
        Mend.send_keys(mend)
        Mlength = self.time_template.getLocator(self.driver, "MLength")
        Mlength.send_keys(mlength)
        Mgap = self.time_template.getLocator(self.driver, "MGap")
        Mgap.send_keys(mgap)
        Astart = self.time_template.getLocator(self.driver, "AStart")
        Astart.send_keys(astart)
        Aend = self.time_template.getLocator(self.driver, "AEnd")
        Aend.send_keys(aend)
        Alength = self.time_template.getLocator(self.driver, "ALength")
        Alength.send_keys(alength)
        Agap = self.time_template.getLocator(self.driver, "AGap")
        Agap.send_keys(agap)
        ensure = self.time_template.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        time_list = self.driver.find_element(By.CSS_SELECTOR,value='[class="timeList"]')
        print('时间模板内容',time_list.get_attribute('textContent'))
        try:
            tips = self.time_template.getLocator(self.driver, 'Tips')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            log.info("新增成功")
            print('提示语：',tips.get_attribute('textContent'))
