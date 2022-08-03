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


    # # 新建一个py文件，放置以下内容-->排班查询、大屏排班--》8-10
    # @pytest.mark.parametrize('item,arrange_time', [('手功能训练张三徒手OT治疗师2','08:00-08:30'),
    #                                                ('艾条灸张三冲击波治疗仪OT治疗师2(实习生小李)','08:40-09:10'),
    #                                                ('手功能训练张三上肢康复治疗仪OT治疗师2','09:20-09:50'),
    #                                                ('艾条灸张三上肢康复治疗仪OT治疗师2(实习生小李)','09:20-09:50'),
    #                                                ('博巴斯训练张三上肢康复治疗仪刘玉栋-治疗师长','10:00-10:30'),
    #                                                ('博巴斯训练张三上肢康复治疗仪OT治疗师2','10:40-11:10')])
    # def test_verify_arrange(self,driver,item,arrange_time):
    #     '''
    #     排班查询--》'全部排班'查询所有治疗师的工单排班情况
    #     :param item: 项目名患者名治疗设备治疗师名称（如：'手功能训练李世杰徒手OT治疗师2'）
    #     :param arrange_time: 排班时间段
    #     '''
    #     work = My_Work(driver)
    #     work.verify_allarrang(item,arrange_time)
    #     # time.sleep(3)
    #
    # @pytest.mark.parametrize('display_mode, name', [('全天','全天排班')])
    # def test_screen_arrange(self,driver,display_mode, name):
    #     '''
    #     排班可视化--》'新增排班大屏'
    #     :param display_mode: 显示模式
    #     :param name: 大屏名称
    #     '''
    #     display = Arrange_Display(driver)
    #     display.screen_arrange(display_mode, name)
    #     # display.view_screen()
    #
    # @pytest.mark.parametrize('patient_name,item,arrange_time', [('*三','OT治疗师2徒手','08:00-08:30'),
    #                                                             ('*三','OT治疗师2冲击波','08:40-09:10'),
    #                                                             ('*三','OT治疗师2上肢','09:20-09:50'),
    #                                                             ('*三','OT治疗师2上肢','09:20-09:50'),
    #                                                             ('*三','刘玉栋-治疗师长上肢','10:00-10:30'),
    #                                                             ('*三','OT治疗师2上肢','10:40-11:10')])
    #
    # def test_arrange_display(self,driver,patient_name,item,arrange_time):
    #     '''
    #     排班可视化--》查询所有治疗师的工单排班情况
    #     :param patient_name: 患者名称
    #     :param item: 排班信息（如：'OT治疗师2冲击波'）
    #     :param arrange_time: 排班时间段
    #     '''
    #     display = Arrange_Display(driver)
    #     display.view_screen()
    #     result = display.arrange_display(patient_name,item,arrange_time)
    #     display.close_screen()
    #     if '匹配成功' in result:
    #         assert True
    #     else:
    #         assert False, '页面没有匹配的排班数据,%s-->%s' % (arrange_time, item)
    #     time.sleep(1)
    #
    # @pytest.mark.parametrize('item,start,end', [('博巴斯训练','开始','结束')])
    # def test_start_work_order(self,driver,item,start,end):
    #     '''
    #     对授权的工单进行排班
    #     :param item: 项目名称
    #     :param operate: 排班
    #     :param time_slot: 排班时间段
    #     :param device: 治疗设备
    #     '''
    #     work = My_Work(driver)
    #     work.start_work(item,start)
    #     time.sleep(1)
    #     work.end_work(item,end)
    #
    # @pytest.mark.parametrize('patient_name,item,arrange_time', [('*三','刘玉栋-治疗师长上肢','10:00-10:30')])
    #
    # def test_arrange_notdisplay(self,driver,patient_name,item,arrange_time):
    #     '''
    #     排班可视化--》查询当前治疗师不在排班中
    #     :param patient_name: 患者名称
    #     :param item: 排班信息（如：'OT治疗师2冲击波'）
    #     :param arrange_time: 排班时间段
    #     '''
    #     display = Arrange_Display(driver)
    #     display.view_screen()
    #     result = display.arrange_display(patient_name,item,arrange_time)
    #     display.close_screen()
    #     if '匹配成功' not in result:
    #         assert True
    #     else:
    #         assert False, '页面显示当前排班数据：排班数据,%s-->%s' % (arrange_time, item)

    #
    #
    # # 新建一个py文件，放置以下内容-->统计分析--》12
    # @pytest.mark.parametrize('data', [('OT治疗师2OT40.00查看明细','OT治疗师2PT50.00查看明细','OT治疗师2传统180.00查看明细','刘玉栋-治疗师长PT100.00查看明细','合计-370.00-')])
    # def test_workload_count(self, driver,data):
    #     '''
    #     治疗师工作量统计
    #     '''
    #     workload_page = NavigateBar(driver)
    #     workload_page.go_to_therapist_workload()
    #     time.sleep(2)
    #     result = statictic_analysis(driver)
    #     for i in range(len(result)):
    #         if result[i] == data[i]:
    #             assert True
    #         else:
    #             assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])
    #
    # @pytest.mark.parametrize('tharepist_workload,job_type,name', [('张三艾条灸治疗60.00传统实习生小李','传统','OT治疗师2')])
    # def test_workload_detailed(self,driver,tharepist_workload,job_type,name):
    #     '''
    #     治疗师工作量明细
    #     '''
    #     workload = WorkLoad(driver)
    #     now_time = time.strftime("%Y-%m-%d")
    #     tharepist_workload = tharepist_workload+now_time+now_time
    #     results = workload.see_work_detailed(tharepist_workload,job_type,name)
    #     for result in results:
    #         if tharepist_workload == result:
    #             assert True
    #         else:
    #             assert False, '页面没有匹配的治疗师工作量,%s' % tharepist_workload
    #
    # @pytest.mark.parametrize('data', [('OT治疗师2OT40.00', 'OT治疗师2PT50.00', 'OT治疗师2传统180.00', '刘玉栋-治疗师长PT100.00', '合计-370.00')])
    # def test_office_workload(self, driver,data):
    #     '''
    #     科室工作量统计
    #     '''
    #     workload_page = NavigateBar(driver)
    #     workload_page.go_to_office_workload()
    #     time.sleep(3)
    #     result = statictic_analysis(driver)
    #     # print(result)
    #     for i in range(len(result)):
    #         if result[i] == data[i]:
    #             assert True
    #         else:
    #             assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])
    #
    # @pytest.mark.parametrize('data', [('冲击波治疗仪160.00查看明细','其他(未选设备)160.00查看明细','上肢康复治疗仪5230.00查看明细','徒手120.00查看明细','合计8370.00-')])
    # def test_device_use_statistics(self, driver,data):
    #     '''
    #     设备使用统计
    #     '''
    #     workload_page = NavigateBar(driver)
    #     workload_page.go_to_device_use_statistics()
    #     time.sleep(3)
    #     result = statictic_analysis(driver)
    #     # print(result)
    #     for i in range(len(result)):
    #         if result[i] == data[i]:
    #             assert True
    #         else:
    #             assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])
    #
    # @pytest.mark.parametrize('data', [('李世杰男住院医生2%50.00查看明细','张三女住院医生2%320.00查看明细','合计-----370.00-','张三')])
    # def test_cost_statistics(self, driver,data):
    #     '''
    #     费用统计
    #     '''
    #     workload_page = NavigateBar(driver)
    #     patient = Patient(driver)
    #     # 获取患者的住院号
    #     patient.find_patient(data[3])
    #     patient.click_patient_card(data[3])
    #     time.sleep(1)
    #     hospital_id = patient.find_hospital_id()
    #     # 将获取到的住院号替换传入参数中的%,放入一个新列表
    #     new_data = []
    #     for i in range(len(data)-1):
    #         new_data.append(data[i].replace('%', hospital_id))
    #     # 获取费用统计页面数据，存放进workload_result列表中
    #     workload_page.go_to_cost_statistics()
    #     time.sleep(2)
    #     workload_result = statictic_analysis(driver)
    #     # new_data和workload_result两个列表遍历对比
    #     for i in range(len(workload_result)):
    #         if workload_result[i] == new_data[i]:
    #             assert True
    #         else:
    #             assert False,'传入数据:%s--》显示数据:%s'%(new_data[i],workload_result[i])
    #
    # @pytest.mark.parametrize('data', [('艾条灸治疗180.00查看明细', '博巴斯训练治疗150.00查看明细', '手功能训练治疗40.00查看明细', '合计-370.00-')])
    # def test_item_statistics(self, driver,data):
    #     '''
    #     项目统计
    #     '''
    #     workload_page = NavigateBar(driver)
    #     workload_page.go_to_item_statistics()
    #     time.sleep(2)
    #     result = statictic_analysis(driver)
    #     # print(result)
    #     for i in range(len(result)):
    #         if result[i] == data[i]:
    #             assert True
    #         else:
    #             assert False,'传入数据:%s--》显示数据:%s'%(data[i],result[i])
    #
