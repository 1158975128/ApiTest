import time
import pytest
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from config.account_info import masterEmail, masterPwd
from common.logger import MyLogging
from page_objects.work_order.my_work import My_Work



log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    login = LoginPage(driver)
    login.login_fris(masterEmail, masterPwd)
    # yield
    # logout = Logout(driver)
    # logout.logout()



class TestWorkOrderTherapist():
    @pytest.mark.parametrize('item,arrange,time_slot,device,tips', [('博巴斯训练','未排班','08:00-08:30','徒手','患者当前时间段已排满'),
                                                               ('博巴斯训练','未排班','09:20-09:50','上肢康复治疗仪','患者当前时间段已排满,设备当前时间段已排满')])
    def test_therapist(self,driver,item,arrange,time_slot,device,tips):
        '''
        状态为’未排班‘的工单排班冲突提示
        :param item: 治疗项目
        :param arrange: 排班状态（未排班）
        :param time_slot: 排班时间段
        :param device: 治疗设备
        :param tips: 冲突提示语，多条是用逗号隔开
        '''
        work = My_Work(driver)
        work.arrange_tips(item,arrange,time_slot,device,tips)
        time.sleep(1)

    @pytest.mark.parametrize('item,arrange,time_slot,device', [('博巴斯训练','未排班','10:00-10:30','上肢康复治疗仪')])
    def test_scheduling(self,driver,item,arrange,time_slot,device):
        '''
        点击未排班的按钮对工单进行排班
        :param item: 治疗项目
        :param arrange: 排班状态（未排班）
        :param time_slot: 排班时间段
        :param device: 治疗设备
        '''
        work = My_Work(driver)
        work.arrange_work(item,arrange,time_slot,device)
        time.sleep(1)

    @pytest.mark.parametrize('name,arrange,therapist,operate', [('博巴斯训练','未排班','OT治疗师2','授权')])
    def test_grant_workorder(self,driver,name,arrange,therapist,operate):
        '''
        对未排班的工单进行授权
        :param name: 治疗项目
        :param arrange: 排班状态（未排班）
        :param therapist: 被授权的治疗师名字
        :param operate: 选择对应的操作（授权）
        '''
        work = My_Work(driver)
        work.grant(name,arrange,therapist,operate)


