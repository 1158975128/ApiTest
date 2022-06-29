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
    mylogin.login_fris('doctor')

class Test_Patient_List():

    # 需求号S#1687
    @pytest.mark.parametrize('data',[('新增患者', '门诊', '全部患者', '姓名', '开始日期', '至', '结束日期')])
    def test_01_check_doctor_patient_page(self,data,driver):
        '''
        测试页面按钮名称和默认设置是否正确
        :param data: 按钮名称和默认设置
        '''
        self.patient = Patient_List(driver)
        self.patient.check_doctor_patient_page(data)


    @pytest.mark.parametrize('data',[("75ceshijj","关节炎","女","其他","关","2022年06月13日"),("yrv检查男住院1","关节炎","男","住院","关","2022年06月09日")])
    def test_02_check_patient_card(self, data,driver):
        '''
        测试患者卡片显示是否正确，传入的日期为当日
        :param data: 传入新增患者信息
        '''
        self.patient = Patient_List(driver)
        time.sleep(1)
        self.patient.add_new_patient(data[0],data[1],data[4],data[2],data[3])
        self.patient.find_patient(data[0])
        self.patient.check_patient_card(data)
        self.patient.leave_hospital(data[0])

    @pytest.mark.parametrize('data',[("测试治疗师","关节炎","关","女","其他","ui_test","st_test","周治疗师全"),("测试治疗师","关节炎","关","男","住院","ui_test","st_test","周治疗师全")])
    def test_03_appoint_therapeutist(self, data,driver):
        '''
        测试指定治疗师
        :param data:传入新增患者信息
        '''
        self.patient = Patient_List(driver)
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = data[0] + now_time
        self.patient.add_new_patient(patient,data[1],data[2],data[3],data[4])
        self.patient.find_patient(patient)
        self.patient.add_treatment_item(patient,data[5])
        self.patient.find_patient(patient)
        self.patient.appoint_therapeutist(data[7])
        time.sleep(1)
        self.patient.add_treatment_item(patient,data[6])
        self.patient.check_treatment_name(data[7])
        self.patient.find_patient(patient)
        self.patient.leave_hospital(patient)

    def test_04_verify_paging(self,driver):
        '''
        测试翻页功能
        '''
        self.patient = Patient_List(driver)
        self.patient.verify_paging()

    # @pytest.mark.parametrize('data',[("其他","全部患者","current"),("其他","我的患者","current"),("门诊","我的患者","current"),("门诊","全部患者","current"),("住院","我的患者","current"),("住院","全部患者","current")])
    @pytest.mark.parametrize('data',[("其他","我的患者","current")])
    def test_05_verify_screen(self,data,driver):
        '''
        测试登记时间、患者来源和患者类型筛选功能
        '''
        self.patient = Patient_List(driver)
        self.patient.find_patient(None,data[0],data[1])
        driver.implicitly_wait(100)
        self.patient.choose_date(data[2])
        driver.implicitly_wait(30)
        self.patient.verify_screen(data[0])


    @pytest.mark.parametrize('data',[("其他女","关节炎","关","女","其他","我的患者"),("住院男","关节炎","关","男","住院","全部患者")])
    def test_06_verify_name_screen(self,data,driver):
        '''
        测试患者姓名、患者来源和患者类型组合筛选功能
        '''
        self.patient = Patient_List(driver)
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = data[0] + now_time
        self.patient.add_new_patient(patient, data[1], data[2], data[3], data[4])
        self.patient.find_patient(patient, data[4], data[5])
        time.sleep(1)
        self.patient.choose_date()
        time.sleep(1)
        result = self.patient.check_patient_name(patient)
        driver.implicitly_wait(1000)
        if result:
            driver.implicitly_wait(1000)
            self.patient.leave_hospital(patient)
            driver.implicitly_wait(1000)


