package sogoing.backend_server.app.feedback.entity.enums

enum class Busy {
    HARD,
    NORMAL,
    EASY;

    companion object {
        fun getRandomBusy(): Busy {
            return Busy.values().random() // enum 값 중에서 랜덤으로 하나 선택
        }
    }
}
