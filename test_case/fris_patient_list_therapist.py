import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.patient.patient_list import Patient_List
import pytest_check as check


log = MyLogging(__name__).logger


class Test_Patient_List():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('doctor')

    def teardown(self):
        self.Patient = Patient_List(self.driver)
        self.driver.quit()
    # 需求号S#1687
    @pytest.mark.parametrize('data',[("测试其他女","关节炎","关","女","其他"),("测试住院男","关节炎","关","男","住院")])
    def test_01_check_patient_card(self, data):
        '''
        测试患者卡片显示是否正确
        :param data: 传入新增患者信息
        '''
        self.patient = Patient_List(self.driver)
        print(data)
        self.patient.add_new_patient(data[0],data[1],data[2],data[3],data[4])
        self.patient.find_patient(data[0],data[4])
        self.patient.check_patient_card()
        self.patient.leave_hospital(data[0])

    @pytest.mark.parametrize('data',[("测试治疗师","关节炎","关","女","其他","ui_test"),("测试治疗师","关节炎","关","男","住院","ui_test")])
    def test_02_appoint_therapeutist(self, data):
        '''
        测试指定治疗师
        :param data:传入新增患者信息
        '''
        self.patient = Patient_List(self.driver)
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = data[0] + now_time
        self.patient.add_new_patient(patient,data[1],data[2],data[3],data[4])
        self.patient.find_patient(patient)
        self.patient.add_treatment_item(patient,data[5])
        self.patient.find_patient(patient)
        self.patient.appoint_therapeutist()
        time.sleep(1)
        self.patient.leave_hospital(patient)

    def test_03_verify_paging(self):
        '''
        测试翻页功能
        '''
        self.patient = Patient_List(self.driver)
        self.patient.verify_paging()

    @pytest.mark.parametrize('data',[("门诊","我的患者","current"),("住院","我的患者","last"),("其他","我的患者","current"),("门诊","全部患者","last"),("住院","全部患者","last"),("其他","全部患者","current")])
    def test_04_verify_screen(self,data):
        '''
        测试登记时间、患者来源和患者类型筛选功能
        '''
        self.patient = Patient_List(self.driver)
        self.patient.find_patient(None,data[0],data[1])
        self.driver.implicitly_wait(100)
        self.patient.choose_date(data[2])
        self.driver.implicitly_wait(30)
        self.patient.verify_screen(data[0])
        self.driver.implicitly_wait(100)



    @pytest.mark.parametrize('data',[("其他女","关节炎","关","女","其他","我的患者"),("住院男","关节炎","关","男","住院","全部患者")])
    def test_04_verify_name_screen(self,data):
        '''
        测试患者姓名、患者来源和患者类型组合筛选功能
        '''
        self.patient = Patient_List(self.driver)
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = data[0] + now_time
        self.patient.add_new_patient(patient, data[1], data[2], data[3], data[4])
        self.patient.find_patient(patient, data[4], data[5])
        time.sleep(1)
        self.patient.choose_date()
        time.sleep(1)
        result = self.patient.check_patient_name(patient)
        self.driver.implicitly_wait(30)
        if result:
            self.driver.implicitly_wait(100)
            self.patient.leave_hospital(patient)
            self.driver.implicitly_wait(30)

    # @pytest.mark.parametrize('data',[("其他女","关节炎","关","女","其他","我的患者"),("住院男","关节炎","关","男","住院","全部患者")])
    def test_05_verify_date(self):
        '''
        测试患者姓名、患者来源和患者类型组合筛选功能
        '''
        self.patient = Patient_List(self.driver)
        self.patient.find_patient()
        time.sleep(1)
        self.patient.choose_date()