import os
import pytest
from utils.browser_tool import Browser
from page_objects.login.login_page import LoginPage
from page_objects.system_management.organization_management import OrgManage
from page_objects.navigate_bar import NavigateBar
from utils.object_map import ObjectMap
from page_objects.login.logout import Logout
import random


map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
org_map = map_path + "/system_management/organization_management.xml"

@pytest.fixture(scope='class', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin','傅利叶医院_ui_别碰我')
    yield
    logout = Logout(driver)
    logout.logout()

class TestAddOrg:
    def setup_class(self):
        self.add_treat_map = ObjectMap(org_map)

    @pytest.mark.parametrize('name,exp_msg', [('冒烟测试机构', '新增成功')])
    def test_name(self, driver,name, exp_msg):
        self.org = OrgManage(driver)
        while True:
            name = name+str(random.randint(1,700))
            result = self.org.add_new_org(name)
            if result == exp_msg:
                break

