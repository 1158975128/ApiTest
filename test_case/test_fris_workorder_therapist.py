import time
import pytest
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
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
    # 治疗项目指定治疗师
    @pytest.mark.parametrize('item,arrange,time_slot,device,tips', [('博巴斯训练','未排班','08:00-08:30','徒手','患者当前时间段已排满'),
                                                               ('博巴斯训练','未排班','09:20-09:50','上肢康复治疗仪','患者当前时间段已排满,设备当前时间段已排满')])
    def test_therapist(self,driver,item,arrange,time_slot,device,tips):
        work = My_Work(driver)
        work.arrange_tips(item,arrange,time_slot,device,tips)
        time.sleep(1)

    @pytest.mark.parametrize('item,arrange_time', [('手功能训练李世杰徒手OT治疗师2','08:00-08:30'),
                                                   ('艾条灸李世杰冲击波治疗仪OT治疗师2(实习生小李)','08:40-09:10'),
                                                   ('手功能训练李世杰上肢康复治疗仪OT治疗师2','09:20-09:50'),
                                                   ('艾条灸李世杰上肢康复治疗仪OT治疗师2(实习生小李)','09:20-09:50')])
    def test_verify_arrange(self,driver,item,arrange_time):
        work = My_Work(driver)
        work.verify_arrang(item,arrange_time)
        # time.sleep(3)
