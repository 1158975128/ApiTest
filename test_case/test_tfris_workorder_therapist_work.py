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
    # yield
    # logout = Logout(driver)
    # logout.logout()

class Test_Work_Order():
    # 治疗师登录完成全部工单--》11
    @pytest.mark.parametrize('status', [('开始结束')])
    def test_button_status(self,driver,status):
        '''
        检查开始/结束按钮有没有隐藏
        '''
        work = My_Work(driver)
        result = work.button_status()
        if status == result:
            assert True
        else:
            assert False, '预期隐藏按钮为:%s ；页面隐藏按钮为:%s' % (status,result)


    @pytest.mark.parametrize('item,arrange', [('艾条灸','未排班')])
    def test_inner_start_end(self,driver,item,arrange):
        '''
        在工单详情页开始和结束工单
        :param item: 项目名
        :param arrange:排班状态（只选择'未排班'）
        '''
        work = My_Work(driver)
        work.inner_start_end(item,arrange)

    @pytest.mark.parametrize('operate,exp_msg', [('开始','开始工单成功'),
                                                 ('结束','结束工单成功')])
    def test_start_allworkorder(self,driver,operate,exp_msg):
        '''
        对授权的工单进行排班
        :param item: 项目名称
        :param operate: 排班
        :param time_slot: 排班时间段
        :param device: 治疗设备
        '''
        work = My_Work(driver)
        work.operate_allwork(operate,exp_msg)
