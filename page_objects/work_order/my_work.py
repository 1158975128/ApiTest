import os
import time
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.common.exceptions import StaleElementReferenceException
from utils.droplist_select_tool import Select
from utils.droplist_select_tool import select_droplist
from utils.close_tips_tool import left_corner_cancel
from utils.verify_arrange import verify_arrange


log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
my_work_map = map_path + "/work_order/my_work.xml"

delay_time = DelayTime.short_time.value

class My_Work():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(my_work_map)
        self.my_work = NavigateBar(self.driver)
        self.select = Select()

    def start_work(self,item,arrange,time_slot,device):
        '''
        工单排班
        :param item: 项目名称
        :param arrange: 排班状态（只点击未排班）
        :param time_slot: 排班的时间段
        :param device: 治疗设备
        '''
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.choose_arrange(self.driver,item,arrange)
        time.sleep(1)
        self.select.choose_scheduling(self.driver,time_slot)
        equipment = self.version.getLocator(self.driver, 'Treatment_Equipment')
        equipment.click()
        select_droplist(self.driver,device)
        save = self.version.getLocator(self.driver, 'Save')
        save.click()

    def arrange_tips(self,item,arrange,time_slot,device,tips):
        '''
        工单排班
        :param item: 项目名称
        :param arrange: 排班状态（只点击未排班）
        :param time_slot: 排班的时间段
        :param device: 治疗设备
        '''
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.choose_arrange(self.driver,item,arrange)
        time.sleep(1)
        self.select.choose_scheduling(self.driver,time_slot)
        equipment = self.version.getLocator(self.driver, 'Treatment_Equipment')
        equipment.click()
        select_droplist(self.driver,device)
        fr_tips = self.driver.find_elements(By.CSS_SELECTOR, value='.fr-tips')
        time.sleep(1)
        tips_list = []
        for fr_tip in fr_tips:
            if fr_tip.is_displayed():
                tips_list.append(fr_tip.get_attribute('textContent').strip())
        time.sleep(1)
        left_corner_cancel(self.driver)
        for tip in tips_list:
            if tip in tips:
                assert True
            else:
                assert False,'当前页面显示%d个;传入的提示语：%s;页面显示提示语：%s'%(len(tips_list),tips,tip)
        time.sleep(1)


    def verify_arrang(self,item,arrange_time):
        '''
        验证排班
        :param item:项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
        :param arrange_time:排班时间段
        '''
        self.my_work.go_to_therapeutist_arrange()
        time.sleep(1)
        verify_arrange(self.driver,item,arrange_time)

    def reroll_work(self,patient_name):
        try:
            self.my_work.go_to_work_order()
            time.sleep(5)
            # 测试患者2022_05_21_19_13_16
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_tds = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td_name in table_tr_tds:
                    print(table_tr_td_name.get_attribute('textContent'))
                    if table_tr_td_name.get_attribute('textContent').strip() == patient_name:
                        table_tr_td_name.click()
                        time.sleep(1)
                        reroll_work = self.version.getLocator(self.driver, "Reroll_Work")
                        reroll_work.click()
                        confirm = self.version.getLocator(self.driver, "Confirm")
                        confirm.click()
                        time.sleep(1)
                        start_work_tips = self.version.getLocator(self.driver, "Start_Work_Tips").text
                        print(start_work_tips)
                        if start_work_tips == '撤回工单成功':
                            return True
                        # 防止删除成功后少行超出范围
                        log.info("撤回工单成功")
                        break
        except StaleElementReferenceException:
            pass

    def end_work(self,patient_name):
        try:
            self.my_work.go_to_work_order()
            time.sleep(5)
            # 测试患者2022_05_21_19_13_16
            table = self.version.getLocator(self.driver, 'Table')
            table_trs = table.find_elements(By.TAG_NAME, value='tr')
            for i in range(len(table_trs)):
                table_tr_tds = table_trs[i].find_elements(By.TAG_NAME, value='td')
                for table_tr_td_name in table_tr_tds:
                    print(table_tr_td_name.get_attribute('textContent'))
                    if table_tr_td_name.get_attribute('textContent').strip() == patient_name:
                        table_tr_td_name.click()
                        time.sleep(1)
                        end_work = self.version.getLocator(self.driver, "End_Work")
                        end_work.click()
                        confirm = self.version.getLocator(self.driver, "Confirm")
                        confirm.click()
                        time.sleep(1)
                        start_work_tips = self.version.getLocator(self.driver, "Start_Work_Tips").text
                        print(start_work_tips)
                        if start_work_tips == '结束工单成功':
                            return True
                        # 防止删除成功后少行超出范围
                        log.info("撤回工单成功")
                        break
        except StaleElementReferenceException:
            pass

