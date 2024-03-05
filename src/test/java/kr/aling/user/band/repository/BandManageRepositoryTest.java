package kr.aling.user.band.repository;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

import kr.aling.user.band.dummy.BandDummy;
import kr.aling.user.band.entity.Band;
import kr.aling.user.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * 그룹 관리 repository 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
@Import(JpaConfig.class)
class BandManageRepositoryTest {

    @Autowired
    BandManageRepository bandManageRepository;

    Band band;

    @BeforeEach
    void setUp() {
        band = BandDummy.dummyPrivateBand();
    }

    @Test
    @DisplayName("band(그룹) 엔티티 저장 테스트")
    void band_entity_save_test() {
        // given

        // when

        // then
        Band save = bandManageRepository.save(band);

        assertThat(save).isNotNull();
        assertThat(save.getBandNo()).isNotNull();
        assertThat(save.getName()).isEqualTo(band.getName());
        assertThat(save.getInfo()).isEqualTo(band.getInfo());
        assertThat(save.getIsEnter()).isEqualTo(band.getIsEnter());
        assertThat(save.getIsViewContent()).isEqualTo(band.getIsViewContent());
        assertThat(save.getIsUpload()).isEqualTo(band.getIsUpload());
        assertThat(save.getIsDelete()).isEqualTo(band.getIsDelete());
        assertThat(save.getFileNo()).isEqualTo(band.getFileNo());
        assertThat(save.getCreateAt()).isBefore(now());
        assertThat(save.getFixPostNo()).isNull();
    }

}