import pytest
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
from page_objects.navigate_bar import NavigateBar
from config.account_info import doctorZhaoEmail, doctorZhaoPwd
from utils.object_map import ObjectMap

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    login = LoginPage(driver)
    login.login(doctorZhaoEmail, doctorZhaoPwd)
    yield
    logout = Logout(driver)
    logout.logout()

@pytest.fixture(scope='function')
def add_patient_dialog(driver):
    navigate = NavigateBar(driver)
    navigate.go_to_patient()
    add_patient_dialog = AddPatient(driver)
    add_patient_dialog.open_add_patient_dialog()
    yield
    add_patient_dialog.close_add_patient_dialog()


class TestAddPatient:
    @pytest.mark.parametrize('name,exp_msg',[('张三', None),
                                             ('一二三四五六七八九十一二三四五六七八九十一二', '最多21个汉字'),
                                             ('', '此项为必填')])
    def test_name(self, add_patient_dialog, name, exp_msg):