from selenium.webdriver.common.by import By
import time
import os
from utils.object_map import ObjectMap
from page_objects.navigate_bar import NavigateBar
from selenium.webdriver.common.action_chains import ActionChains

map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
display_map = map_path + "/arrange_query/arrange_display.xml"

class Arrange_Display():
    def __init__(self, driver):
        self.driver = driver
        self.display = ObjectMap(display_map)
        self.arrange = NavigateBar(self.driver)

    def screen_arrange(self,display_mode,name):
        self.arrange.go_to_arrange_display()
        time.sleep(1)
        new_additional = self.display.getLocator(self.driver, 'New_Additional')
        new_additional.click()
        name_input = self.display.getLocator(self.driver, 'Name')
        name_input.clear()
        name_input.send_keys(name)
        # 选择显示模式是半天还是全天
        fr_div = self.driver.find_element(By.CSS_SELECTOR, value='.is-guttered:nth-child(2) .code-input-box')
        fr_labels = fr_div.find_elements(By.TAG_NAME, value='label')
        for fr_label in fr_labels:
            if fr_label.get_attribute('textContent').strip() == display_mode:
                fr_label.click()
                time.sleep(1)
                break
        confirm = self.display.getLocator(self.driver, 'Confirm')
        confirm.click()
        time.sleep(1)

    def view_screen(self):
        '''
        点击查看按钮
        '''
        self.arrange.go_to_arrange_display()
        time.sleep(1)
        look = self.display.getLocator(self.driver, 'Look')
        look.click()
        time.sleep(1)

    def close_screen(self):
        title = self.display.getLocator(self.driver, 'Title')
        actions = ActionChains(self.driver)
        actions.move_to_element(title).click().perform()
        time.sleep(1)

    def arrange_display(self,patient_name,item,arrange_time):
        # 遍历获取当前页面显示得时间段，存放在字典里
        fr_div = self.driver.find_element(By.CSS_SELECTOR, value='.body .el-table__header-wrapper')
        fr_trs = fr_div.find_elements(By.TAG_NAME, value='th')
        count = 1
        dict_time = {}
        for fr_tr in fr_trs:
            dict_time[count] = fr_tr.get_attribute('textContent').strip()
            count += 1
        print(dict_time)
        # 遍历获取当前页面每个时间段得排班信息
        tbody = self.driver.find_element(By.CSS_SELECTOR, value='tbody')
        tbody_trs = tbody.find_elements(By.TAG_NAME, value='tr')
        for tbody_tr in tbody_trs:
            if patient_name in tbody_tr.get_attribute('textContent').strip():
                tbody_tds = tbody.find_elements(By.TAG_NAME, value='td')
                number = 1
                result_list = []
                for tbody_td in tbody_tds:
                    if item in tbody_td.get_attribute('textContent').strip():
                        if arrange_time == dict_time[number]:
                            result_list.append('匹配成功')
                            break
                    else:
                        result_list.append('匹配失败')
                    number += 1
                time.sleep(1)
                print(result_list)
                return result_list


