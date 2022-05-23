import time
import unittest
import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.department_preserve.position import Position
import pytest_check as check


log = MyLogging(__name__).logger


class TestPosition():

    def setup(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def teardown(self):
        self.Position = Position(self.driver)
        self.driver.quit()

    data = [('新增职务','职务名称','创建时间','修改时间','操作')]
    @pytest.mark.parametrize('new_additional,position,create_time,modification_time,operation',data)
    def test_01_positio_page(self,new_additional,position,create_time,modification_time,operation):
        '''
        验证职务页格式正确
        '''
        self.posion = Position(self.driver)
        page_list = self.posion.check_position_page()
        check.equal(new_additional,page_list[0],"检查新增职务按钮")
        check.equal(position,page_list[1],"检查职务名称")
        check.equal(create_time,page_list[2],"检查创建时间")
        check.equal(modification_time,page_list[3],"检查修改时间")
        check.equal(operation,page_list[4],"检查操作")
        log.info("TestCase:验证职务页格式正确<br/>")

    data = [("测试新增职务")]
    @pytest.mark.parametrize('position',data)
    def test_02_add_position(self,position):
        '''
        测试新增职务
        :param department: 新增职务名
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        position = position + now_time
        self.position = Position(self.driver)
        self.position.add_new_position(position)
        log.info("新增职务成功")
        time.sleep(1)
        check.is_true(self.position.find_position_name(position),"判断职务是否成功新增，返回true为成功")
        time.sleep(1)
        self.position.delete_position(position)
        time.sleep(1)

    data = [("测试修改新增职务","测试修改职务")]
    @pytest.mark.parametrize('position,modify_position',data)
    def test_03_modify_position(self,position,modify_position):
        '''
        测试修改职务
        :param position: 新增职务名称
        :param modify_position: 修改职务名称
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        position = position + now_time
        modify_position = modify_position + now_time
        self.position = Position(self.driver)
        self.position.add_new_position(position)
        log.info("新增职务成功")
        self.driver.implicitly_wait(10)
        self.position.modify_position(position,modify_position)
        check.is_true(self.position.find_position_name(modify_position),"判断职务是否成功修改，返回true为成功")
        time.sleep(1)
        self.position.delete_position(modify_position)
        time.sleep(1)

    data = [("测试删除新增职务")]
    @pytest.mark.parametrize('delete_position',data)
    def test_04_delete_position(self,delete_position):
        '''
        测试删除职务
        :param delete_position: 删除职务的名称
        '''
        now_time = time.strftime("%Y_%m_%d_%H_%M_%S")
        delete_position = delete_position + now_time
        self.position = Position(self.driver)
        self.position.add_new_position(delete_position)
        log.info("新增职务成功")
        time.sleep(1)
        self.position.delete_position(delete_position)
        time.sleep(1)
        check.is_false(self.position.find_position_name(delete_position),"判断职务是否成功修改，返回false为成功")