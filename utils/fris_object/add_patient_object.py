class AddPatientObject:
    def __init__(self):
        self._name = None
        self._patient_type = None
        self._functional = None
        self._attending_doctor = None

    @property
    def name(self):
        """
        姓名
        """
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def patient_type(self):
        """
        疾病类型
        """
        return self._patient_type

    @patient_type.setter
    def patient_type(self, patient_type):
        self._patient_type = patient_type

    @property
    def functional(self):
        """
        功能诊断
        """
        return self._functional

    @functional.setter
    def functional(self, functional):
        self._functional = functional

    @property
    def attending_doctor(self):
        """
        主治医生
        """
        return self._attending_doctor

    @attending_doctor.setter
    def attending_doctor(self, attending_doctor):
        self._attending_doctor = attending_doctor
