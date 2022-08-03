import time
import pytest
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
from config.account_info import masterEmail, masterPwd
from common.logger import MyLogging
from page_objects.work_order.my_work import My_Work
from page_objects.arrange_query.arrange_display import Arrange_Display
from page_objects.statistic_analysis.tharepist_workload import WorkLoad
from utils.statistic_analysis_tool import statictic_analysis
from page_objects.navigate_bar import NavigateBar
from page_objects.patient.patient import Patient
from utils.droplist_select_tool import Select
from utils.object_map import ObjectMap



log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    login = LoginPage(driver)
    login.login_fris(masterEmail, masterPwd)
    yield
    logout = Logout(driver)
    logout.logout()



class TestWorkOrderTherapist():
    @pytest.mark.parametrize('data', [('张三女住院医生2%320.00查看明细','合计-----320.00-','张三')])
    def test_cost_statistics(self, driver,data):
        '''
        费用统计
        '''
        workload_page = NavigateBar(driver)
        patient = Patient(driver)
        time.sleep(1)
        # 获取患者的住院号
        patient.find_patient(data[2])
        patient.click_patient_card(data[2])
        time.sleep(1)
        hospital_id = patient.find_hospital_id()
        # 将获取到的住院号替换传入参数中的%,放入一个新列表
        new_data = []
        for i in range(len(data)-1):
            new_data.append(data[i].replace('%', hospital_id))
        # 获取费用统计页面数据，存放进workload_result列表中
        workload_page.go_to_cost_statistics()
        time.sleep(2)
        workload_result = statictic_analysis(driver)
        # new_data和workload_result两个列表遍历对比
        for i in range(len(workload_result)):
            if workload_result[i] == new_data[i]:
                assert True
            else:
                assert False,'传入数据:%s--》显示数据:%s'%(new_data[i],workload_result[i])