import time
import unittest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from parameterized import parameterized
from page_objects.department_preserve.position import Position


log = MyLogging(__name__).logger


class TestPosition(unittest.TestCase):

    def setUp(self):
        self.driver = Browser.open_browser()
        self.mylogin = LoginPage(self.driver)
        self.mylogin.login_fris('admin')

    def tearDown(self):
        self.Position = Position(self.driver)
        self.driver.quit()

    @parameterized.expand([('新增职务','职务名称','创建时间','修改时间','操作')])
    def test_01_positio_page(self,new_additional,position,create_time,modification_time,operation):
        '''
        验证职务页格式正确
        '''
        self.posion = Position(self.driver)
        page_list = self.posion.check_position_page()
        self.assertEqual(new_additional,page_list[0])
        self.assertEqual(position,page_list[1])
        self.assertEqual(create_time,page_list[2])
        self.assertEqual(modification_time,page_list[3])
        self.assertEqual(operation,page_list[4])
        log.info("TestCase:验证职务页格式正确<br/>")

    @parameterized.expand([("测试新增职务")])
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
        self.assertTrue(self.position.find_position_name(position))
        time.sleep(1)
        self.position.delete_position(position)
        time.sleep(1)

    @parameterized.expand([("测试修改新增职务","测试修改职务")])
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
        self.assertTrue(self.position.find_position_name(modify_position))
        time.sleep(1)
        self.position.delete_position(modify_position)
        time.sleep(1)

    @parameterized.expand([("测试删除新增职务")])
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
        self.assertFalse(self.position.find_position_name(delete_position))
