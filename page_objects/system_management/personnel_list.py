import os
import time
import random
from selenium.webdriver.common.by import By
from utils.verify_code_tool import get_verify
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.common.exceptions import NoSuchElementException
from utils.close_tips_tool import right_corner_cancel
from utils.close_tips_tool import close_login_tips

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
personnel_map = map_path + "/system_management/personnel_list.xml"


def select_droplist(driver,name):
    droplist_ul = driver.find_element(By.CSS_SELECTOR, value='[aria-hidden="false"] ul')
    droplist_lis = droplist_ul.find_elements(By.TAG_NAME, value='li')
    for droplist_li in droplist_lis:
        # print(droplist_li.get_attribute('textContent').strip())
        if droplist_li.get_attribute('textContent').strip() == name:
            droplist_li.click()
            time.sleep(1)
            break

class PersonnelList():
    def __init__(self, driver):
        self.driver = driver
        self.personnel = ObjectMap(personnel_map)
        self.personnel_page = NavigateBar(self.driver)

    def add_new_personnel(self,email,email_pwd,email_type,name,sex,phone,pwd,con_pwd,office,depar,title,job,job_type,role):
        self.personnel_page.go_to_system_maintain()
        time.sleep(1)
        new_additional = self.personnel.getLocator(self.driver, "New_Additional")
        new_additional.click()
        time.sleep(1)
        # 填写手机或邮箱
        p_email = self.personnel.getLocator(self.driver, "Email")
        p_email.send_keys(email)
        # 点击确定
        confirm = self.personnel.getLocator(self.driver, "Confirm")
        confirm.click()
        time.sleep(1)
        try:
            self.personnel.getLocator(self.driver, 'AccountTitle')
        except NoSuchElementException:
            change = self.personnel.getLocator(self.driver, "Change")
            change.click()
            assert False, "没有进入新增账号页面！"
        else:
            # 姓名
            user_name = self.personnel.getLocator(self.driver, "Name")
            user_name.send_keys(name)
            # 性别
            if sex == '男':
                male = self.personnel.getLocator(self.driver, "Male")
                male.click()
            elif sex == '女':
                female = self.personnel.getLocator(self.driver, "Female")
                female.click()
            # 出生日期
            brithday = self.personnel.getLocator(self.driver, "Brithday")
            brithday.click()
            time.sleep(1)
            select = self.personnel.getLocator(self.driver, "SelectBrithday")
            select.click()
            # 填写手机号
            user_phone = self.personnel.getLocator(self.driver, "Phone")
            user_phone.send_keys(phone)
            # 点击获取验证码
            sendverfy = self.personnel.getLocator(self.driver, "SendVerfy")
            sendverfy.click()
            # 填写密码
            user_pwd = self.personnel.getLocator(self.driver, "Pwd")
            user_pwd.send_keys(pwd)
            # 填写确认密码
            user_con_pwd = self.personnel.getLocator(self.driver, "ConPwd")
            user_con_pwd.send_keys(con_pwd)
            # 选择科室
            user_office = self.personnel.getLocator(self.driver, "Office")
            self.driver.execute_script("arguments[0].click();", user_office)
            # user_office.click()
            self.driver.implicitly_wait(100)
            select_droplist(self.driver,office)
            # 选择部门
            user_depar = self.personnel.getLocator(self.driver, "Department")
            self.driver.execute_script("arguments[0].click();", user_depar)
            # user_depar.click()
            self.driver.implicitly_wait(100)
            select_droplist(self.driver,depar)
            # 选择职称
            user_title = self.personnel.getLocator(self.driver, "Title")
            self.driver.execute_script("arguments[0].click();", user_title)
            # user_title.click()
            self.driver.implicitly_wait(100)
            select_droplist(self.driver,title)
            time.sleep(1)
            # 选择岗位
            user_job = self.personnel.getLocator(self.driver, "Job")
            self.driver.execute_script("arguments[0].click();", user_job)
            self.driver.implicitly_wait(100)
            select_droplist(self.driver,job)
            # 选择岗位小类
            job_div = self.driver.find_element(By.CSS_SELECTOR, value='.el-col-24:nth-child(6) .el-checkbox-group')
            job_labels = job_div.find_elements(By.TAG_NAME, value='label')
            for job_label in job_labels:
                if job_label.get_attribute('textContent').strip() in job_type:
                    print(job_label.get_attribute('textContent').strip(),'----->',job_type)
                    job_label.find_element(By.CSS_SELECTOR, value='.el-checkbox__input').click()
            # 选择角色
            role_div = self.driver.find_element(By.CSS_SELECTOR, value='.is-required [aria-label="checkbox-group"]')
            role_labels = role_div.find_elements(By.TAG_NAME, value='label')
            for role_label in role_labels:
                if role_label.get_attribute('textContent').strip() in role:
                    print(role_label.get_attribute('textContent').strip(),'----->',role)
                    role_label.find_element(By.CSS_SELECTOR, value='.el-checkbox__input').click()
            time.sleep(2)
            # 填写验证码
            verfy = self.personnel.getLocator(self.driver, "Verfy")
            verfy.send_keys(get_verify(email.split('@')[0],email_pwd))
            time.sleep(1)
            # 点击确定
            ensure = self.personnel.getLocator(self.driver, "Ensure")
            ensure.click()
            time.sleep(1)
            try:
                tips = self.personnel.getLocator(self.driver, 'Tips').get_attribute('textContent')
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

    def find_personnel(self,name):
        """
        查找用户是否在列表中
        :param name: 用户名
        :return: True/Flase
        """
        self.personnel_page.go_to_system_maintain()
        personnel_names = self.driver.find_elements(By.CSS_SELECTOR,value='.is-scrolling-none tr td:nth-child(2) div')
        next_page = self.personnel.getLocator(self.driver, "Next_Page")
        name_list = []
        flag = True
        status = 0
        while flag:
            if len(personnel_names) == 10 and next_page.is_displayed():
                for personnel_name in personnel_names:
                    name_list.append(personnel_name.text.strip())
                if name in name_list:
                    flag = False
                    return True
                else:
                    next_page.click()
            else:
                for personnel_name in personnel_names:
                    name_list.append(personnel_name.get_attribute('textContent').strip())
                for personnel_name in name_list:
                    if personnel_name == name:
                        status = 1
                if status == 1:
                    return True
                else:
                    print('没有匹配的' + name)
                    return False
                flag = False
                print(flag,'*******')
