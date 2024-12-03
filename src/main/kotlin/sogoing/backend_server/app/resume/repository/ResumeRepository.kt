package sogoing.backend_server.app.resume.repository

import org.springframework.data.jpa.repository.JpaRepository
import sogoing.backend_server.app.resume.entity.Resume

interface ResumeRepository : JpaRepository<Resume, Long> {

}
