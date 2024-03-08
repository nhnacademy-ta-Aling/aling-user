package kr.aling.user.band.dummy;

import kr.aling.user.band.entity.Band;

/**
 * 그룹 더미.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandDummy {

    public static Band dummyPrivateBand() {
        return Band.builder()
                .name("dummyBandName")
                .info("dummyBandInfo")
                .isEnter(false)
                .isViewContent(false)
                .fileNo(1L)
                .build();
    }

    public static Band dummyPublicBand() {
        return Band.builder()
                .name("dummyBandName")
                .info("dummyBandInfo")
                .isEnter(true)
                .isViewContent(true)
                .fileNo(1L)
                .build();
    }
}
