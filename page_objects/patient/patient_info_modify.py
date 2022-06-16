import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from page_objects.navigate_bar_all.navigate_version import VersionExpanded

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
patient_map = map_path + "/patient/patient_manage.xml"

delay_time = DelayTime.short_time.value


class Patient_Manage():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(patient_map)
        self.patient = NavigateBar(self.driver)

    def check_patient_page(self):
        '''
        检查页面元素是否正确
        :return: new_patient,origin_droplist,patient_droplist
        '''
        self.patient.go_to_patient()
        time.sleep(1)
        new_patient = self.version.getLocator(self.driver, "New_patient").text
        origin_droplist = self.version.getLocator(self.driver, "Origin_droplist").get_attribute('value')
        patient_droplist = self.version.getLocator(self.driver, "Patient_droplist").get_attribute('value')
        return new_patient,origin_droplist,patient_droplist

    def add_new_patient(self,patient,disease,diagnosis):
        '''
        新增一个住院患者
        :param patient: 传入患者姓名姓名
        :param disease: 传入疾病类型
        :param diagnosis: 传入功能诊断
        :return: 成功返回True ，失败返回False
        '''
        self.patient.go_to_patient()
        self.driver.implicitly_wait(20)
        new_patient = self.version.getLocator(self.driver, "New_patient")
        new_patient.click()
        patient_name = self.version.getLocator(self.driver, "Patient_name")
        patient_name.send_keys(patient)
        sex = self.version.getLocator(self.driver, "Sex_man")
        sex.click()
        disease_type = self.version.getLocator(self.driver, "Disease_type")
        disease_type.click()
        time.sleep(1)
        disease_type.send_keys(disease)
        disease_type_select = self.version.getLocator(self.driver, "Disease_type_select")
        disease_type_select.click()
        time.sleep(2)
        origin = self.version.getLocator(self.driver, "Origin")
        origin.click()
        time.sleep(1)
        function_diagnosis = self.version.getLocator(self.driver, "Function_diagnosis")
        function_diagnosis.click()
        time.sleep(1)
        function_diagnosis.send_keys(diagnosis)
        function_diagnosis_select = self.version.getLocator(self.driver, "Function_diagnosis_select")
        function_diagnosis_select.click()
        time.sleep(1)
        doctor = self.version.getLocator(self.driver, "Doctor")
        doctor.click()
        time.sleep(1)
        doctor_select = self.version.getLocator(self.driver, "Doctor_select")
        doctor_select.click()
        time.sleep(1)
        deparetment = self.version.getLocator(self.driver, "Department")
        deparetment.click()
        time.sleep(1)
        deparetment_select = self.version.getLocator(self.driver, "Department_select")
        deparetment_select.click()
        time.sleep(1)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        tips = self.version.getLocator(self.driver, "Tips").text.strip()
        if tips == '登记患者成功！':
            print("新增患者成功：", tips)
            return True
        else:
            return False
        log.info("新增成功")

    def find_patient(self,patient_name):
        '''
        查找住院患者
        :param patient_name: 传入查找的患者姓名
        '''
        self.patient.go_to_patient()
        time.sleep(3)
        origin_select = self.version.getLocator(self.driver, "Origin_select")
        origin_select.click()
        time.sleep(1)
        hospital_select = self.version.getLocator(self.driver, "Hospital_select")
        hospital_select.click()
        time.sleep(1)
        name_input = self.version.getLocator(self.driver, "Name_input")
        name_input.send_keys(patient_name)
        time.sleep(1)


    def add_treatment_item(self,patient_name,item):
        '''
        新增一个治疗项目
        :param patient_name: 传入患者姓名
        :param item: 治疗项目名称
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text,patient_name)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
            add_Treatment.click()
            item_name = self.version.getLocator(self.driver, "Item_name")
            item_name.click()
            time.sleep(1)
            item_name.send_keys(item)
            time.sleep(1)
            item_name_select = self.version.getLocator(self.driver, "Item_name_select")
            item_name_select.click()
            time.sleep(1)
            region_name = self.version.getLocator(self.driver, "Region_name")
            region_name.click()
            time.sleep(1)
            region_name_select = self.version.getLocator(self.driver, "Region_name_select")
            region_name_select.click()
            time.sleep(1)
            department_name = self.version.getLocator(self.driver, "Department_name")
            department_name.click()
            time.sleep(1)
            department_name_select = self.version.getLocator(self.driver, "Department_name_select")
            department_name_select.click()
            time.sleep(1)
            item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
            item_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "新增项目成功":
                print("新增治疗项目成功：",tips)
                return True
            else:
                return False


    def add_judge_item(self,patient_name,item):
        '''
        新增一个评定项目
        :param patient_name: 传入患者姓名
        :param item: 传入评定项目
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text,patient_name)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            Judge_item = self.version.getLocator(self.driver, "Judge_Item")
            Judge_item.click()
            add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
            add_Treatment.click()
            item_name = self.version.getLocator(self.driver, "Item_name")
            item_name.click()
            time.sleep(1)
            item_name.send_keys(item)
            time.sleep(1)
            item_name_select = self.version.getLocator(self.driver, "Item_name_select")
            item_name_select.click()
            time.sleep(1)
            region_name = self.version.getLocator(self.driver, "Region_name")
            region_name.click()
            time.sleep(1)
            region_name_select = self.version.getLocator(self.driver, "Region_name_select")
            region_name_select.click()
            time.sleep(1)
            hospital_department_name = self.version.getLocator(self.driver, "Hospital_Department_name")
            hospital_department_name.click()
            time.sleep(1)
            department_name_select = self.version.getLocator(self.driver, "Department_name_select")
            department_name_select.click()
            time.sleep(1)
            item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
            item_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "新增项目成功":
                print("新增治疗项目成功：",tips)
                return True
            else:
                return False

    def leave_hospital(self,patient_name):
        '''
        测试住院患者结束疗程出院
        :param patient_name: 出院患者的姓名
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            patientInfo = self.version.getLocator(self.driver, "PatientInfo")
            patientInfo.click()
            end_treatment = self.version.getLocator(self.driver, "End_Treatment")
            end_treatment.click()
            leave_time = self.version.getLocator(self.driver, "Time")
            leave_time.click()
            time.sleep(1)
            choose_time = self.version.getLocator(self.driver, "Choose_Time")
            choose_time.click()
            time.sleep(1)
            leave_ensure = self.version.getLocator(self.driver, "Leave_Ensure")
            leave_ensure.click()
            time.sleep(1)
            again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
            again_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "结束疗程成功":
                print("结束疗程成功：",tips)
                return True
            else:
                return False



    def click_patientInfo(self):
        '''
        在患者管理详情页点击患者信息办理出院
        :return: 成功返回True ，失败返回False
        '''
        patientInfo = self.version.getLocator(self.driver, "PatientInfo")
        patientInfo.click()
        end_treatment = self.version.getLocator(self.driver, "End_Treatment")
        end_treatment.click()
        leave_time = self.version.getLocator(self.driver, "Time")
        leave_time.click()
        time.sleep(1)
        choose_time = self.version.getLocator(self.driver, "Choose_Time")
        choose_time.click()
        time.sleep(1)
        leave_ensure = self.version.getLocator(self.driver, "Leave_Ensure")
        leave_ensure.click()
        time.sleep(1)
        again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
        again_ensure.click()
        time.sleep(1)
        tips = self.version.getLocator(self.driver, "Tips").text
        if tips == "结束疗程成功":
            print("结束疗程成功：", tips)
            return True
        else:
            return False