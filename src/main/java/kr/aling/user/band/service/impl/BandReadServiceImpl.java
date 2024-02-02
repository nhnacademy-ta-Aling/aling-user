package kr.aling.user.band.service.impl;

import kr.aling.user.band.service.BandReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@Service
@Transactional(readOnly = true)
public class BandReadServiceImpl implements BandReadService {
}
