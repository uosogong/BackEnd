from locust import HttpUser, task, between

class SogoingLoadTest(HttpUser):
    # 사용자가 요청 사이에 대기하는 시간 (초)
    wait_time = between(1, 3)

    @task
    def test_homepage(self):
        # 홈페이지에 GET 요청
        self.client.get("/departments")

    @task
    def test_department_list(self):
        # 부서 리스트를 가져오는 API 호출
        self.client.get("/departments/top")