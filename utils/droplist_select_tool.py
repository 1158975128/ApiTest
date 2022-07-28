from selenium.webdriver.common.by import By
import time


def select_droplist(driver,name):
    droplist_ul = driver.find_element(By.CSS_SELECTOR, value='[aria-hidden="false"] ul')
    droplist_lis = droplist_ul.find_elements(By.TAG_NAME, value='li')
    for droplist_li in droplist_lis:
        # print(droplist_li.get_attribute('textContent').strip())
        if droplist_li.get_attribute('textContent').strip() == name:
            droplist_li.click()
            time.sleep(1)
            break

class Select:
    def select_sex(self,driver,name):
        '''
        :param name: 传入选择的性别，男/女
        '''
        fr_div = driver.find_element(By.CSS_SELECTOR, value='label[for="sex"]+.el-form-item__content')
        fr_spans = fr_div.find_elements(By.TAG_NAME, value='span')
        for fr_span in fr_spans:
            # print(droplist_li.get_attribute('textContent').strip())
            if fr_span.get_attribute('textContent').strip() == name:
                fr_span.click()
                time.sleep(1)
                break

    def bed_side(self,driver,name):
        '''
        :param name: 传入选择的床旁，是/否
        '''
        fr_div = driver.find_element(By.CSS_SELECTOR, value='label[for="bedside"]+.el-form-item__content')
        fr_spans = fr_div.find_elements(By.TAG_NAME, value='span')
        for fr_span in fr_spans:
            # print(droplist_li.get_attribute('textContent').strip())
            if fr_span.get_attribute('textContent').strip() == name:
                fr_span.click()
                time.sleep(1)
                break

    def checkbox(self,driver,name):
        '''
        点击复选框
        :param name: 传入项目名称
        '''
        fr_body = driver.find_element(By.CSS_SELECTOR, value='.el-table__body-wrapper .el-table__body')
        fr_trs = fr_body.find_elements(By.TAG_NAME, value='tr')
        for fr_tr in fr_trs:
            # print(fr_tr.get_attribute('textContent').strip())
            if name in fr_tr.get_attribute('textContent').strip():
                fr_tr.find_element(By.CSS_SELECTOR, value='.el-checkbox__input').click()
                break

    def choose_arrange(self,driver,name,arrange):
        '''
        点击复选框
        :param name: 传入项目名称和排班状态
        '''
        fr_body = driver.find_element(By.CSS_SELECTOR, value='.el-table__body-wrapper .el-table__body')
        fr_trs = fr_body.find_elements(By.TAG_NAME, value='tr')
        for fr_tr in fr_trs:
            # print(name,arrange,'---->',fr_tr.get_attribute('textContent').strip())
            if name in fr_tr.get_attribute('textContent').strip() and arrange in fr_tr.get_attribute('textContent').strip():
                fr_tr.find_element(By.CSS_SELECTOR, value='.color-danger').click()
                break

    def choose_limit_time(self,driver,name):
        '''
        患者管理--》治疗项目选择长期项目或短期项目
        '''
        fr_p = driver.find_element(By.CSS_SELECTOR, value='.treat-ptoject-type')
        fr_spans = fr_p.find_elements(By.TAG_NAME, value='span')
        for fr_span in fr_spans:
            # print(fr_span.get_attribute('textContent').strip())
            if fr_span.get_attribute('textContent').strip() == name:
                fr_span.click()
                time.sleep(1)
                break

    def choose_operation(self,driver,name):
        '''
        患者管理选择指定/结束
        '''
        fr_p = driver.find_element(By.CSS_SELECTOR, value='.patient-project-table-controller')
        fr_buttons = fr_p.find_elements(By.TAG_NAME, value='button')
        for fr_button in fr_buttons:
            # print(fr_button.get_attribute('textContent').strip())
            if fr_button.get_attribute('textContent').strip() == name:
                fr_button.click()
                time.sleep(1)
                break

    def my_order_operation(self,driver,name):
        '''
        我的工单选择开始/结束/授权/排班
        '''
        fr_p = driver.find_element(By.CSS_SELECTOR, value='.my-order-table-controller')
        fr_buttons = fr_p.find_elements(By.TAG_NAME, value='button')
        for fr_button in fr_buttons:
            # print(fr_button.get_attribute('textContent').strip())
            if fr_button.get_attribute('textContent').strip() == name:
                fr_button.click()
                time.sleep(1)
                break

    def choose_scheduling(self,driver,time_slot):
        '''
        传入排班的时间段
        '''
        fr_radio = driver.find_element(By.CSS_SELECTOR, value='.fr-radio')
        fr_spans = fr_radio.find_elements(By.TAG_NAME, value='span')
        for fr_span in fr_spans:
            # print(fr_span.get_attribute('textContent').strip())
            if fr_span.get_attribute('textContent').strip() == time_slot:
                fr_span.click()
                time.sleep(1)
                break