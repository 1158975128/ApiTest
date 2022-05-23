import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.work_order.my_work import My_Work


log = MyLogging(__name__).logger


class Test_Work_Order():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('therapistall')

    def teardown(self):
        self.My_work = My_Work(self.driver)
        self.driver.quit()

    # data = [("新增患者", "门诊", "全部患者")]
    # @pytest.mark.parametrize('new_patient,origin_droplist,patient_droplist', data)
    def test_01_department_type_page(self):
        self.my_work = My_Work(self.driver)
        self.my_work.start_work()
        self.my_work.check_start_work()
        # page_list = self.patient.check_patient_page()
        # print(page_list)
        # assert new_patient == page_list[0] and origin_droplist == page_list[1] and patient_droplist == page_list[2]
        # log.info("TestCase:验证检查患者管理页面<br/>")

    # data = [("测试住院患者","关节炎","关")]
    # @pytest.mark.parametrize('patient,disease,diagnosis',data)
    # def test_02_add_department(self,patient,disease,diagnosis):
    #     '''
    #     测试新增住院患者
    #     :param patient: 患者姓名
    #     :param disease: 疾病类型
    #     :param diagnosis: 功能诊断
    #     '''
    #     now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
    #     patient = patient + now_time
    #     self.patient = Patient_Manage(self.driver)
    #     self.patient.add_new_patient(patient,disease,diagnosis)
    #     self.patient.find_patient(patient)
    #     check_name = self.patient.patient_name(patient)
    #     log.info("新增患者成功")
    #     time.sleep(1)
    #     assert check_name is True
    #
    # data = [("测试治疗项目","关节炎","关","ui_test")]
    # @pytest.mark.parametrize('patient,disease,diagnosis,item_name',data)
    # def test_03_add_treatment_item(self,patient,disease,diagnosis,item_name):
    #     '''
    #     测试新增住院患者
    #     :param patient: 患者姓名
    #     :param disease: 疾病类型
    #     :param diagnosis: 功能诊断
    #     '''
    #     now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
    #     patient = patient + now_time
    #     self.patient = Patient_Manage(self.driver)
    #     self.patient.add_new_patient(patient,disease,diagnosis)
    #     self.patient.find_patient(patient)
    #     self.patient.add_treatment_item(patient,item_name)
    #     print(patient)
    #
    # data = [("测试评定","关节炎","关","辩证")]
    # @pytest.mark.parametrize('patient,disease,diagnosis,judge_name',data)
    # def test_04_add_judge_item(self,patient,disease,diagnosis,judge_name):
    #     '''
    #     测试新增住院患者,给该患者新增评定项目
    #     :param patient: 患者姓名
    #     :param disease: 疾病类型
    #     :param diagnosis: 功能诊断
    #     :param judge_name: 评定项目
    #     '''
    #     now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
    #     patient = patient + now_time
    #     self.patient = Patient_Manage(self.driver)
    #     self.patient.add_new_patient(patient,disease,diagnosis)
    #     self.patient.find_patient(patient)
    #     self.patient.add_judge_item(patient,judge_name)
    #     print(patient)
    #
    # data = [("测试患者出院","关节炎","关")]
    # @pytest.mark.parametrize('patient,disease,diagnosis',data)
    # def test_05_leave_hospital(self,patient,disease,diagnosis):
    #
    #     now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
    #     patient = patient + now_time
    #     self.patient = Patient_Manage(self.driver)
    #     self.patient.add_new_patient(patient,disease,diagnosis)
    #     self.patient.find_patient(patient)
    #     self.patient.leave_hospital(patient)
    #     assert self.patient.check_treatment_stoped() is True

