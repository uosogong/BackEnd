package sogoing.backend_server.app.department.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class DepartmentCreateRequestDto(val departments: List<DepartmentDto>? = null)

data class DepartmentDto(
    @Schema(description = "부서명", example = "컴퓨터과학부") val name: String?,
    @Schema(
        description = "세부 내용",
        example =
            """1. 근무분야 및 조건

 - 근무내용 : 사업단 행정 및 학사업무 보조
 - 근무장소 : 빅데이터혁신융합대학사업단 사무실 ( 인문학관 5201-2 호 )
 - 모집인원 : 1 명
 - 시급 : 12,000 원

 - 근무기간 : 2024.12.23.~ 2025.2.28 .  ※ 근무기간은 추후 변동가능

 - 근무시간 : 주 4 일 / 1 일 6 시간 (10:00~17:00) / 12:00~13:00 점심시간 제외   ※ 근무시간 협의가능


2.  자격사항

◎  필수사항

 - 서울시립대학교 학부 재학생 (휴학생 제외, 근무 기간 중간에 휴학 불가)

◎  우대사항

 - 성실하고 근면한 학생

 - 사무 행정 경험자

 - 엑셀 , 파워포인트 활용 가능 학생

 - 장기근무 가능자

3.  심사방법 : 서류 전형 후 면접 진행

4.  제출서류

 -  빅데이터 혁신융합대학사업 근로장학생 지원서 ( 첨부양식 )

 -   재학증명서 ( 사본으로 우선 제출 가능하며 , 추후 원본 제출 )

5.  제출방법

 - 이메일 제출 ( psy2024@uos.ac.kr ) / 하나의 파일로 첨부하여 메일 제출

 - 메일 및 파일 제목 “ 빅데이터 근로장학생지원 _ 성명 ” 로 작성 후 첨부하여 제출



6.  접수기간 및 심사일정

 - 접수기간 : 공고일 ~ 2024. 12. 6.(금 ) 까지

 - 면접일정 : 서류심사 합격자에 한해서 면접일정 개별 유선 통보


7.  유의사항
 -  위 세부일정은 사정에 따라 다소 변경될 수 있음
 -  제출된 서류는 일체 반환하지 않으며 , 추후 허위사실 발견 시 채용 취소

 -  기타 문의사항은 메일로 문의 ( psy2024@uos.ac.kr ) """
    )
    val introduction: String?,
)
