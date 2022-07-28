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

    @pytest.mark.parametrize('item,arrange_time', [('手功能训练李世杰徒手OT治疗师2','08:00-08:30'),
                                                   ('艾条灸李世杰冲击波治疗仪OT治疗师2(实习生小李)','08:40-09:10'),
                                                   ('手功能训练李世杰上肢康复治疗仪OT治疗师2','09:20-09:50'),
                                                   ('艾条灸李世杰上肢康复治疗仪OT治疗师2(实习生小李)','09:20-09:50')])
    def test_verify_arrange(self,driver,item,arrange_time):
        '''
        排班查询--》治疗师排班查询当前治疗师的工单排班情况
        :param item: 项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
        :param arrange_time: 排班时间段
        '''
        work = My_Work(driver)
        work.verify_arrang(item,arrange_time)
        # time.sleep(3)


    # 新建一个py文件，放置以下内容
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