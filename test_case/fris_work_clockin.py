import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.work_order.my_work import My_Work


log = MyLogging(__name__).logger


class Test_Work_Order():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('therapistall')

    def teardown(self):
        self.My_work = My_Work(self.driver)
        self.driver.quit()

    data = [("住院李四")]
    @pytest.mark.parametrize('patient_name', data)
    def test_01_check_work_order_page(self,patient_name):
        self.my_work = My_Work(self.driver)
        self.my_work.check_my_work_page()
        # result = self.my_work.check_my_work_page()
        # print(result)


    data = [("住院李四")]
    @pytest.mark.parametrize('patient_name', data)
    def test_02_start_work(self,patient_name):
        self.my_work = My_Work(self.driver)
        start_work = self.my_work.start_work(patient_name)
        assert start_work is True
        log.info("开始工单成功")

    data = [("住院李四")]
    @pytest.mark.parametrize('patient_name', data)
    def test_03_reroll_work(self,patient_name):
        self.my_work = My_Work(self.driver)
        self.my_work.start_work(patient_name)
        reroll_work = self.my_work.reroll_work(patient_name)
        log.info("开始工单成功")
        assert reroll_work is True

    data = [("住院李四")]
    @pytest.mark.parametrize('patient_name', data)
    def test_04_end_work(self,patient_name):
        self.my_work = My_Work(self.driver)
        self.my_work.start_work(patient_name)
        end_work = self.my_work.end_work(patient_name)
        log.info("结束工单成功")
        assert end_work is True

