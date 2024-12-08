package sogoing.backend_server

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import java.io.IOException
import kotlin.random.Random
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.dib.entity.Dib
import sogoing.backend_server.app.dib.repository.DibRepository
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.feedback.entity.enums.Busy
import sogoing.backend_server.app.feedback.entity.enums.Mood
import sogoing.backend_server.app.feedback.repository.FeedbackRepository
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.entity.UserRole
import sogoing.backend_server.app.user.repository.UserRepository
import sogoing.backend_server.common.error.exception.department.DepartmentNotFoundException

@Component
@Transactional
class DatabaseInitializer(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val departmentRepository: DepartmentRepository,
    private val dibRepository: DibRepository,
    private val feedBackRepository: FeedbackRepository,
) {
    @PostConstruct
    fun init() {
        try {
            val initDepartment = initDepartment()
            val initUser = initUser()

            initDib(initUser, initDepartment)
            initFeedback(initUser, initDepartment)
            println("DB init Finished")
        } catch (e: Exception) {}
    }

    private fun initFeedback(
        initUser: List<User>,
        initDepartment: List<Department>
    ): MutableList<Feedback> {
        val feedbackList =
            initUser
                .stream()
                .map {
                    val deptNum = getRandomNumber(initDepartment.size)
                    Feedback(
                        description = feedbacks[Random.nextInt(0, feedbacks.size - 1)],
                        department = initDepartment[deptNum],
                        rating = Random.nextInt(1, 5),
                        busy = Busy.getRandomBusy(),
                        mood = Mood.getRandomMood(),
                        user = it
                    )
                }
                .toList()

        return feedBackRepository.saveAll(feedbackList)
    }

    private fun initDib(initUser: List<User>, initDepartment: List<Department>): MutableList<Dib> {
        val dibs =
            initUser
                .stream()
                .map {
                    val deptNum = getRandomNumber(initDepartment.size)
                    Dib(user = it, department = initDepartment[deptNum])
                }
                .toList()
        return dibRepository.saveAll(dibs)
    }

    private fun getRandomNumber(size: Int): Int {
        return Random.nextInt(0, size - 1)
    }

    private fun initUser(): MutableList<User> {
        val userData =
            listOf(
                User(
                    phone = "010-1234-5678",
                    address = "Seoul, Korea",
                    role = UserRole.USER,
                    name = "Alice",
                    studentId = "20220010001",
                    email = "alice@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-2345-6789",
                    address = "Busan, Korea",
                    role = UserRole.USER,
                    name = "Bob",
                    studentId = "2022001502",
                    email = "bob@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-3456-7890",
                    address = "Daegu, Korea",
                    role = UserRole.USER,
                    name = "Charlie",
                    studentId = "2022100103",
                    email = "charlie@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-4567-8901",
                    address = "Incheon, Korea",
                    role = UserRole.USER,
                    name = "David",
                    studentId = "2022110004",
                    email = "david@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-5678-9012",
                    address = "Gwangju, Korea",
                    role = UserRole.USER,
                    name = "Eve",
                    studentId = "2022110005",
                    email = "eve@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-5678-9011",
                    address = "Gwangju, Korea",
                    role = UserRole.USER,
                    name = "Woojin",
                    studentId = "2022110005",
                    email = "Woojin@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-1234-5678",
                    address = "Seoul, Korea",
                    role = UserRole.USER,
                    name = "Goeun",
                    studentId = "20220010001",
                    email = "Goeun@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-1234-5678",
                    address = "Seoul, Korea",
                    role = UserRole.USER,
                    name = "정구홍",
                    studentId = "20220010001",
                    email = "정구홍@example.com",
                    password = encoder.encode("password123")
                ),
                User(
                    phone = "010-1234-5678",
                    address = "Seoul, Korea",
                    role = UserRole.USER,
                    name = "이강민",
                    studentId = "20220010001",
                    email = "이강민@example.com",
                    password = encoder.encode("password123")
                ),
            )
        return userRepository.saveAll(userData)
    }

    private fun initDepartment(): MutableList<Department> {
        val objectMapper = ObjectMapper()

        try {
            val rootNode = objectMapper.readTree(jsonData)
            val departmentsNode = rootNode.path("departments")
            val departments: MutableList<Department> = mutableListOf()
            val adminUsers: MutableList<User> = mutableListOf()
            // Access the departments
            var count = 1
            for (departmentNode in departmentsNode) {
                val name = departmentNode.path("name").asText()
                val introduction = departmentNode.path("introduction").asText()
                val user = makeAdminUser("department${count}")
                adminUsers.add(user)
                val department =
                    Department(
                        name = name,
                        introduction = introduction,
                        internRecruitment = false,
                        scholarshipRecruitment = false,
                        user = user
                    )
                departments.add(department)
                count++
            }
            userRepository.saveAll(adminUsers)
            return departmentRepository.saveAll(departments)
        } catch (e: IOException) {
            e.printStackTrace()
            throw DepartmentNotFoundException()
        }
    }

    private fun makeAdminUser(departmentName: String): User {
        return User.makeAdmin(
            SignUpRequest(
                name = departmentName,
                email = "${departmentName}@uos.ac.kr",
                password = departmentName,
                studentId = "",
                phone = "",
            ),
            encoder = encoder
        )
    }

    private val jsonData =
        """
        {
        	"departments": [
        	{
        		"name": "경영대학원",
        		"introduction": "1. 정보\n\n  - 근무내용 : 경영대학원 야간수업 지원\n - 근무장소 : 경영대학원 행정실 ( 미래관 5 층 508 호 )\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 주 4 일 / 1 일 6 시간 (10:00~17:00) / 12:00~13:00 점심시간 제외   ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "환경원예학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 환경원예학과 행정 업무 보조\n - 근무장소 : 자연과학대학 환경원예학과 사무실 ( 자연과학관 4층 8-411 호 )\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "행정학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 행정학과사무실 업무 보조\n - 근무장소 : 21  세기관 215   호\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "통계학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 통계학과 사무실 업무 보조\n - 근무장소 : 미래관 708 호 통계학과사무실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "첨단융합학부",
        		"introduction": "1. 정보\n\n  - 근무내용 : 첨단융합학부 사무실 행정 업무 보조\n - 근무장소 : 첨단융합학부 사무실 ( 자연과학관 310 호 )\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "중국어문화학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 중국어문화학과 행정 업무 보조\n - 근무장소 : 인문학관 216호(중국어문화학과 사무실)\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "컴퓨터과학부",
        		"introduction": "1. 정보\n\n  - 근무내용 : 컴퓨터과학부 업무 보조\n - 근무장소 : 정보기술관 202호\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "인공지능혁신융합대학사업단",
        		"introduction": "1. 정보\n\n  - 근무내용 : 인공지능혁신융합대학사업단 업무 보조\n - 근무장소 : 인공지능혁신융합대학사업단 사무실(정보기술관 103호)\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "인공지능학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 인공지능학과 업무 보조, 강의실 및 실험실습 기자재 관리\n - 근무장소 : 창공관  415  호 인공지능학과 사무실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "음악학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 음악학과 사무실 업무 보조\n - 근무장소 : 음악학과 사무실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "융합전공학부",
        		"introduction": "1. 정보\n\n  - 근무내용 : 융합전공학부 업무 보조\n - 근무장소 : 융합전공학부 사무실(창공관 303-2호)\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "스포츠과학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 스포츠과학과 사무실 행정 업무 보조\n - 근무장소 : 100주년기념관 나동 813호 스포츠과학과 사무실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "수학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 수학과 업무 보조\n - 근무장소 : 미래관  812 호 수학과사무실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "세무학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 세무학과 업무 보조\n - 근무장소 : 세무학과 사무실   (21  세기관   411  호  )\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "법학전문대학원",
        		"introduction": "1. 정보\n\n  - 근무내용 : 법학전문대학원 행정실 업무보조\n - 근무장소 : 법학관 533호 (법전원 행정실)\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 주 4 일 / 1 일 6 시간 (10:00~17:00) / 12:00~13:00 점심시간 제외   ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( psy2024@uos.ac.kr ) "
        	},
        	{
        		"name": "도시공학과",
        		"introduction": "1. 정보\n\n  - 근무내용 :도시공학과 업무 보조\n - 근무장소 : 배봉관 6-305 호 도시공학과사무실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "교양교육부",
        		"introduction": "1. 정보\n\n  - 근무내용 : 교양교육부 업무 보조\n - 근무장소 : 창공관 305 호 , 법학관 102 호 ( 교양교육부 행정실 및 법학관 강사실 )\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 주 4 일 / 1 일 6 시간 (10:00~17:00) / 12:00~13:00 점심시간 제외   ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "교무과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 교무과 행정 업무 보조\n - 근무장소 : 대학본부 3층 교무과\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "공학교육혁신센터",
        		"introduction": "1. 정보\n\n  - 근무내용 : 공학교육혁신센터 보조업무, 수업진행 보조\n - 근무장소 : 자연과학관 112-1호 공학교육혁신센터\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 주 4 일 / 1 일 6 시간 (10:00~17:00) / 12:00~13:00 점심시간 제외   ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( lee2dong@uos.ac.kr ) "
        	},
        	{
        		"name": "공간정보공학과",
        		"introduction": "1. 정보\n\n  - 근무내용 : 도시과학대학 공간정보공학과 업무 보조\n - 근무장소 : 공간정보공학과 사무실 (  21  세기관 602   호)\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "고시반",
        		"introduction": "1. 정보\n\n  - 근무내용 : 고시반 업무 보조\n - 근무장소 : 고시반 사무실 (법학관 405호)\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	},
        	{
        		"name": "경제학부",
        		"introduction": "1. 정보\n\n  - 근무내용 : 경제학부 PC실 관리\n - 근무장소 : 미래관 경제학부 PC실\n - 모집인원 : 1 명\n - 시급 : 12,000 원\n\n - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능\n\n - 근무시간 : 월~금 9:00 ~ 18:00 / 12:00~13:00 점심시간 제외 (시간 협의)  ※ 근무시간 협의가능\n\n\n2.  자격사항\n\n - 필수사항\n\n  - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)\n\n- 우대사항\n\n  - 성실하고 근면한 학생\n\n  - 사무 행정 경험자\n\n  - 엑셀 , 파워포인트 활용 가능 학생\n\n  - 장기근무 가능자\n\n3.  접수기간\n\n - 공고일 ~ 2024. 12. 6.(금 ) 까지 (채용 시 마감)\n\n4. 선발결과 : 개별 유선 통보\n\n\n5. 유의사항\n -  위 세부일정은 사정에 따라 다소 변경될 수 있음\n -  제출된 서류는 일체 반환하지 않으며, 추후 허위사실 발견 시 채용 취소\n\n - 기타 문의사항은 메일로 문의 ( genschool@uos.ac.kr ) "
        	}
        	]
        }
    """
            .trimIndent()

    private val feedbacks =
        listOf(
            "너무 힘듭니다 진짜로",
            "여기만큼 편한 곳 없습니다",
            "최고의 꿀 직장",
            "그만 일하고 싶어요 진짜",
            "쉬는 시간을 잘 안줘서 힘듭니다",
            "다들 행복하세요",
            "교직원 선생님들이 맛있는 거 많이 사주십니다."
        )
}
