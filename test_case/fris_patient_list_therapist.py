import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.patient.patient_list import Patient_List
from page_objects.arrange_order.work_clockin import Work_Clock_In

import pytest_check as check


log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('therapistAll')

class Test_Patient_List():

    # 需求号S#1687
    @pytest.mark.parametrize('data',[('门诊', '我的患者', '姓名', '开始日期', '至', '结束日期')])
    def test_01_check_doctor_patient_page(self,data,driver):
        '''
        测试页面按钮名称和默认设置是否正确
        :param data: 按钮名称和默认设置
        '''
        self.patient = Patient_List(driver)
        self.patient.check_therapist_patient_page(data)


    def test_03_verify_paging(self,driver):
        '''
        测试翻页功能
        '''
        self.patient = Patient_List(driver)
        self.patient.verify_paging()

    @pytest.mark.parametrize('data',[("门诊","全部患者","last"),("门诊","我的患者","current"),("住院","我的患者","last"),("其他","我的患者","current"),("住院","全部患者","last"),("其他","全部患者","current")])
    def test_04_verify_screen(self,data,driver):
        '''
        测试登记时间、患者来源和患者类型筛选功能
        '''
        self.patient = Patient_List(driver)
        self.patient.find_patient(None,data[0],data[1])
        driver.implicitly_wait(100)
        self.patient.choose_date(data[2])
        driver.implicitly_wait(30)
        self.patient.verify_screen(data[0])
        driver.implicitly_wait(100)



    @pytest.mark.parametrize('data',[('测试治疗师2022_05_30_14_04_42',"其他","我的患者"),("检查住院男","住院","全部患者")])
    def test_04_verify_name_screen(self,data,driver):
        '''
        测试患者姓名、患者来源和患者类型组合筛选功能
        '''
        self.patient = Patient_List(driver)

        self.patient.find_patient(data[0], data[1], data[2])
        time.sleep(1)
        self.patient.choose_date()
        time.sleep(1)
        self.patient.check_patient_name(data[0])
