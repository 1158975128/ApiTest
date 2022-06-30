import os
import time

import pytest
from page_objects.login.login_page import LoginPage
from page_objects.login.logout import Logout
from page_objects.home_page.home_page_modify_info import HomePage
from page_objects.navigate_bar import NavigateBar
from selenium.webdriver.common.by import By

from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.actions.key_actions import KeyActions
from selenium.webdriver.common.action_chains import ActionChains

map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
home_page_path = map_path + "/home_page/home_page_modify_info.xml"

# S#1993

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('therapistAll')

class TestHomePageModify:
    def setup_class(self):
        self.home_page_map = ObjectMap(home_page_path)

    @pytest.mark.parametrize('name,exp_msg', [('张三', None), ('123', None), ('!.', None), ('abc', None),
                                              ('一二三四五六七八九十一二三四五六七八九十一二', '最多21个汉字')]) # ,('', '此项为必填')
    def test_name(self, driver, name, exp_msg):
        """
        TestCase: 姓名输入框格式校验
        """
        print("姓名框输入：%s" % name)
        driver.refresh()
        home_page_info = HomePage(driver)
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        name_input = self.home_page_map.getLocator(driver, 'NameInput')
        name_input.click()
        time.sleep(1)
        name_input.clear()
        name_input.send_keys(name)
        home_page_info.click_info_title()
        if exp_msg == "此项为必填":
            save = self.home_page_map.getLocator(driver, 'Save')
            save.click()
        if exp_msg is None:
            try:
                act_msg_area = self.home_page_map.getLocator(driver, 'NameInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.home_page_map.getLocator(driver, 'NameInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('birthday,exp_msg', [(None,'此项为必填'),('1', None)])
    def test_birthday(self, driver,birthday,exp_msg):
        """
        TestCase: 出生日期
        """
        driver.refresh()
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        time.sleep(2)
        if birthday is None:
            element = driver.find_element(By.CSS_SELECTOR,value='[placeholder="请选择日期"]')
            action = ActionChains(driver)
            action.move_to_element(element).perform()
            birthday_close = self.home_page_map.getLocator(driver, 'BirthdayClose')
            birthday_close.click()
        else:
            birthday_input = self.home_page_map.getLocator(driver, 'BirthdayInput')
            birthday_input.click()
            old_birthday = birthday_input.get_attribute('value')
            choose_month = self.home_page_map.getLocator(driver, 'ChooseMonth')
            choose_month.click()
            choose_birthday = self.home_page_map.getLocator(driver, 'ChooseBirthday')
            choose_birthday.click()
            time.sleep(1)
            new_birthday = birthday_input.get_attribute('value')
            assert old_birthday != new_birthday
        time.sleep(1)
        if exp_msg is None:
            try:
                act_msg_area = self.home_page_map.getLocator(driver, 'BirthdayInputError')
            except NoSuchElementException:
                print("无提示语，通过！")
                assert True
            else:
                assert False, "有提示信息(%s)，失败！" % str(act_msg_area.get_attribute('textContent'))
        else:
            try:
                act_msg_area = self.home_page_map.getLocator(driver, 'BirthdayInputError')
            except NoSuchElementException:
                assert False, "无提示语，失败！"
            else:
                assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"

    @pytest.mark.parametrize('sex,exp_msg', [('女', None), ('男', None)])
    def test_sex(self, driver, sex, exp_msg):
        """
        TestCase: 校验年龄格式
        """
        print("年龄输入：%s" % str(sex))
        # driver.refresh()
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        if sex == '女':
            print('is female')
            sex_female = self.home_page_map.getLocator(driver, 'SexFemale')
            sex_female.click()
            time.sleep(1)
            print(sex_female.get_attribute('class'))
        else:
            print('is male')
            sex_male = self.home_page_map.getLocator(driver, 'SexMale')
            sex_male.click()
            time.sleep(1)
            print(sex_male.get_attribute('class'))

    def test_phone(self, driver):
        """
        TestCase: 校验手机号为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        phone_input = self.home_page_map.getLocator(driver, 'PhoneInput')
        if phone_input.get_attribute('class').split(' ')[-1] == 'is-disabled':
            assert True
        else:
            assert False,"手机号输入框没有被禁用，失败！"


    def test_email(self, driver):
        """
        TestCase: 校验邮箱为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        email_input = self.home_page_map.getLocator(driver, 'EmailInput')
        time.sleep(1)
        if email_input.get_attribute('class').split(' ')[-1] == 'is-disabled':
            assert True
        else:
            assert False,"邮箱输入框没有被禁用，失败！"

    def test_offices(self, driver):
        """
        TestCase: 校验科室为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        offices_input = self.home_page_map.getLocator(driver, 'Offices')
        time.sleep(1)
        if offices_input.get_attribute('class').split(' ')[-2] == 'is-disabled':
            assert True
        else:
            assert False,"科室下拉框框没有被禁用，失败！"

    def test_department(self, driver):
        """
        TestCase: 校验部门为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        department_input = self.home_page_map.getLocator(driver, 'Department')
        time.sleep(1)
        if department_input.get_attribute('class').split(' ')[-2] == 'is-disabled':
            assert True
        else:
            assert False,"部门下拉框框没有被禁用，失败！"

    def test_title(self, driver):
        """
        TestCase: 校验职称为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        title_input = self.home_page_map.getLocator(driver, 'Title')
        time.sleep(1)
        if title_input.get_attribute('class').split(' ')[-2] == 'is-disabled':
            assert True
        else:
            assert False,"职称下拉框框没有被禁用，失败！"

    def test_position(self, driver):
        """
        TestCase: 校验所属岗位为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        positing_input = self.home_page_map.getLocator(driver, 'Position')
        time.sleep(1)
        if positing_input.get_attribute('class').split(' ')[-2] == 'is-disabled':
            assert True
        else:
            assert False,"所属岗位下拉框框没有被禁用，失败！"

    def test_post(self, driver):
        """
        TestCase: 校验职务为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        post_input = self.home_page_map.getLocator(driver, 'Post')
        time.sleep(1)
        if post_input.get_attribute('class').split(' ')[-2] == 'is-disabled':
            assert True
        else:
            assert False,"职务下拉框框没有被禁用，失败！"

    def test_job_category(self, driver):
        """
        TestCase: 校验岗位小类为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        job_category = driver.find_element(By.CSS_SELECTOR,value='.el-col:nth-child(7) [class="el-checkbox-group"]')
        categorys = job_category.find_elements(By.TAG_NAME, value='label')
        time.sleep(1)
        for category in categorys:
            if category.get_attribute('class').split(' ')[-2] == 'is-disabled':
                assert True
            else:
                assert False,"岗位小类没有被禁用，失败！"

    def test_role(self, driver):
        """
        TestCase: 校验角色为不可填
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        time.sleep(1)
        role = driver.find_element(By.CSS_SELECTOR,value='.el-col:nth-child(8) .el-checkbox-group')
        role_labels = role.find_elements(By.TAG_NAME, value='label')
        time.sleep(1)
        for role_label in role_labels:
            if role_label.get_attribute('class').split(' ')[-2] == 'is-disabled':
                assert True
            else:
                assert False,"角色没有被禁用，失败！"

    @pytest.mark.parametrize('name,exp_msg',[('修改名字1','修改成功')])
    def test_save_neme(self, driver,name,exp_msg):
        """
        TestCase: 校验成功保存修改
        """
        my_photo = self.home_page_map.getLocator(driver, 'My_Photo')
        my_photo.click()
        time.sleep(3)
        name_input = self.home_page_map.getLocator(driver, 'NameInput')
        time.sleep(1)
        old_name = name_input.get_attribute('value')
        name_input.click()
        time.sleep(1)
        name_input.clear()
        name_input.send_keys(name)
        save = self.home_page_map.getLocator(driver, 'Save')
        save.click()
        try:
            act_msg_area = self.home_page_map.getLocator(driver, 'Tips')
        except NoSuchElementException:
            assert False, "无提示语，失败！"
        else:
            assert act_msg_area.get_attribute('textContent') == exp_msg, "预期提示语不正确，失败!"
            name_input = self.home_page_map.getLocator(driver, 'NameInput')
            name_input.click()
            time.sleep(1)
            name_input.clear()
            name_input.send_keys(old_name)



    # @pytest.mark.parametrize('search_word_1,search_word_2,exp_patient_type_1,exp_patient_type_2,exp_msg',
    #                          [('头晕和眩晕', 'E10.500x051', '头晕和眩晕(R42.x00)', '1型糖尿病性下肢感染(E10.500x051)',
    #                            '此项为必填')])
    # def test_patient_type(self, driver, add_patient_dialog, search_word_1, search_word_2, exp_patient_type_1,
    #                       exp_patient_type_2, exp_msg):
    #     """
    #     TestCase:
    #     """
    #     print("两个搜索关键词分别为%(search_word_1)s和%(search_word_2)s" % {"search_word_1": search_word_1, "search_word_2": search_word_2})
    #     print("两个期待结果为%(exp_1)s和%(exp_2)s" % {"exp_1": exp_patient_type_1, "exp_2": exp_patient_type_2})
    #     add_patient = AddPatient(driver)
    #     patient_type_box = self.add_patient_map.getLocator(driver, 'PatientTypeBox')
    #     print("选择疾病类型")
    #     # js = "document.querySelector('label[for=\"type\"]+div > div > div > div > div').innerHTML=\"%s\"" % search_word_1
    #     # driver.execute_script(js)
    #     patient_type_box.send_keys(search_word_1)
    #     add_patient.select_drop_down_item_one()
    #     js = "document.querySelector('label[for=\"type\"]+div > div > div > div > div').innerHTML=\"%s\"" % search_word_2
    #     driver.execute_script(js)
    #     add_patient.select_drop_down_item_one()
    #     act_patient_type_1 = self.add_patient_map.getLocator(driver, 'PatientTypeOne').get_attribute('textContent')
    #     act_patient_type_2 = self.add_patient_map.getLocator(driver, 'PatientTypeTwo').get_attribute('textContent')
    #     assert act_patient_type_1 == exp_patient_type_1, "所选疾病类型不正确，失败！"
    #     assert act_patient_type_2 == exp_patient_type_2, "所选疾病类型不正确，失败！"
    #     print("删除所选疾病类型")
    #     close_btn = self.add_patient_map.getLocator(driver, 'PatientTypeTwoCloseBtn')
    #     close_btn.click()
    #     close_btn = self.add_patient_map.getLocator(driver, 'PatientTypeOneCloseBtn')
    #     close_btn.click()
    #     print("查看提示信息")
    #     act_msg = self.add_patient_map.getLocator(driver, 'PatientTypeError').get_attribute('textContent')
    #     assert act_msg == exp_msg, "疾病类型提示语不正确，失败！"
