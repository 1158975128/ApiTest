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
disease_map = map_path + "/treatment_preserve/disease_type.xml"

delay_time = DelayTime.short_time.value


class Disease_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(disease_map)
        self.disease_page = NavigateBar(self.driver)


    def check_disease_page(self):
        '''
        检查页面元素是否正确
        :return: new_additional,check_department_name,check_operation_name
        '''
        self.disease_page.go_to_disease()
        time.sleep(1)
        new_additional = self.version.getLocator(self.driver, "New_Additional").text.strip()
        Check_Position_Names = self.driver.find_elements(By.CSS_SELECTOR,value='.el-table__header-wrapper th div')
        position = Check_Position_Names[0].get_attribute('textContent').strip()
        create_time = Check_Position_Names[1].get_attribute('textContent').strip()
        modification_time = Check_Position_Names[2].get_attribute('textContent').strip()
        operation = Check_Position_Names[3].get_attribute('textContent').strip()
        return new_additional,position,create_time,modification_time,operation


    def add_new_disease(self,disease_name):
        '''
        新增疾病类型
        :param disease_name: 新增疾病的名称
        '''
        self.disease_page.go_to_disease()
        self.driver.implicitly_wait(20)
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys(disease_name)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        log.info("新增成功")

    def find_disease_name(self,name):
        """
        查找部门名称是否在列表中
        :param name: 部门名称
        :return: True/Flase
        """
        self.disease_page.go_to_disease()
        disease_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.version.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            # print('执行了if')
            if len(disease_names) == 10 and next_page.is_displayed():
                for disease_name in disease_names:
                    name_list.append(disease_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
                # print(name_list,status,flag)
            else:
                # print('执行了else')
                for disease_name in disease_names:
                    # print(disease_name.get_attribute('textContent').strip(),flag)
                    name_list.append(disease_name.get_attribute('textContent').strip())
                for disease_name in name_list:
                    if disease_name == name:
                        status = 1
                # print(name_list)
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')


    def delete_disease(self,disease_name):
        '''
        删除部门
        :param department_name: 传入删除的部门名称
        Logic：遍历Tbody,根据传入的删除的部门名称找到对应的删除按钮，删除后跳出循环
        :except
        '''
        try:
            self.disease_page.go_to_disease()
            time.sleep(1)
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_td1s = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td1 in table_tr_td1s:
                    #     # 根据部门名称定位要删除的行
                    if table_tr_td1.get_attribute('textContent').strip() == disease_name:
                        table_tr_td2s = table_trs[i].find_elements(By.TAG_NAME, value='td')
                        #         # 获取tr里第二个td的所有button
                        table_buttons = table_tr_td2s[3].find_elements(By.TAG_NAME, value='button')
                        for table_button in table_buttons:
                            if table_button.text == "删除":
                                table_button.click()
                                time.sleep(1)
                                self.driver.implicitly_wait(10)
                                ensure = self.version.getLocator(self.driver, "Delete_Ensure")
                                ensure.click()
                                time.sleep(1)
                        # 防止删除成功后少行超出范围
                        break
            log.info("删除成功")
        except:
            pass

    def modify_disease(self,disease_name,change_disease):
        '''
        修改部门名称
        :param department_name: 传入删除的部门名称
        Logic：遍历Tbody,根据传入的修改的部门名称找到对应的删除按钮，修改后跳出循环
        '''
        self.disease_page.go_to_disease()
        time.sleep(3)
        table = self.version.getLocator(self.driver, 'Table')
        table_trs = table.find_elements(By.TAG_NAME,value='tr')
        for i in range(len(table_trs)):
            table_tr_td1s = table_trs[i].find_elements(By.TAG_NAME,value='td')
            for table_tr_td1 in table_tr_td1s:
            #     # 根据部门名称定位要删除的行
                if table_tr_td1.get_attribute('textContent').strip() == disease_name:
                    table_tr_td2s = table_trs[i].find_elements(By.TAG_NAME,value='td')
            #         # 获取tr里第二个td的所有button
                    table_buttons = table_tr_td2s[3].find_elements(By.TAG_NAME,value='button')
                    for table_button in table_buttons:
                        if table_button.text == "修改":
                            table_button.click()
                            self.driver.implicitly_wait(20)
                            import_words = self.version.getLocator(self.driver, "Import_Words")
                            import_words.clear()
                            import_words.send_keys(change_disease)
                            ensure = self.version.getLocator(self.driver, "Ensure")
                            ensure.click()
                            time.sleep(1)
                    # 修改成功后跳出循环
                    break
        log.info("修改成功")
