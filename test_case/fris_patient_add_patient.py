import os
import time
import pytest
import pytest_check as check
from page_objects.login.login import LoginPage
from utils.browser_tool import Browser
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
from page_objects.navigate_bar import NavigateBar
from config.account_info import doctorZhaoEmail, doctorZhaoPwd
from config.public_data.patient import Sex
from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.actions.key_actions import KeyActions


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

    @pytest.mark.parametrize('name,exp_msg', [('张三', None), ('123', None), ('!.', None), ('abc', None),
                                              ('一二三四五六七八九十一二三四五六七八九十一二', '最多21个汉字'),
                                              ('', '此项为必填')])
    def test_name(self, driver, add_patient_dialog, name, exp_msg):
        """
        TestCase: 姓名输入框格式校验
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
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'NameInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('identity_number,exp_msg', [('37098319941107224X', None), ('', None),
                                                         ('123', '身份证号错误'), ('abc', '身份证号错误'),
                                                         ('一二三', '身份证号错误'), ('!.', '身份证号错误')])
    def test_identity_number(self, driver, add_patient_dialog, identity_number, exp_msg):
        """
        TestCase: 身份证号格式校验
        """
        print("身份证号输入：%s" % str(identity_number))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        identity_number_input = self.add_patient_map.getLocator(driver, 'IdentityNumberInput')
        identity_number_input.click()
        identity_number_input.send_keys(identity_number)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'IdentityNumberInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'IdentityNumberInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('age,exp_msg', [(0, None), (200, None), ('', None), (201, '0-200'),
                                             ('-1', '0-200'), ('100.5', '0-200'), ('abc', '0-200'),
                                             ('一二三', '0-200'), ('?.', '0-200')])
    def test_age(self, driver, add_patient_dialog, age, exp_msg):
        """
        TestCase: 校验年龄格式
        """
        print("年龄输入：%s" % str(age))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        age_input = self.add_patient_map.getLocator(driver, 'AgeInput')
        age_input.click()
        age_input.send_keys(age)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'AgeInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'AgeInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('height,exp_msg', [(0, None), (300, None), ('', None), ('100.1', '0-300'),
                                                (301, '0-300'), ('-1', '0-300'), ('abc', '0-300'),
                                                ('一二三', '0-300'), ('?.', '0-300')])
    def test_height(self, driver, add_patient_dialog, height, exp_msg):
        """
        TestCase: 校验身高格式
        """
        print("身高输入：%s" % str(height))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        height_input = self.add_patient_map.getLocator(driver, 'HeightInput')
        height_input.click()
        height_input.send_keys(height)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'HeightInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'HeightInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('weight,exp_msg', [(0, None), (1000, None), ('', None), ('100.1', None),
                                                ('100.11', '请填写数字(保留一位小数)'), (1001, '0-1000'),
                                                ('-1', '0-1000'), ('abc', '0-1000'), ('一二三', '0-1000'),
                                                ('?.', '0-1000')])
    def test_weight(self, driver, add_patient_dialog, weight, exp_msg):
        """
        TestCase: 校验体重格式
        """
        print("体重输入：%s" % str(weight))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        weight_input = self.add_patient_map.getLocator(driver, 'WeightInput')
        weight_input.click()
        weight_input.send_keys(weight)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'WeightInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'WeightInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('phone,exp_msg', [(0, None), ('01-2', None),
                                               ('01234567890123456789012345678901', None),
                                               ('012345678901234567890123456789012', '请填写正确的手机号'),
                                               ('abc', '请填写正确的手机号'), ('一二三', '请填写正确的手机号'),
                                               ('?.', '请填写正确的手机号')])
    def test_phone(self, driver, add_patient_dialog, phone, exp_msg):
        """
        TestCase: 校验手机号格式
        """
        print("手机号输入：%s" % str(phone))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        phone_input = self.add_patient_map.getLocator(driver, 'PhoneInput')
        phone_input.click()
        phone_input.send_keys(phone)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'PhoneInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'PhoneInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('email,exp_msg', [('1@fftai.com', None), ('.@fftai', None), ('', None),
                                               ('012345678901234567890123456789012345678901234567890123456789@123', None),
                                               ('012345678901234567890123456789012345678901234567890123456789@1234', '请填写64位以内字符'),
                                               ('1@', '请填写正确的邮箱'), ('@fftai', '请填写正确的邮箱'),
                                               ('。@fftai', '请填写正确的邮箱'), ('abc', '请填写正确的邮箱')])
    def test_email(self, driver, add_patient_dialog, email, exp_msg):
        """
        TestCase: 校验邮箱格式
        """
        print("邮箱输入：%s" % str(email))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        email_input = self.add_patient_map.getLocator(driver, 'EmailInput')
        email_input.click()
        email_input.send_keys(email)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'EmailInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'EmailInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('linkman_phone,exp_msg', [(0, None), ('01-2', None),
                                                       ('01234567890123456789012345678901', None),
                                                       ('012345678901234567890123456789012', '请填写正确的手机号'),
                                                       ('abc', '请填写正确的手机号'), ('一二三', '请填写正确的手机号'),
                                                       ('?.', '请填写正确的手机号')])
    def test_linkman_phone(self, driver, add_patient_dialog, linkman_phone, exp_msg):
        """
        TestCase: 校验紧急电话格式
        """
        print("紧急电话输入：%s" % str(linkman_phone))
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        linkman_phone_input = self.add_patient_map.getLocator(driver, 'LinkmanPhoneInput')
        linkman_phone_input.click()
        linkman_phone_input.send_keys(linkman_phone)
        add_patient.click_add_patient_dialog_title()
        if exp_msg is None:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'LinkmanPhoneInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.add_patient_map.getLocator(driver, 'LinkmanPhoneInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('search_word_1,search_word_2,exp_patient_type_1,exp_patient_type_2,exp_msg',
                             [('头晕和眩晕', 'E10.500x051', '头晕和眩晕(R42.x00)', '1型糖尿病性下肢感染(E10.500x051)',
                               '此项为必填')])
    def test_patient_type(self, driver, add_patient_dialog, search_word_1, search_word_2, exp_patient_type_1,
                          exp_patient_type_2, exp_msg):
        """
        TestCase:
        """
        print("两个搜索关键词分别为%(search_word_1)s和%(search_word_2)s" % {"search_word_1": search_word_1, "search_word_2": search_word_2})
        print("两个期待结果为%(exp_1)s和%(exp_2)s" % {"exp_1": exp_patient_type_1, "exp_2": exp_patient_type_2})
        add_patient = AddPatient(driver)
        patient_type_box = self.add_patient_map.getLocator(driver, 'PatientTypeBox')
        print("选择疾病类型")
        # js = "document.querySelector('label[for=\"type\"]+div > div > div > div > div').innerHTML=\"%s\"" % search_word_1
        # driver.execute_script(js)
        patient_type_box.send_keys(search_word_1)
        add_patient.select_drop_down_item_one()
        js = "document.querySelector('label[for=\"type\"]+div > div > div > div > div').innerHTML=\"%s\"" % search_word_2
        driver.execute_script(js)
        add_patient.select_drop_down_item_one()
        act_patient_type_1 = self.add_patient_map.getLocator(driver, 'PatientTypeOne').get_attribute('textContent')
        act_patient_type_2 = self.add_patient_map.getLocator(driver, 'PatientTypeTwo').get_attribute('textContent')
        assert act_patient_type_1 == exp_patient_type_1, "所选疾病类型不正确，失败！"
        assert act_patient_type_2 == exp_patient_type_2, "所选疾病类型不正确，失败！"
        print("删除所选疾病类型")
        close_btn = self.add_patient_map.getLocator(driver, 'PatientTypeTwoCloseBtn')
        close_btn.click()
        close_btn = self.add_patient_map.getLocator(driver, 'PatientTypeOneCloseBtn')
        close_btn.click()
        print("查看提示信息")
        act_msg = self.add_patient_map.getLocator(driver, 'PatientTypeError').get_attribute('textContent')
        assert act_msg == exp_msg, "疾病类型提示语不正确，失败！"

    @pytest.mark.parametrize('search_word_1,search_word_2,exp_functional_1,exp_functional_2,exp_msg',
                             [('Fine foot use', 'b830', 'Fine foot use(d446)', '皮肤的其他功能(b830)',
                               '此项为必填')])
    def test_functional(self, driver, add_patient_dialog, search_word_1, search_word_2, exp_functional_1,
                        exp_functional_2, exp_msg):
        """
        TestCase: 1. 输入两个功能诊断，选择验证正确性  2. 清空该输入框，验证提示信息
        """
        print("两个搜索关键词分别为%(search_word_1)s和%(search_word_2)s" % {"search_word_1": search_word_1,
                                                                 "search_word_2": search_word_2})
        print("两个期待结果为%(exp_1)s和%(exp_2)s" % {"exp_1": exp_functional_1, "exp_2": exp_functional_2})
        add_patient = AddPatient(driver)
        print("选择功能诊断")
        functional_box = self.add_patient_map.getLocator(driver, 'FunctionalBox')
        # driver.implicitly_wait(10)
        functional_box.send_keys(search_word_1)
        print("已输入功能诊断1")
        # driver.implicitly_wait(10)
        add_patient.select_drop_down_item_one()
        print("已选功能诊断1")
        # driver.implicitly_wait(10)
        functional_box.send_keys(search_word_2)
        print("已输入功能诊断2")
        # driver.implicitly_wait(10)
        add_patient.select_drop_down_item_one()
        print("已选择功能诊断2")
        driver.implicitly_wait(10)
        act_functional_1 = self.add_patient_map.getLocator(driver, 'FunctionalOne').get_attribute('textContent')
        act_functional_2 = self.add_patient_map.getLocator(driver, 'FunctionalTwo').get_attribute('textContent')
        assert act_functional_1 == exp_functional_1, "所选功能诊断不正确，失败！"
        assert act_functional_2 == exp_functional_2, "所选功能诊断不正确，失败！"
        print("删除所选功能诊断")
        close_btn = self.add_patient_map.getLocator(driver, 'FunctionalTwoCloseBtn')
        close_btn.click()
        close_btn = self.add_patient_map.getLocator(driver, 'FunctionalOneCloseBtn')
        close_btn.click()
        print("查看提示信息")
        act_msg = self.add_patient_map.getLocator(driver, 'FunctionalError').get_attribute('textContent')
        assert act_msg == exp_msg, "功能诊断提示语不正确，失败！"

    @pytest.mark.parametrize('exp_list', [('护理一级', '护理二级', '护理三级')])
    def test_nursing_level(self, driver, add_patient_dialog, exp_list):
        """
        TestCase: 查看护理等级下拉菜单
        """
        add_patient = AddPatient(driver)
        add_patient.expand_hospital_info_module()
        nursing_level_box = self.add_patient_map.getLocator(driver, 'NursingLevelBox')
        nursing_level_box.click()
        nursing_level_drop_down = self.add_patient_map.getLocator(driver, 'DropDown')
        nursing_level_list = nursing_level_drop_down.find_elements(By.CSS_SELECTOR, 'li > span')
        act_nursing_level_list = tuple([item.get_attribute('textContent') for item in nursing_level_list])
        assert act_nursing_level_list == exp_list, '护理等级列表错误'

    @pytest.mark.parametrize('identity_number,exp_sex,exp_birthday,exp_age,exp_address',
                             [('230882197703172611', Sex.male, '1977-03-17', '45', '黑龙江省佳木斯市富锦市')])
    def test_identity_number_auto_input(self, driver, add_patient_dialog, identity_number, exp_sex, exp_birthday,
                                        exp_age, exp_address):
        """
        TestCase: 输入身份证号，验证自动填充的性别、生日、年龄、地址
        """
        print("输入的身份证号是%(identity_number)s，自动填充性别：%(sex)s，生日：%(birthday)s，年龄：%(age)s，地址：%(address)s" %
              {"identity_number": identity_number, "sex": exp_sex.value, "birthday": exp_birthday, "age": exp_age,
               "address": exp_address})
        add_patient = AddPatient(driver)
        add_patient.expand_basic_info_module()
        identity_number_input = self.add_patient_map.getLocator(driver, 'IdentityNumberInput')
        identity_number_input.click()
        identity_number_input.send_keys(identity_number)
        add_patient.click_add_patient_dialog_title()
        sex_male_box = self.add_patient_map.getLocator(driver, 'SexMale')
        act_birthday = driver.execute_script(
            "document.querySelector('label[for=\"birthday\"]+.el-form-item__content .el-input__inner').value")
        act_age = driver.execute_script(
            "document.querySelector('label[for=\"age\"]+.el-form-item__content .el-input__inner').value")
        act_address = driver.execute_script(
            "document.querySelector('label[for=\"address\"]+.el-form-item__content .el-input__inner').value")
        check.is_in("el-tag--dark", sex_male_box.get_attribute("class"), "性别男未选择，失败！")
        check.equal(act_birthday, exp_birthday, "生日错误，失败！")
        check.equal(act_age, exp_age, "年龄错误，失败！")
        check.equal(act_address, exp_address, "地址错误，失败！")

    @pytest.mark.parametrize('source,exp_hospital_number_label', [('住院', '住院号'), ('其他', '就诊号')])
    def test_hospital_number_label(self, driver, add_patient_dialog, source, exp_hospital_number_label):
        """
        TestCase: 点击来源（住院，其他），验证住院号标签
        """
        source_btn = driver.find_element(By.XPATH,
                                         '//label[@for="sourceId"]/following-sibling::div/descendant::span[text()="%s"]' % source)
        source_btn.click()
        act_hospital_number_label = self.add_patient_map.getLocator(driver, 'HospitalNumberLabel')
        assert act_hospital_number_label.get_attribute('textContent') == exp_hospital_number_label, "住院(就诊)号文字不正确，失败！"

    def test_add_patient(self, driver, add_patient_dialog):
        """
        TestCase: 添加一个患者（仅必填项），查看结果
        """
        add_patient = AddPatient(driver)
        exp_add_patient = add_patient.add_patient_required()
