import os
import pytest
from page_objects.login.login import LoginPage
from utils.browser_tool import Browser
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
from page_objects.navigate_bar import NavigateBar
from config.account_info import doctorZhaoEmail, doctorZhaoPwd
from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException


map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
add_patient_map = map_path + "/patient/add_patient.xml"


@pytest.fixture(scope='module', autouse=True)
def login(driver):
    login = LoginPage(driver)
    login.login_fris(doctorZhaoEmail, doctorZhaoPwd)
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
    def setup_class(self):
        self.add_patient_map = ObjectMap(add_patient_map)

    @pytest.mark.parametrize('name,exp_msg',[('张三', None), ('123', None), ('!.', None), ('abc', None),
                                             ('一二三四五六七八九十一二三四五六七八九十一二', '最多21个汉字'),
                                             ('', '此项为必填')])
    def test_name(self, driver, add_patient_dialog, name, exp_msg):
        """
        TestCase: 姓名输入框格式校验
        :param driver: 驱动
        :param add_patient_dialog: 打开对话框fixture
        :param name: 姓名
        :param exp_msg: 报警信息
        """
        print("姓名框输入：%s" % name)
        add_patient = AddPatient(driver)
        name_input = self.add_patient_map.getLocator(driver, 'NameInput')
        name_input.click()
        name_input.send_keys(name)
        add_patient.click_add_patient_dialog_title()
        # add_patient_dialog_title = self.add_patient_map.getLocator(driver, 'AddPatientTitle')
        # add_patient_dialog_title.click()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'NameInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.text)
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'NameInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('identity_number,exp_msg',[('37098319941107224X', None), ('', None),
                                                        ('123', '身份证号错误'), ('abc', '身份证号错误'),
                                                        ('一二三', '身份证号错误'), ('!.', '身份证号错误')])
    def test_identity_number(self, driver, add_patient_dialog, identity_number, exp_msg):
        """
        TestCase: 身份证号格式校验
        :param driver: 驱动
        :param add_patient_dialog: 打开对话框fixture
        :param identity_number: 身份证号
        :param exp_msg: 预计报警信息
        """
        print("身份证号输入框：%s" % str(identity_number))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        identity_number_input = self.add_patient_map.getLocator(driver, 'IdentityNumberInput')
        identity_number_input.click()
        identity_number_input.send_keys(identity_number)
        add_patient.click_add_patient_dialog_title()


