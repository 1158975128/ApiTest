import time
import pytest
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
from config.account_info import therapistOTEmail, therapistOTPwd
from common.logger import MyLogging
from page_objects.work_order.my_work import My_Work

log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    login = LoginPage(driver)
    login.login_fris(therapistOTEmail, therapistOTPwd)
    yield
    logout = Logout(driver)
    logout.logout()

class Test_Work_Order():
    # 治疗师完成治疗师长的授权工单操作--》7
    @pytest.mark.parametrize('name', [('博巴斯训练')])
    def test_arrange_button(self,driver,name):
        '''
        检查授权工单排版列是否为空
        '''
        work = My_Work(driver)
        result = work.arrange_button(name)
        if len(result) == 0:
            assert True
        else:
            assert False,'“排班”列不为空，显示内容:%s'%result

    @pytest.mark.parametrize('item,time_slot,device,tips,operate', [('博巴斯训练','09:20-09:50','上肢康复治疗仪','患者当前时间段已排满,设备当前时间段已排满,治疗师当前时间段已排满','排班')])
    def test_scheduling_conflict(self,driver,item,time_slot,device,tips,operate):
        '''
        授权工单排班冲突提示
        :param item: 治疗项目
        :param time_slot: 排班时间段
        :param device: 治疗设备
        :param tips: 提示语
        :param operate: 操作（排班）
        '''
        work = My_Work(driver)
        work.grant_arrange_tips(item,time_slot,device,tips,operate)
        time.sleep(1)

    @pytest.mark.parametrize('item,operate,time_slot,device', [('博巴斯训练','排班','10:40-11:10','上肢康复治疗仪')])
    def test_scheduling(self,driver,item,operate,time_slot,device):
        '''
        对授权的工单进行排班
        :param item: 项目名称
        :param operate: 排班
        :param time_slot: 排班时间段
        :param device: 治疗设备
        '''
        work = My_Work(driver)
        work.gran_arrange_work(item,operate,time_slot,device)
        time.sleep(1)

