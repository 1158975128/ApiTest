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
    # 治疗师长进行排班查询、大屏排班--》8-10
    @pytest.mark.parametrize('item,arrange_time', [('手功能训练张三徒手OT治疗师2','08:00-08:30'),
                                                   ('艾条灸张三冲击波治疗仪OT治疗师2(实习生小李)','08:40-09:10'),
                                                   ('手功能训练张三上肢康复治疗仪OT治疗师2','09:20-09:50'),
                                                   ('艾条灸张三上肢康复治疗仪OT治疗师2(实习生小李)','09:20-09:50'),
                                                   ('博巴斯训练张三上肢康复治疗仪刘玉栋-治疗师长','10:00-10:30'),
                                                   ('博巴斯训练张三上肢康复治疗仪OT治疗师2','10:40-11:10')])
    def test_verify_arrange(self,driver,item,arrange_time):
        '''
        排班查询--》'全部排班'查询所有治疗师的工单排班情况
        :param item: 项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
        :param arrange_time: 排班时间段
        '''
        work = My_Work(driver)
        work.verify_allarrang(item,arrange_time)
        # time.sleep(3)

    @pytest.mark.parametrize('display_mode,name', [('全天','全天排班')])
    def test_screen_arrange(self,driver,display_mode,name):
        '''
        排班可视化--》'新增排班大屏'
        :param display_mode: 显示模式
        :param name: 大屏名称
        '''
        display = Arrange_Display(driver)
        display.screen_arrange(display_mode,name)
        # display.view_screen()

    @pytest.mark.parametrize('patient_name,item,arrange_time', [('*三','OT治疗师2徒手','08:00-08:30'),
                                                                ('*三','OT治疗师2冲击波','08:40-09:10'),
                                                                ('*三','OT治疗师2上肢','09:20-09:50'),
                                                                ('*三','OT治疗师2上肢','09:20-09:50'),
                                                                ('*三','刘玉栋-治疗师长上肢','10:00-10:30'),
                                                                ('*三','OT治疗师2上肢','10:40-11:10')])

    def test_arrange_display(self,driver,patient_name,item,arrange_time):
        '''
        排班可视化--》查询所有治疗师的工单排班情况
        :param patient_name: 患者名称
        :param item: 排班信息（如：'OT治疗师2冲击波'）
        :param arrange_time: 排班时间段
        '''
        display = Arrange_Display(driver)
        display.view_screen()
        result = display.arrange_display(patient_name,item,arrange_time)
        display.close_screen()
        if '匹配成功' in result:
            assert True
        else:
            assert False, '页面没有匹配的排班数据,%s-->%s' % (arrange_time, item)
        time.sleep(1)

    @pytest.mark.parametrize('item,start,end', [('博巴斯训练','开始','结束')])
    def test_start_work_order(self,driver,item,start,end):
        '''
        对授权的工单进行排班
        :param item: 项目名称
        :param operate: 排班
        :param time_slot: 排班时间段
        :param device: 治疗设备
        '''
        work = My_Work(driver)
        work.start_work(item,start)
        time.sleep(1)
        work.end_work(item,end)

    @pytest.mark.parametrize('patient_name,item,arrange_time', [('*三','刘玉栋-治疗师长上肢','10:00-10:30')])

    def test_arrange_notdisplay(self,driver,patient_name,item,arrange_time):
        '''
        排班可视化--》查询当前治疗师不在排班中
        :param patient_name: 患者名称
        :param item: 排班信息（如：'OT治疗师2冲击波'）
        :param arrange_time: 排班时间段
        '''
        display = Arrange_Display(driver)
        display.view_screen()
        result = display.arrange_display(patient_name,item,arrange_time)
        display.close_screen()
        if '匹配成功' not in result:
            assert True
        else:
            assert False, '页面显示当前排班数据：排班数据,%s-->%s' % (arrange_time, item)
