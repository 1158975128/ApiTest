import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.patient.patient_manage import Patient_Manage
import pytest_check as check


log = MyLogging(__name__).logger


class Test_Patient():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('doctor')

    def teardown(self):
        self.Patient = Patient_Manage(self.driver)
        self.driver.quit()

    data = [("新增患者", "门诊", "全部患者")]
    @pytest.mark.parametrize('new_patient,origin_droplist,patient_droplist', data)
    def test_01_check_patient_page(self,new_patient,origin_droplist,patient_droplist):
        '''
        检查患者管理页面是否正确
        :param new_patient: 新增患者按钮
        :param origin_droplist: 默认患者来源为门诊
        :param patient_droplist:默认为全部患者
        :return:
        '''
        self.patient = Patient_Manage(self.driver)
        page_list = self.patient.check_patient_page()
        print(page_list)
        check.equal(new_patient,page_list[0],"判断新增患者按钮")
        check.equal(origin_droplist,page_list[1],"判断默认患者来源下拉框为门诊")
        check.equal(patient_droplist,page_list[2],"判断默认患者下拉框为全部患者")
        log.info("TestCase:验证检查患者管理页面<br/>")

    data = [("测试住院患者","关节炎","关")]
    @pytest.mark.parametrize('patient,disease,diagnosis',data)
    def test_02_add_patient(self,patient,disease,diagnosis):
        '''
        测试新增住院患者
        :param patient: 患者姓名
        :param disease: 疾病类型
        :param diagnosis: 功能诊断
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = patient + now_time
        self.patient = Patient_Manage(self.driver)
        new_patient = self.patient.add_new_patient(patient,disease,diagnosis)
        log.info("新增患者成功")
        time.sleep(1)
        check.is_true(new_patient,"检查新增患者，成功返回true")
        self.patient.leave_hospital(patient)

    data = [("测试治疗项目","关节炎","关","ui_test")]
    @pytest.mark.parametrize('patient,disease,diagnosis,item_name',data)
    def test_03_add_treatment_item(self,patient,disease,diagnosis,item_name):
        '''
        测试新增住院患者
        :param patient: 患者姓名
        :param disease: 疾病类型
        :param diagnosis: 功能诊断
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = patient + now_time
        self.patient = Patient_Manage(self.driver)
        self.patient.add_new_patient(patient,disease,diagnosis)
        self.patient.find_patient(patient)
        treatment_item = self.patient.add_treatment_item(patient,item_name)
        check.is_true(treatment_item,"判断新增治疗项目，成功返回true")
        self.patient.click_patientInfo()




    data = [("测试评定","关节炎","关","辩证")]
    @pytest.mark.parametrize('patient,disease,diagnosis,judge_name',data)
    def test_04_add_judge_item(self,patient,disease,diagnosis,judge_name):
        '''
        测试新增住院患者,给该患者新增评定项目
        :param patient: 患者姓名
        :param disease: 疾病类型
        :param diagnosis: 功能诊断
        :param judge_name: 评定项目
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = patient + now_time
        self.patient = Patient_Manage(self.driver)
        self.patient.add_new_patient(patient,disease,diagnosis)
        self.patient.find_patient(patient)
        judge_item = self.patient.add_judge_item(patient,judge_name)
        check.is_true(judge_item,"判断新增评定项目，成功返回true")
        self.patient.click_patientInfo()

    data = [("测试患者出院","关节炎","关")]
    @pytest.mark.parametrize('patient,disease,diagnosis',data)
    def test_05_leave_hospital(self,patient,disease,diagnosis):

        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        patient = patient + now_time
        self.patient = Patient_Manage(self.driver)
        self.patient.add_new_patient(patient,disease,diagnosis)
        self.patient.find_patient(patient)
        leave_hospital = self.patient.leave_hospital(patient)
        check.is_true(leave_hospital,"判断结束疗程，成功返回true")
