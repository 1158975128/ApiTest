import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from page_objects.navigate_bar_all.navigate_version import VersionExpanded

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
disease_type_map = map_path + "/department_preserve/department.xml"

delay_time = DelayTime.short_time.value


class Department_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(disease_type_map)
        self.department = NavigateBar(self.driver)
        # self.management = VersionExpanded(self.driver)
        # self.management.navigate_version_control()

    def check_department_page(self):
        self.department.go_to_department()
        time.sleep(1)
        new_additional = self.version.getLocator(self.driver, "New_Additional").text
        check_department_name = self.version.getLocator(self.driver, 'Check_Department_Name').text
        time.sleep(1)
        check_operation_name = self.version.getLocator(self.driver, 'Check_Operation_Name').text
        return new_additional,check_department_name,check_operation_name




    def add_new_department(self,dep_name):
        self.department.go_to_department()
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys(dep_name)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        log.info("新增成功")

    def find_department_name(self,name):
        """
        查找部门名称是否在列表中
        :param name: 部门名称
        :return: True/Flase
        """
        self.department.go_to_department()
        department_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        flag = 0
        for department_name in department_names:
            if department_name.text == name:
                flag = 1
        if flag == 1:
            return True
        else:
            print('没有匹配的'+name)
            return False


    def delete_department(self,department_name):
        self.department.go_to_department()
        table = self.version.getLocator(self.driver, 'Table')
        tabletrs = table.find_elements_by_tag_name('tr')
        for i in range(1,len(tabletrs)+1):
            path = ".is-scrolling-none tr:nth-child({}) td:nth-child({}) div".format(i, 1)
            tablediv = self.driver.find_element_by_css_selector(path)
            trs = tabletrs[i-1]
            # 根据部门名称定位要删除的行
            if tablediv.text == department_name:
                tabletds = trs.find_elements_by_tag_name('td')
                td = tabletds[1]
                tablebuttons = td.find_elements_by_tag_name('button')
                for tablebutton in tablebuttons:
                    if tablebutton.text =="删除":
                        tablebutton.click()
                        self.driver.implicitly_wait(10)
                        ensure = self.version.getLocator(self.driver, "Delete_Ensure")
                        ensure.click()
                # 防止删除成功后少行超出范围
                break
        log.info("删除成功")

    def change_department(self,department_name,change_depar):
        self.department.go_to_department()
        table = self.version.getLocator(self.driver, 'Table')
        tabletrs = table.find_elements_by_tag_name('tr')
        for i in range(1,len(tabletrs)+1):
            path = ".is-scrolling-none tr:nth-child({}) td:nth-child({}) div".format(i, 1)
            tablediv = self.driver.find_element_by_css_selector(path)
            trs = tabletrs[i-1]
            # 根据部门名称定位要修改的行
            if tablediv.text == department_name:
                tabletds = trs.find_elements_by_tag_name('td')
                td = tabletds[1]
                tablebuttons = td.find_elements_by_tag_name('button')
                for tablebutton in tablebuttons:
                    if tablebutton.text =="修改":
                        tablebutton.click()
                        self.driver.implicitly_wait(20)
                        import_words = self.version.getLocator(self.driver, "Import_Words")
                        import_words.clear()
                        import_words.send_keys(change_depar)
                        ensure = self.version.getLocator(self.driver, "Ensure")
                        ensure.click()
                        time.sleep(1)
                # 修改成功后跳出循环
                break
        log.info("修改成功")



