package backend.dao;

import backend.domain.Device;
import backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT dev FROM DEVICE dev WHERE dev.deviceName = ?1 AND dev.deviceOwner = ?2")
    Optional<Device> findDeviceByNameAndOwner(@Param("1") String deviceName, @Param("2") User deviceOwner);
}
