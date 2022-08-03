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
from page_objects.arrange_query.verify_arrange import verify_arrange
from selenium.common.exceptions import NoSuchElementException
from utils.close_tips_tool import close_login_tips
from utils.close_tips_tool import cancel_button
from utils.scheduling_conflict_tips_tool import scheduling_conflict


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

    def arrange_work(self,item,arrange,time_slot,device):
        '''
        对状态为’未安排‘的工单排班
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
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            left_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '排班成功':
                left_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                log.info("新增成功")
                return tips

    def arrange_tips(self,item,arrange,time_slot,device,tips):
        '''
        工单排班提示
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
        scheduling_conflict(self.driver,tips)
        # fr_tips = self.driver.find_elements(By.CSS_SELECTOR, value='.fr-tips')
        # time.sleep(1)
        # tips_list = []
        # for fr_tip in fr_tips:
        #     if fr_tip.is_displayed():
        #         tips_list.append(fr_tip.get_attribute('textContent').strip())
        # time.sleep(1)
        # left_corner_cancel(self.driver)
        # for tip in tips_list:
        #     if tip in tips:
        #         assert True
        #     else:
        #         assert False,'当前页面显示%d个;传入的提示语：%s;页面显示提示语：%s'%(len(tips_list),tips,tip)
        # time.sleep(1)

    def grant_arrange_tips(self, item, time_slot, device, tips, operate):
        '''
        授权工单排班提示
        :param item: 项目名称
        :param arrange: 排班状态（只点击未排班）
        :param time_slot: 排班的时间段
        :param device: 治疗设备
        '''
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.checkbox(self.driver, item)
        self.select.my_order_operation(self.driver, operate)
        self.select.choose_scheduling(self.driver, time_slot)
        equipment = self.version.getLocator(self.driver, 'Treatment_Equipment')
        equipment.click()
        select_droplist(self.driver, device)
        scheduling_conflict(self.driver, tips)

    def grant(self,name,arrange,therapist,operate):
        '''
        工单授权
        :param name:治疗项目名称
        :param arrange:排班状态(未排班)
        :param therapist:被授权的治疗师名字
        :param operate:选择的操作（开始/结束/授权/排班）
        '''
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.choose_checkbox_arrange(self.driver,name,arrange)
        self.select.my_order_operation(self.driver,operate)
        grant = self.version.getLocator(self.driver, 'Grant')
        self.driver.execute_script("arguments[0].click();", grant)
        select_droplist(self.driver,therapist)
        time.sleep(1)
        ensure = self.version.getLocator(self.driver, 'Ensure')
        ensure.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent').strip()
        except NoSuchElementException:
            cancel_button(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '授权成功':
                cancel_button(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败，提示语：%s"%tips
            else:
                log.info("新增成功")
                return tips

    def gran_arrange_work(self,item,operate,time_slot,device):
        '''
        对授权的工单进行排班
        :param item: 项目名称
        :param operate: 排班
        :param time_slot: 排班时间段
        :param device: 治疗设备
        '''
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.checkbox(self.driver, item)
        self.select.my_order_operation(self.driver, operate)
        time.sleep(1)
        self.select.choose_scheduling(self.driver,time_slot)
        equipment = self.version.getLocator(self.driver, 'Treatment_Equipment')
        equipment.click()
        select_droplist(self.driver,device)
        save = self.version.getLocator(self.driver, 'Save')
        save.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            left_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '排班成功':
                left_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                log.info("新增成功")
                return tips

    def verify_arrang(self,item,arrange_time):
        '''
        验证治疗师排班
        :param item:项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
        :param arrange_time:排班时间段
        '''
        self.my_work.go_to_therapeutist_arrange()
        time.sleep(1)
        results = verify_arrange(self.driver,item,arrange_time)
        if '匹配成功' in results:
            assert True
        else:
            assert False,'页面没有匹配的排班数据'

    def verify_allarrang(self,item,arrange_time):
        '''
        验证全部排班
        :param item:项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
        :param arrange_time:排班时间段
        '''
        self.my_work.go_to_all_arrange()
        time.sleep(1)
        results = verify_arrange(self.driver,item,arrange_time)
        print(results)
        if '匹配成功' in results:
            assert True
        else:
            assert False,'页面没有匹配的排班数据,%s-->%s'%(arrange_time,item)

    def start_work(self,item,operate):
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.checkbox(self.driver, item)
        self.select.my_order_operation(self.driver, operate)
        time.sleep(1)
        confirm = self.version.getLocator(self.driver, 'Confirm')
        confirm.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            left_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '开始工单成功':
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                close_login_tips(self.driver)

    def operate_allwork(self,operate,exp_msg):
        self.my_work.go_to_work_order()
        time.sleep(1)
        all_choose = self.version.getLocator(self.driver, 'AllChoose')
        all_choose.click()
        time.sleep(1)
        self.select.my_order_operation(self.driver, operate)
        time.sleep(1)
        confirm = self.version.getLocator(self.driver, 'Confirm')
        confirm.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            if tips != exp_msg:
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                close_login_tips(self.driver)

    def end_work(self,item,operate):
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.checkbox(self.driver, item)
        time.sleep(1)
        self.select.my_order_operation(self.driver, operate)
        time.sleep(1)
        confirm = self.version.getLocator(self.driver, 'Confirm')
        confirm.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            if tips != '结束工单成功':
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                close_login_tips(self.driver)

    def button_status(self):
        self.my_work.go_to_work_order()
        time.sleep(1)
        all_choose = self.version.getLocator(self.driver, 'AllChoose')
        all_choose.click()
        time.sleep(1)
        fr_p = self.driver.find_element(By.CSS_SELECTOR, value='.my-order-table-controller')
        fr_buttons = fr_p.find_elements(By.TAG_NAME, value='button')
        status_list = ''
        for fr_button in fr_buttons:
            # print(fr_button.get_attribute('class'))
            if 'is-disabled' in fr_button.get_attribute('class'):
                status_list += fr_button.get_attribute('textContent').strip()
        return status_list

    def inner_start_end(self,item,arrange):
        self.my_work.go_to_work_order()
        time.sleep(1)
        self.select.click_item_name(self.driver,item,arrange)
        time.sleep(1)
        start_work = self.version.getLocator(self.driver, 'Start_Work')
        start_work.click()
        time.sleep(1)
        start_ensure = self.version.getLocator(self.driver, 'EnsureInner')
        start_ensure.click()
        time.sleep(1)
        end_work = self.version.getLocator(self.driver, 'End_Work')
        self.driver.execute_script("arguments[0].click();", end_work)
        # end_work.click()
        time.sleep(3)
        # close_login_tips(self.driver)
        # time.sleep(1)
        end_ensure = self.version.getLocator(self.driver, 'EnsureInner')
        end_ensure.click()
        # self.driver.execute_script("arguments[0].click();", end_ensure)
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            left_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            if tips != '结束工单成功':
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            # else:
                close_login_tips(self.driver)



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