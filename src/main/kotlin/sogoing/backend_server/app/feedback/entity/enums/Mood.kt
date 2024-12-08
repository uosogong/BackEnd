package sogoing.backend_server.app.feedback.entity.enums

enum class Mood {
    HARMONY,
    STRICT,
    PLEASANT,
    SILENCE;

    companion object {
        fun getRandomMood(): Mood {
            return Mood.values().random() // enum 값 중에서 랜덤으로 하나 선택
        }
    }
}
