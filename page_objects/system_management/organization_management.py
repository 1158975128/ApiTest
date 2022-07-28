import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from utils.close_tips_tool import cancel_button
from utils.close_tips_tool import close_login_tips
from selenium.common.exceptions import NoSuchElementException
from page_objects.navigate_bar import NavigateBar


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
org_map = map_path + "/system_management/organization_management.xml"

delay_time = DelayTime.short_time.value


class OrgManage:
    """
    新增患者对话框
    """
    def __init__(self, driver):
        self.driver = driver
        self.orga_map = ObjectMap(org_map)
        self.org_page = NavigateBar(self.driver)


    def add_new_org(self,org_name):
        '''
        新增机构
        :param disease_name: 新增机构的名称
        '''
        self.org_page.go_to_organization_management()
        time.sleep(1)
        new_additional = self.orga_map.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        import_words = self.orga_map.getLocator(self.driver, "Import_Words")
        import_words.clear()
        time.sleep(1)
        import_words.send_keys(org_name)
        ensure = self.orga_map.getLocator(self.driver, "Ensure")
        ensure.click()
        try:
            tips = self.orga_map.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            cancel_button(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '新增成功':
                cancel_button(self.driver)
                close_login_tips(self.driver)
                # assert False, "新增失败,提示语:%s"%tips
            else:
                log.info("新增成功")
                return tips
