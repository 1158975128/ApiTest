from datetime import datetime


class PatientObject:
    def __init__(self):
        self._new_patient = None
        self._origin_droplist = None
        self._patient_droplist = None
        self._name_input = None
        self._date = None
        self._date_frame = None
        self._check_end_date = None

        self._check_name = None
        self._disease_name = None
        self._sex_value = None
        self._source = None
        self._sex_logo = None
        self._attending_doctor = None
        self._patient_department = None
        self._cost = None
        self._registation_time = None
        self._therapeutist = None
        self._appoint_therapeutist = None
        self._see_report = None

    @property
    def new_patient(self):
        return self._new_patient

    @new_patient.setter
    def new_patient(self, new_patient):
        self._new_patient = new_patient

    @property
    def origin_droplist(self):
        return self._origin_droplist

    @origin_droplist.setter
    def origin_droplist(self, origin_droplist):
        self._origin_droplist = origin_droplist

    @property
    def patient_droplist(self):
        return self._patient_droplist

    @patient_droplist.setter
    def patient_droplist(self, patient_droplist):
        self._patient_droplist = patient_droplist

    @property
    def name_input(self):
        return self._name_input

    @name_input.setter
    def name_input(self, name_input):
        self._name_input = name_input

    @property
    def date(self):
        return self._date

    @date.setter
    def date(self, date):
        self._date = date

    @property
    def date_frame(self):
        return self._date_frame

    @date_frame.setter
    def date_frame(self, date_frame):
        self._date_frame = date_frame

    @property
    def check_end_date(self):
        return self._check_end_date

    @check_end_date.setter
    def check_end_date(self, check_end_date):
        self._check_end_date = check_end_date

    @property
    def check_name(self):
        return self._check_name

    @check_name.setter
    def check_name(self, check_name):
        self._check_name = check_name

    @property
    def disease_name(self):
        return self._disease_name

    @disease_name.setter
    def disease_name(self, disease_name):
        self._disease_name = disease_name

    @property
    def sex_value(self):
        return self._sex_value

    @sex_value.setter
    def sex_value(self, sex_value):
        self._sex_value = sex_value

    @property
    def source(self):
        return self._source

    @source.setter
    def source(self, source):
        self._source = source

    @property
    def sex_logo(self):
        return self._sex_logo

    @sex_logo.setter
    def sex_logo(self, sex_logo):
        self._sex_logo = sex_logo

    @property
    def attending_doctor(self):
        return self._attending_doctor

    @attending_doctor.setter
    def attending_doctor(self, attending_doctor):
        self._attending_doctor = attending_doctor

    @property
    def patient_department(self):
        return self._patient_department

    @patient_department.setter
    def patient_department(self, patient_department):
        self._patient_department = patient_department

    @property
    def cost(self):
        return self._cost

    @cost.setter
    def cost(self, cost):
        self._cost = cost

    @property
    def registation_time(self):
        return self._registation_time

    @registation_time.setter
    def registation_time(self, registation_time):
        self._registation_time = registation_time

    @property
    def therapeutist(self):
        return self._therapeutist

    @therapeutist.setter
    def therapeutist(self, therapeutist):
        self._therapeutist = therapeutist

    @property
    def appoint_therapeutist(self):
        return self._appoint_therapeutist

    @appoint_therapeutist.setter
    def appoint_therapeutist(self, appoint_therapeutist):
        self._appoint_therapeutist = appoint_therapeutist

    @property
    def see_report(self):
        return self._see_report

    @see_report.setter
    def see_report(self, see_report):
        self._see_report = see_report
