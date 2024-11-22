package sogoing.backend_server.app.dib.repository

import org.springframework.data.jpa.repository.JpaRepository
import sogoing.backend_server.app.dib.entity.Dib

interface DibRepository : JpaRepository<Dib, Long> {}
