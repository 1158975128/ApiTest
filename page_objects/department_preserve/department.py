import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from selenium.common.exceptions import NoSuchElementException
from page_objects.navigate_bar import NavigateBar
from utils.close_tips_tool import cancel_button
from utils.close_tips_tool import close_login_tips

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
department_type_map = map_path + "/department_preserve/department.xml"

delay_time = DelayTime.short_time.value


class Department_Type():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(department_type_map)
        self.department = NavigateBar(self.driver)

    def check_department_page(self):
        '''
        检查页面元素是否正确
        :return: new_additional,check_department_name,check_operation_name
        '''
        self.department.go_to_department()
        time.sleep(1)
        new_additional = self.version.getLocator(self.driver, "New_Additional").text
        check_department_name = self.version.getLocator(self.driver, 'Check_Department_Name').text
        time.sleep(1)
        check_operation_name = self.version.getLocator(self.driver, 'Check_Operation_Name').text
        return new_additional,check_department_name,check_operation_name

    def add_new_department(self,dep_name):
        '''
        新增部门
        :param dep_name: 新增部门的名称
        '''
        self.department.go_to_department()
        time.sleep(1)
        new_additional = self.version.getLocator(self.driver, "New_Additional")
        new_additional.click()
        import_words = self.version.getLocator(self.driver, "Import_Words")
        import_words.send_keys(dep_name)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            cancel_button(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '新增成功':
                cancel_button(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语:%s"%tips
            else:
                log.info("新增成功")
                return tips

    def find_department(self,name):
        """
        查找部门是否在列表中
        :param name: 部门
        :return: True/Flase
        """
        self.department.go_to_department()
        department_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(1) div')
        next_page = self.version.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            # print('执行了if')
            if len(department_names) == 10 and next_page.is_displayed():
                for department_name in department_names:
                    name_list.append(department_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for department_name in department_names:
                    name_list.append(department_name.get_attribute('textContent').strip())
                for department_name in name_list:
                    if department_name == name:
                        status = 1
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')


    def delete_department(self,department_name):
        '''
        删除部门
        :param department_name: 传入删除的部门名称
        Logic：遍历Tbody,根据传入的删除的部门名称找到对应的删除按钮，删除后跳出循环
        :except
        '''
        try:
            self.department.go_to_department()
            time.sleep(5)
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_td1s = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td1 in table_tr_td1s:
                    # 根据部门名称定位要删除的行
                    if table_tr_td1.get_attribute('textContent').strip() == department_name:
                        table_tr_td2s = table_trs[i].find_elements(By.TAG_NAME, value='td')
                        # 获取tr里第二个td的所有button
                        table_buttons = table_tr_td2s[1].find_elements(By.TAG_NAME, value='button')
                        for table_button in table_buttons:
                            if table_button.text == "删除":
                                table_button.click()
                                self.driver.implicitly_wait(10)
                                ensure = self.version.getLocator(self.driver, "Delete_Ensure")
                                ensure.click()

                        # 防止删除成功后少行超出范围
                        break
            log.info("删除成功")
        except:
            pass

    def change_department(self,department_name,change_depar):
        '''
        修改部门名称
        :param department_name: 传入删除的部门名称
        Logic：遍历Tbody,根据传入的修改的部门名称找到对应的删除按钮，修改后跳出循环
        '''
        self.department.go_to_department()
        time.sleep(5)
        table = self.version.getLocator(self.driver, 'Table')
        table_trs = table.find_elements(By.TAG_NAME,value='tr')
        for i in range(len(table_trs)):
            table_tr_td1s = table_trs[i].find_elements(By.TAG_NAME,value='td')
            for table_tr_td1 in table_tr_td1s:
                # 根据部门名称定位要修改的行
                if table_tr_td1.get_attribute('textContent').strip() == department_name:
                    table_tr_td2s = table_trs[i].find_elements(By.TAG_NAME,value='td')
                    # 获取tr里第二个td的所有button
                    table_buttons = table_tr_td2s[1].find_elements(By.TAG_NAME,value='button')
                    for table_button in table_buttons:
                        if table_button.text == "修改":
                            table_button.click()
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



