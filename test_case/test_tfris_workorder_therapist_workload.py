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
    # yield
    # logout = Logout(driver)
    # logout.logout()



class TestWorkOrderTherapist():
    # 治疗师长统计分析--》12
    @pytest.mark.parametrize('data', [('OT治疗师2OT40.00查看明细','OT治疗师2PT50.00查看明细','OT治疗师2传统180.00查看明细','刘玉栋-治疗师长PT50.00查看明细','合计-320.00-')])
    def test_workload_count(self, driver,data):
        '''
        治疗师工作量统计
        '''
        workload_page = NavigateBar(driver)
        workload_page.go_to_therapist_workload()
        time.sleep(2)
        result = statictic_analysis(driver)
        for i in range(len(result)):
            if result[i] == data[i]:
                assert True
            else:
                assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])

    @pytest.mark.parametrize('tharepist_workload,job_type,name', [('张三艾条灸治疗60.00传统实习生小李','传统','OT治疗师2')])
    def test_workload_detailed(self,driver,tharepist_workload,job_type,name):
        '''
        治疗师工作量明细
        '''
        workload = WorkLoad(driver)
        now_time = time.strftime("%Y-%m-%d")
        tharepist_workload = tharepist_workload+now_time+now_time
        results = workload.see_work_detailed(tharepist_workload,job_type,name)
        for result in results:
            if tharepist_workload == result:
                assert True
            else:
                assert False, '页面没有匹配的治疗师工作量,%s' % tharepist_workload

    @pytest.mark.parametrize('data', [('OT治疗师2OT40.00', 'OT治疗师2PT50.00', 'OT治疗师2传统180.00', '刘玉栋-治疗师长PT50.00', '合计-320.00')])
    def test_office_workload(self, driver,data):
        '''
        科室工作量统计
        '''
        workload_page = NavigateBar(driver)
        workload_page.go_to_office_workload()
        time.sleep(3)
        result = statictic_analysis(driver)
        # print(result)
        for i in range(len(result)):
            if result[i] == data[i]:
                assert True
            else:
                assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])

    @pytest.mark.parametrize('data', [('冲击波治疗仪160.00查看明细','其他(未选设备)160.00查看明细','上肢康复治疗仪4180.00查看明细','徒手120.00查看明细','合计7320.00-')])
    def test_device_use_statistics(self, driver,data):
        '''
        设备使用统计
        '''
        workload_page = NavigateBar(driver)
        workload_page.go_to_device_use_statistics()
        time.sleep(3)
        result = statictic_analysis(driver)
        # print(result)
        for i in range(len(result)):
            if result[i] == data[i]:
                assert True
            else:
                assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])

    @pytest.mark.parametrize('data', [('艾条灸治疗180.00查看明细', '博巴斯训练治疗100.00查看明细', '手功能训练治疗40.00查看明细', '合计-320.00-')])
    def test_item_statistics(self, driver,data):
        '''
        项目统计
        '''
        workload_page = NavigateBar(driver)
        workload_page.go_to_item_statistics()
        time.sleep(2)
        result = statictic_analysis(driver)
        # print(result)
        for i in range(len(result)):
            if result[i] == data[i]:
                assert True
            else:
                assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])
