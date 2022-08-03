import os
import time
import random
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.common.exceptions import NoSuchElementException
from utils.droplist_select_tool import select_droplist


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
workload_map = map_path + "/statistic_analysis/tharepist_workload.xml"

delay_time = DelayTime.short_time.value


class WorkLoad():
    def __init__(self, driver):
        self.driver = driver
        self.workload = ObjectMap(workload_map)
        self.workload_page = NavigateBar(self.driver)


    def workload_count(self,tharepist_workload):
        self.workload_page.go_to_therapist_workload()
        time.sleep(1)
        fr_body = self.driver.find_element(By.CSS_SELECTOR, value='.el-table__body-wrapper tbody')
        fr_trs = fr_body.find_elements(By.TAG_NAME, value='tr')
        count_list = []
        for fr_tr in fr_trs:
            if tharepist_workload == fr_tr.get_attribute('textContent').strip():
                count_list.append('匹配成功')
                break
            else:
                count_list.append('匹配失败')
        return count_list

    def see_work_detailed(self,tharepist_workload,job_type,name):
        self.workload_page.go_to_therapist_workload()
        time.sleep(1)
        type_select = self.workload.getLocator(self.driver, 'JobType')
        type_select.click()
        time.sleep(1)
        select_droplist(self.driver,job_type)
        fr_body = self.driver.find_element(By.CSS_SELECTOR, value='.el-table__body-wrapper tbody')
        fr_trs = fr_body.find_elements(By.TAG_NAME, value='tr')
        for fr_tr in fr_trs:
            if name in fr_tr.get_attribute('textContent').strip():
                detailed = fr_tr.find_element(By.CSS_SELECTOR,value='.go-detail')
                self.driver.execute_script("arguments[0].click();", detailed)
                time.sleep(1)
                fr_body = self.driver.find_element(By.CSS_SELECTOR, value='.el-table__body-wrapper tbody')
                fr_trs = fr_body.find_elements(By.TAG_NAME, value='tr')
                count_list = []
                for fr_tr in fr_trs:
                    if tharepist_workload in fr_tr.get_attribute('textContent').strip():
                        count_list.append(fr_tr.get_attribute('textContent').strip().split(' ')[0])
                    else:
                        count_list.append('匹配失败')
                return count_list
