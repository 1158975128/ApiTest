import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.patient.patient_info_modify import Patient_Info_Modify
from page_objects.patient.patient_list import Patient_List
import pytest_check as check


log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin')

class Test_Patient_Modify():

    # 需求号S#1696
    data = [("新增患者", "门诊", "全部患者")]
    @pytest.mark.parametrize('new_patient,origin_droplist,patient_droplist', data)
    def test_01_check_patient_page(self,new_patient,origin_droplist,patient_droplist,driver):
        '''
        检查患者管理页面是否正确
        :param new_patient: 新增患者按钮
        :param origin_droplist: 默认患者来源为门诊
        :param patient_droplist:默认为全部患者
        :return:
        '''
        self.patient = Patient_Info_Modify(driver)
        page_list = self.patient.check_patient_page()
        print(page_list)
        check.equal(new_patient,page_list[0],"判断新增患者按钮")
        check.equal(origin_droplist,page_list[1],"判断默认患者来源下拉框为门诊")
        check.equal(patient_droplist,page_list[2],"判断默认患者下拉框为全部患者")
        log.info("TestCase:验证检查患者管理页面<br/>")

    # data = [("测试住院患者","关节炎","关")]
    # @pytest.mark.parametrize('patient,disease,diagnosis',data)
    def test_02_add_patient(self):
        '''
        测试新增住院患者
        :param patient: 患者姓名
        :param disease: 疾病类型
        :param diagnosis: 功能诊断
        '''
        # now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        # patient = patient + now_time
        self.patient_info = Patient_Info_Modify(self.driver)
        self.patient = Patient_List(self.driver)
        # self.patient.add_new_patient(patient,disease,diagnosis)
        log.info("新增患者成功")
        time.sleep(1)
        self.patient.find_patient('测试住院患者2022_06_13_23_25_58')
        time.sleep(3)
        self.patient_info.modify_info('测试住院患者2022_06_13_23_25_58')
        self.patient.leave_hospital('测试住院患者2022_06_13_23_25_58')

