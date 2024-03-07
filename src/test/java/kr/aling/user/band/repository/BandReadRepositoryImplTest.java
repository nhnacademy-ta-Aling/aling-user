package kr.aling.user.band.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.dummy.BandDummy;
import kr.aling.user.band.entity.Band;
import kr.aling.user.banduser.dummy.BandUserDummy;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduserrole.dummy.BandUserRoleDummy;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.config.JpaConfig;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 그룹 조회 Repository 테스트.
 *
 * @author 정유진
 * @since 1.0
 **/
@DataJpaTest
@Import(JpaConfig.class)
class BandReadRepositoryImplTest {

    Band band;
    AlingUser alingUser;
    BandUserRole bandUserRole;
    BandUser bandUser;
    @Autowired
    private BandReadRepository bandReadRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        bandUserRole = BandUserRoleDummy.dummyDefaultBandUserRole();
        alingUser = UserDummy.dummy();
        band = BandDummy.dummyPublicBand();
        bandUser = BandUserDummy.dummy(bandUserRole, band, alingUser);

        testEntityManager.persist(alingUser);
        testEntityManager.persist(band);
        testEntityManager.persist(bandUserRole);
        testEntityManager.persist(bandUser);
    }

    @AfterEach
    void clear() {
        testEntityManager.clear();
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 상세 정보 조회 쿼리 테스트")
    void getBandDetailInfoByBandName_successTest() {
        // given
        String bandName = band.getName();

        // when

        // then
        GetBandInfoResponseDto getBandInfoResponseDto =
                bandReadRepository.getBandDetailInfoByBandName(bandName).orElse(null);

        assertThat(getBandInfoResponseDto).isNotNull();
        assertThat(getBandInfoResponseDto.getBandNo()).isEqualTo(band.getBandNo());
        assertThat(getBandInfoResponseDto.getName()).isEqualTo(band.getName());
        assertThat(getBandInfoResponseDto.getFileNo()).isEqualTo(band.getFileNo());
        assertThat(getBandInfoResponseDto.getInfo()).isEqualTo(band.getInfo());
        assertThat(getBandInfoResponseDto.getIsEnter()).isEqualTo(band.getIsEnter());
        assertThat(getBandInfoResponseDto.getIsViewContent()).isEqualTo(band.getIsViewContent());
        assertThat(getBandInfoResponseDto.getIsUpload()).isEqualTo(band.getIsUpload());
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 목록을 조회 테스트")
    void getSearchBandInfoListByBandName_success_test() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when

        // then
        Page<GetBandInfoResponseDto> result =
                bandReadRepository.getSearchBandInfoListByBandName(band.getName(), pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).getBandNo()).isEqualTo(band.getBandNo());
        assertThat(result.getContent().get(0).getName()).isEqualTo(band.getName());
        assertThat(result.getContent().get(0).getFileNo()).isEqualTo(band.getFileNo());
        assertThat(result.getContent().get(0).getInfo()).isEqualTo(band.getInfo());
        assertThat(result.getContent().get(0).getIsUpload()).isEqualTo(band.getIsUpload());
        assertThat(result.getContent().get(0).getIsEnter()).isEqualTo(band.getIsEnter());
        assertThat(result.getContent().get(0).getIsViewContent()).isEqualTo(band.getIsViewContent());
    }

    @Test
    @DisplayName("회원이 가입한 그룹 목록 조회 테스트")
    void getJoinedBandInfoListByUserNo_success_test() {
        // given

        // when

        // then
        List<GetBandDetailInfoResponseDto> result =
                bandReadRepository.getJoinedBandInfoListByUserNo(alingUser.getUserNo());

        assertThat(result).isNotNull();
        assertThat(result.get(0).getBandNo()).isEqualTo(band.getBandNo());
        assertThat(result.get(0).getName()).isEqualTo(band.getName());
        assertThat(result.get(0).getFileNo()).isEqualTo(band.getFileNo());
        assertThat(result.get(0).getInfo()).isEqualTo(band.getInfo());
        assertThat(result.get(0).getIsUpload()).isEqualTo(band.getIsUpload());
        assertThat(result.get(0).getIsEnter()).isEqualTo(band.getIsEnter());
        assertThat(result.get(0).getIsViewContent()).isEqualTo(band.getIsViewContent());
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 조회 테스트")
    void getByName_successTest() {
        // given
        String bandName = band.getName();

        // when

        // then
        Optional<Band> resultOptional = bandReadRepository.getByName(bandName);
        assertThat(resultOptional).isPresent();

        Band result = resultOptional.get();
        assertThat(result.getBandNo()).isEqualTo(band.getBandNo());
        assertThat(result.getName()).isEqualTo(band.getName());
        assertThat(result.getInfo()).isEqualTo(band.getInfo());
        assertThat(result.getIsEnter()).isEqualTo(band.getIsEnter());
        assertThat(result.getIsViewContent()).isEqualTo(band.getIsViewContent());
        assertThat(result.getIsUpload()).isEqualTo(band.getIsUpload());
        assertThat(result.getIsDelete()).isEqualTo(band.getIsDelete());
        assertThat(result.getFileNo()).isEqualTo(band.getFileNo());
        assertThat(result.getFixPostNo()).isEqualTo(band.getFixPostNo());
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 존재 여부 조회 테스트")
    void existsNonDeleteBandByName_successTest() {
        // given
        String bandName = band.getName();
        String nonBandName = "qwopriuqpwoireuwqo";

        // when

        // then
        boolean result = bandReadRepository.existsNonDeleteBandByName(bandName);
        assertThat(result).isTrue();

        boolean notExistsResult = bandReadRepository.existsNonDeleteBandByName(nonBandName);
        assertThat(notExistsResult).isFalse();
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 회원 수 조회 테스트")
    void getCountBandUser() {
        // given
        String bandName = band.getName();

        // when

        // then
        long countBandUser = bandReadRepository.getCountBandUser(bandName);
        assertThat(countBandUser).isEqualTo(1L);
    }
}