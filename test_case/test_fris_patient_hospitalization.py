import os
import time
import pytest
import pytest_check as check
from page_objects.login.login import LoginPage
from utils.browser_tool import Browser
from page_objects.login.login import LoginPage
from page_objects.login.logout import Logout
from page_objects.patient.add_patient import AddPatient
from config.account_info import doctor2Email, doctor2Pwd
from page_objects.patient.patient import Patient
from utils.close_tips_tool import close_login_tips
from utils.droplist_select_tool import Select
from page_objects.navigate_bar import NavigateBar
from config.public_data.patient import Sex
from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.actions.key_actions import KeyActions
from selenium.webdriver.common.by import By


map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
add_patient_map = map_path + "/patient/add_patient.xml"


@pytest.fixture(scope='module', autouse=True)
def login(driver):
    login = LoginPage(driver)
    login.login_fris(doctor2Email, doctor2Pwd)
    yield
    logout = Logout(driver)
    logout.logout()

class TestAddPatient:

    @pytest.mark.parametrize('name,identity,phone,profession,linkman,relation,disease,function,doctor,depart,source', [('李世杰','339001197512156328','13613913813','公务员','老张','父子','脑梗死','单侧身体肌肉的力量','医生2', '康复科','住院')])
    def test_add_patient(self, driver,name,identity,phone,profession,linkman,relation,disease,function,doctor,depart,source):
        """
        TestCase: 添加一个患者（仅必填项），查看结果
        """
        add_patient = AddPatient(driver)
        patient = Patient(driver)
        add_patient.add_new_patient(name,identity,phone,profession,linkman,relation,disease,function,doctor,depart,source)
        patient.find_patient(name)
        patient.click_patient_card(name)
        time.sleep(1)



    @pytest.mark.parametrize('name,item,region,department,bed_side,limit,dose,frequen,total,therapeutist,intern,notes,attention', [('李世杰','艾条灸','四肢','康复科','是','长期','3','3',None,'OT治疗师2','实习生小李','经颅磁治疗',None),
                                                                                                                                   ('李世杰','博巴斯训练','全身','康复科','否','短期','1','2','10',None,None,None,'注意事项1'),
                                                                                                                                   ('李世杰','手功能训练','全身','骨科','否','短期','1','2','10',None,None,None,'注意事项1')])
    def test_add_treatment_item(self, driver,name,item,region,department,bed_side,limit,dose,frequen,total,therapeutist,intern,notes,attention):
        """
        TestCase: 添加一个患者（仅必填项），查看结果
        """
        patient = Patient(driver)

        # patient.find_patient(name)
        # patient.click_patient_card(name)
        # time.sleep(1)
        patient.add_treatment_item(item,region,department,bed_side,limit,dose,frequen,total,therapeutist,intern,notes,attention)

    # 治疗项目指定治疗师
    @pytest.mark.parametrize('patient_name,limit,name,therapeutist,operation', [('李世杰','短期项目','博巴斯训练','刘玉栋-治疗师长','指定')])
    def test_checkbox_operation(self,driver,patient_name,limit,name,therapeutist,operation):
        patient = Patient(driver)
        # patient.find_patient(patient_name)
        # patient.click_patient_card(patient_name)
        # time.sleep(1)
        result = patient.checkbox_operation(limit,name,therapeutist,operation)
        print(result)
        time.sleep(1)

    # 岗位小类指定治疗师
    @pytest.mark.parametrize('patient_name,job_type,name', [('李世杰','OT','OT治疗师2')])
    def test_appoint_therapeutist(self,driver,patient_name,job_type,name):
        patient = Patient(driver)
        patient.find_patient(patient_name)
        # patient.click_patient_card(patient_name)
        time.sleep(1)
        patient.appoint_therapeutist(job_type,name)
