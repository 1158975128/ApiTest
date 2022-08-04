import time
import pytest
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
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
    @pytest.mark.parametrize('item,arrange,time_slot,device', [('手功能训练','未排班','08:00-08:30','徒手'),
                                                               ('艾条灸','未排班','08:40-09:10','冲击波治疗仪'),
                                                               ('手功能训练','未排班','09:20-09:50','上肢康复治疗仪'),
                                                               ('艾条灸','未排班','09:20-09:50','上肢康复治疗仪')])
    def test_scheduling(self,driver,item,arrange,time_slot,device):
        '''
        对‘未排班’的工单进行排班
        :param item: 项目名称
        :param arrange: 排班状态（未排班）
        :param time_slot: 排班时间段
        :param device: 治疗设备
        '''
        work = My_Work(driver)
        work.arrange_work(item,arrange,time_slot,device)
        time.sleep(1)

    @pytest.mark.parametrize('item,arrange_time', [('手功能训练张三徒手OT治疗师2','08:00-08:30'),
                                                   ('艾条灸张三冲击波治疗仪OT治疗师2(实习生小李)','08:40-09:10'),
                                                   ('手功能训练张三上肢康复治疗仪OT治疗师2','09:20-09:50'),
                                                   ('艾条灸张三上肢康复治疗仪OT治疗师2(实习生小李)','09:20-09:50')])
    def test_verify_arrange(self,driver,item,arrange_time):
        '''
        排班查询--》治疗师排班查询当前治疗师的工单排班情况
        :param item: 项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
        :param arrange_time: 排班时间段
        '''
        work = My_Work(driver)
        work.verify_arrang(item,arrange_time)
        time.sleep(1)
