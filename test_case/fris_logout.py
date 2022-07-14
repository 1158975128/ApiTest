import time
import pytest
from page_objects.login.login_page import LoginPage
from common.logger import MyLogging
from page_objects.login.logout import Logout

log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin')


class TestDepartment():

    def test_01_logout(self,driver):
        self.logout = Logout(driver)
        time.sleep(2)
        result = self.logout.logout()
        assert result == '登录 FRIS！'