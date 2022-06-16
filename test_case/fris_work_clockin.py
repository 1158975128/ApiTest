import time
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.arrange_order.work_clockin import Work_Clock_In


log = MyLogging(__name__).logger


class Test_Work_Clock_In():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('therapist1')

    def teardown(self):
        self.Work_Clock = Work_Clock_In(self.driver)
        self.driver.quit()

    data = [("OT")]
    @pytest.mark.parametrize('work_name', data)
    def test_01_check_work_order_page(self,work_name):
        self.clock_in = Work_Clock_In(self.driver)
        self.clock_in.start_work(work_name)




