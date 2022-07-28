import os
import time
import pytest
from page_objects.login.login_page import LoginPage
from page_objects.treatment_preserve.treatment_equipment import Equipment
from page_objects.treatment_preserve.region import Region
from page_objects.treatment_preserve.project_template import ProjectTemplate
from page_objects.department_preserve.job_type import JobType
from page_objects.department_preserve.department import Department_Type
from page_objects.department_preserve.position import Position
from page_objects.department_preserve.office import Office
from page_objects.department_preserve.title import Title
from page_objects.department_preserve.job import Job
from page_objects.system_management.unit import Unit
from page_objects.system_management.lease import Lease
from page_objects.system_management.professin import Profession
from page_objects.system_management.relation import Relation
from page_objects.system_management.time_template import TimeTemplate
from page_objects.system_management.pay import Pay
from page_objects.system_management.register import Register
from page_objects.system_management.personnel_list import PersonnelList
from utils.close_tips_tool import close_login_tips
from page_objects.navigate_bar import NavigateBar
from utils.object_map import ObjectMap
from selenium.webdriver.common.by import By
from page_objects.login.logout import Logout

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
        close_login_tips(driver)
        assert self.type.find_job_type(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('name', [('门诊'),('住院'),('急诊')])
    def test_add_department(self, driver,name):
        self.department = Department_Type(driver)
        self.department.add_new_department(name)
        close_login_tips(driver)
        assert self.department.find_department(name) is True
        time.sleep(1)


    @pytest.mark.parametrize('name', [('医生'),('护士'),('护士长'),('治疗师'),('治疗师长'),('副主任'),('主任')])
    def test_add_position(self, driver,name):
        self.position = Position(driver)
        self.position.add_new_position(name)
        close_login_tips(driver)
        assert self.position.find_position(name) is True
        time.sleep(1)

    # 需要测试
    @pytest.mark.parametrize('name', [('骨科'),('康复科'),('神经科')])
    def test_add_office(self, driver,name):
        self.office = Office(driver)
        self.office.add_office(name)
        close_login_tips(driver)
        assert self.office.find_office(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('name', [('康复治疗士'),('康复治疗师'),('中级康复技师'),('副主任康复师'),('主任康复技师'),('助理医师'),('住院医师'),('主治医师'),('副主任医师'),('主任医师'),('护师'),('主管护师'),('副主任护师'),('主任护师')])
    def test_add_title(self, driver,name):
        self.title = Title(driver)
        self.title.add_title(name)
        close_login_tips(driver)
        time.sleep(1)
        # self.title.find_title(name)

    @pytest.mark.skip(reason="新建机构默认有这些岗位")
    @pytest.mark.parametrize('name', [('护士'),('护士长'),('治疗师'),('治疗师长'),('医生')])
    def test_add_job(self, driver,name):
        self.job = Job(driver)
        self.job.add_job(name)
        close_login_tips(driver)
        assert self.job.find_job(name) is True
        time.sleep(1)

class TestSystem:
    @pytest.mark.parametrize('name', [('次'),('项'),('穴位')])
    def test_add_unit(self, driver,name):
        self.unit = Unit(driver)
        self.unit.add_unit(name)
        close_login_tips(driver)
        assert self.unit.find_job(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('name,number,price', [('眼视康治疗仪','S0001','100'),('上肢康复治疗仪','S0002','200'),('下肢康治疗仪','S0003','150')])
    def test_add_device(self, driver,name,number,price):
        self.device = Lease(driver)
        self.device.add_lease(name,number,price)
        close_login_tips(driver)
        assert self.device.find_lease(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('name', [('工人'),('农民'),('公务员')])
    def test_add_profession(self, driver,name):
        self.profession = Profession(driver)
        self.profession.add_profession(name)
        close_login_tips(driver)
        assert self.profession.find_profession(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('name', [('父子'),('母女'),('夫妻')])
    def test_add_relation(self, driver,name):
        self.relation = Relation(driver)
        self.relation.add_relation(name)
        close_login_tips(driver)
        assert self.relation.find_relation(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('mstart,mend,mlength,mgap,astart,aend,alength,agap', [('8:00','12:00','30','10','14:00','17:00','30','10')])
    def test_add_time_template(self, driver,mstart,mend,mlength,mgap,astart,aend,alength,agap):
        self.template = TimeTemplate(driver)
        self.template.add_time_template(mstart,mend,mlength,mgap,astart,aend,alength,agap)
        close_login_tips(driver)
        time.sleep(1)

    def test_pay(self, driver):
        self.pay = Pay(driver)
        self.pay.add_pay()
        close_login_tips(driver)
        time.sleep(1)

    @pytest.mark.parametrize('price', [('10.00')])
    def test_add_register(self, driver,price):
        self.register = Register(driver)
        result = self.register.add_register(price)
        close_login_tips(driver)
        assert result==price
        time.sleep(1)

class TestTreat:
    @pytest.mark.parametrize('number,name,shorter,exp_msg', [('a01','徒手','徒手', '新增成功'),('a02','上肢康复治疗仪','上肢', '新增成功'),('a03','冲击波治疗仪','冲击波', '新增成功'),('a04','干扰电治疗仪','干扰点电', '新增成功')])
    def test_add_equipment(self, driver,number,name,shorter,exp_msg):
        self.equipment = Equipment(driver)
        time.sleep(1)
        self.equipment.add_new_equipment(number,name,shorter)
        close_login_tips(driver)
        assert self.equipment.find_equipment_name(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('name', [('头部'),('四肢'),('全身')])
    def test_add_region(self, driver,name):
        self.region = Region(driver)
        self.region.add_new_region(name)
        close_login_tips(driver)
        assert self.region.find_region(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('number,name,p_type,bedside,unit,dose,price,freq,cycle,t_time,j_type', [('F00000058930','艾条灸','治疗','是','次','1', '20','1','短期','30','传统'),('F000032083','博巴斯训练','治疗','否','项','1','50','1','长期','30','PT'),('F00000061325','手功能训练','治疗','否','次','1','20','1','短期','20','OT'),
                                                                                                     ('F000031001','肌张力评定','评定','否','次','1','17','1','短期','30','PT'),('F000031002','日常生活能力评定','评定','否','次','1','21','1','短期','30','OT'),('F000031003','吞咽功能障碍评定','评定','否','次','1','35','1','短期','30','ST')])
    def test_add_project(self, driver,number,name,p_type,bedside,unit,dose,price,freq,cycle,t_time,j_type):
        self.project = ProjectTemplate(driver)
        self.project.add_new_project(number,name,p_type,bedside,unit,dose,price,freq,cycle,t_time,j_type)
        close_login_tips(driver)
        assert self.project.find_project_template(name) is True
        time.sleep(1)

    @pytest.mark.parametrize('email,email_pwd,email_type,name,sex,phone,pwd,con_pwd,office,depar,title,job,job_type,role', [('jssdezyy025@163.com','jssdezyy123','163','刘玉栋-医生','男','183896854215','abc123456@','abc123456@','康复科', '门诊','助理医师','医生','OTPT传统','康复医生护士'),
                                                                                                                            ('jssdezyy026@163.com','jssdezyy123','163','OT治疗师2','男','133896854215','abc123456@','abc123456@','康复科', '门诊','康复治疗士','治疗师','OT','治疗师')])


    # @pytest.mark.parametrize('email,email_pwd,email_type,name,sex,phone,pwd,con_pwd,office,depar,title,job,job_type,role', [('jssdezyy027@163.com','jssdezyy123','163','医生2','男','152896854215','abc123456@','abc123456@','骨科', '住院','住院医师','医生','OTPT传统','康复医生'),
    #                                                                                                                         ('jssdezyy028@163.com','jssdezyy123','163','刘玉栋-治疗师','男','175896854215','abc123456@','abc123456@','康复科', '门诊','康复治疗士','治疗师','PT','治疗师'),
    #                                                                                                                         ('jssdezyy029@163.com','jssdezyy123','163','OT治疗师2','女','138896854215','abc123456@','abc123456@','康复科', '门诊','康复治疗士','治疗师','OT传统','治疗师'),
    #                                                                                                                         ('jssdezyy030@163.com','jssdezyy123','163','刘玉栋-治疗师长','女','188896854215','abc123456@','abc123456@','康复科', '门诊','副主任康复师','治疗师','OTPTST传统','治疗师长')])
    def test_add_personnel(self, driver,email,email_pwd,email_type,name,sex,phone,pwd,con_pwd,office,depar,title,job,job_type,role):
        self.personnel = PersonnelList(driver)
        self.personnel.add_new_personnel(email,email_pwd,email_type,name,sex,phone,pwd,con_pwd,office,depar,title,job,job_type,role)
        close_login_tips(driver)
        assert self.personnel.find_personnel(name) is True
        time.sleep(1)