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
from utils.close_tips_tool import right_corner_cancel
from utils.close_tips_tool import close_login_tips

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
project_map = map_path + "/treatment_preserve/project_template.xml"

delay_time = DelayTime.short_time.value


def select_droplist(driver,name):
    droplist_ul = driver.find_element(By.CSS_SELECTOR, value='[aria-hidden="false"] ul')
    droplist_lis = droplist_ul.find_elements(By.TAG_NAME, value='li')
    for droplist_li in droplist_lis:
        # print(droplist_li.get_attribute('textContent').strip())
        if droplist_li.get_attribute('textContent').strip() == name:
            droplist_li.click()
            time.sleep(1)
            break

class ProjectTemplate():
    def __init__(self, driver):
        self.driver = driver
        self.project = ObjectMap(project_map)
        self.project_page = NavigateBar(self.driver)

    def add_new_project(self,number,name,p_type,bedside,unit,dose,price,freq,cycle,t_time,j_type):
        self.project_page.go_to_project_template()
        time.sleep(1)
        new_additional = self.project.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        # 项目编号
        p_number = self.project.getLocator(self.driver, "Number")
        p_number.send_keys(number)
        # 项目名称
        p_name = self.project.getLocator(self.driver, "Name")
        p_name.send_keys(name)
        # 项目类型
        project_type = self.project.getLocator(self.driver, "Type")
        project_type.click()
        self.driver.implicitly_wait(100)
        select_droplist(self.driver,p_type)
        # 是否床边
        if bedside == '是':
            bedside_y = self.project.getLocator(self.driver, "BedsideY")
            bedside_y.click()
        elif bedside == '否':
            bedside_n = self.project.getLocator(self.driver, "BedsideN")
            bedside_n.click()
        # 剂量单位
        p_unit = self.project.getLocator(self.driver, "Unit")
        p_unit.click()
        self.driver.implicitly_wait(100)
        select_droplist(self.driver,unit)
        # 剂量
        p_dose = self.project.getLocator(self.driver, "Dose")
        p_dose.clear()
        p_dose.send_keys(dose)
        # 剂量单价
        dose_price = self.project.getLocator(self.driver, "DosePrice")
        dose_price.send_keys(price)
        # 频次
        frequency = self.project.getLocator(self.driver, "Frequency")
        frequency.send_keys(freq)
        # 长短期
        slect_time = self.project.getLocator(self.driver, "Time")
        slect_time.click()
        self.driver.implicitly_wait(100)
        select_droplist(self.driver,cycle)
        if cycle == '短期':
            # 总次数
            times = self.project.getLocator(self.driver, "TreatTimes")
            times.clear()
            times.send_keys('1')
        time.sleep(1)
        # 治疗时长
        treat_time = self.project.getLocator(self.driver, "TreatTime")
        treat_time.send_keys(t_time)
        time.sleep(1)
        # 岗位小类
        job_type = self.project.getLocator(self.driver, "JobType")
        job_type.click()
        self.driver.implicitly_wait(100)
        select_droplist(self.driver,j_type)
        # 点击确定
        ensure = self.project.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.project.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            time.sleep(1)
            if tips != '新增成功':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s"%tips
            else:
                log.info("新增成功")
                return tips


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
