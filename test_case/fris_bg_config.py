import os
import time
import pytest
from page_objects.login.login_page import LoginPage
from page_objects.treatment_preserve.treatment_equipment import Equipment
from page_objects.treatment_preserve.region import Region
from page_objects.department_preserve.job_type import JobType
from page_objects.department_preserve.department import Department_Type
from page_objects.department_preserve.position import Position
from page_objects.department_preserve.office import Office
from page_objects.department_preserve.title import Title
from page_objects.department_preserve.job import Job
from page_objects.navigate_bar import NavigateBar
from utils.object_map import ObjectMap
from selenium.webdriver.common.by import By
from page_objects.login.logout import Logout



map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
equipment_map = map_path + "/treatment_preserve/treatment_equipment.xml"

@pytest.fixture(scope='module', autouse=True)
def login(driver):
    mylogin = LoginPage(driver)
    mylogin.login_fris('admin')
    # yield
    # logout = Logout(driver)
    # logout.logout()


class TestDepartment:
    @pytest.mark.parametrize('name', [('PT'),('OT'),('ST'),('传统')])
    def test_add_job_type(self, driver,name):
        self.type = JobType(driver)
        self.type.add_job_type(name)
        time.sleep(1)
        assert self.type.find_job_type(name) is True

    @pytest.mark.parametrize('name', [('门诊'),('住院'),('急诊')])
    def test_add_department(self, driver,name):
        self.department = Department_Type(driver)
        self.department.add_new_department(name)
        time.sleep(1)
        assert self.department.find_department(name) is True

    @pytest.mark.parametrize('name', [('医生'),('护士'),('护士长'),('治疗师'),('治疗师长'),('副主任'),('主任')])
    def test_add_position(self, driver,name):
        self.position = Position(driver)
        self.position.add_new_position(name)
        time.sleep(1)
        assert self.position.find_position(name) is True

    # 需要测试
    @pytest.mark.parametrize('name', [('骨科'),('康复科'),('神经科')])
    def test_add_office(self, driver,name):
        self.office = Office(driver)
        self.office.add_office(name)
        time.sleep(1)
        assert self.office.find_office(name) is True

    # 需要测试
    @pytest.mark.parametrize('name', [('康复治疗士'),('康复治疗师'),('中级康复技师'),('副主任康复师'),('主任康复技师'),('助理医师'),('住院医师'),('主治医师'),('副主任医师'),('主任医师'),('护师'),('主管护师'),('副主任护师'),('主任护师')])
    def test_add_title(self, driver,name):
        self.title = Title(driver)
        self.title.add_title(name)
        time.sleep(1)
        assert self.title.find_title(name) is True

    # 需要测试
    @pytest.mark.parametrize('name', [('护士'),('护士长'),('治疗师'),('治疗师长'),('医生')])
    def test_add_job(self, driver,name):
        self.job = Job(driver)
        self.job.add_job(name)
        time.sleep(1)
        assert self.job.find_job(name) is True

class TestSystem:
    @pytest.mark.parametrize('name', [('PT'),('OT'),('ST'),('传统')])
    def test_add_job_type(self, driver,name):
        self.type = JobType(driver)
        self.type.add_job_type(name)
        time.sleep(1)
        assert self.type.find_job_type(name) is True


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

    @pytest.mark.parametrize('name', [('头部'),('四肢'),('全身')])
    def test_add_project_template(self, driver,name):
        self.region = Region(driver)
        print(111)
        # self.region.add_new_region(name)
        # time.sleep(1)
        # assert self.region.find_region(name) is True