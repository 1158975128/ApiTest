import os
import time

import pytest
from utils.browser_tool import Browser
from page_objects.login.login_page import LoginPage
from page_objects.treatment_preserve.treatment_equipmemt import Equipment
from page_objects.treatment_preserve.region import Region
from page_objects.navigate_bar import NavigateBar
from utils.object_map import ObjectMap
from selenium.webdriver.common.by import By
from page_objects.login.logout import Logout



map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
equipment_map = map_path + "/treatment_preserve/treatment_equipment.xml"

@pytest.fixture(scope='class', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin')
    yield
    logout = Logout(driver)
    logout.logout()


class TestTreat:
    def setup_class(self):
        self.treat_map = ObjectMap(equipment_map)

    @pytest.mark.parametrize('number,name,shorter,exp_msg', [('a01','徒手','徒手', '新增成功'),('a02','上肢康复治疗仪','上肢', '新增成功'),('a03','冲击波治疗仪','冲击波', '新增成功'),('a04','干扰电治疗仪','干扰点电', '新增成功')])
    def test_add_equipment(self, driver,number,name,shorter,exp_msg):
        self.equipment = Equipment(driver)
        self.equipment.add_new_equipment(number,name,shorter)
        time.sleep(1)
        assert self.equipment.find_equipment_name(name) is True

    @pytest.mark.parametrize('name', [('头部'),('四肢'),('全身')])
    def test_add_region(self, driver,name):
        self.region = Region(driver)
        self.region.add_new_region(name)
        time.sleep(1)
        assert self.region.find_region(name) is True