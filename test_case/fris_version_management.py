import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from page_objects.system_management.version_management import Version_Manage
import pytest_check as check


log = MyLogging(__name__).logger

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin')

class TestDisease():

    # 需求号S#1992
    data = [('版本号 描述 操作')]
    @pytest.mark.parametrize('menu_bar',data)
    def test_01_version_page(self,menu_bar,driver):
        '''
        验证版本管理页格式正确
        '''
        self.version = Version_Manage(driver)
        result = self.version.check_version_page()
        check.equal(result,menu_bar,'判断菜单栏是否正确')


    data = [("V 1.2.345","测试修改系统版本号")]
    @pytest.mark.parametrize('version,desc',data)
    def test_02_modify_version_management(self,version,desc,driver):
        '''
        测试修改系统版本号
        :param version: 传入修改的版本号
        :param desc: 传入修改的描述
        '''
        self.version = Version_Manage(driver)
        result_list = self.version.modify_version(version,desc)
        check.not_equal(version,result_list[0],"检查版本号是否修改成功")
        check.not_equal(desc,result_list[1],"检查描述是否修改成功")
        log.info("修改成功")
        self.version.modify_version(result_list[0], result_list[1])



