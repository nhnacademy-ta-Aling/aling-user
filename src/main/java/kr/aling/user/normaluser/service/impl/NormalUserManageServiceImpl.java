package kr.aling.user.normaluser.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.transaction.Transactional;
import kr.aling.user.normaluser.dto.request.CreateNormalUserRequestDto;
import kr.aling.user.normaluser.entity.NormalUser;
import kr.aling.user.normaluser.repository.NormalUserManageRepository;
import kr.aling.user.normaluser.service.NormalUserManageService;
import kr.aling.user.user.dto.resquest.CreateUserRequestDto;
import kr.aling.user.user.service.UserManageService;
import kr.aling.user.wantjobtype.entity.WantJobType;
import kr.aling.user.wantjobtype.service.WantJobTypeReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 일반회원 CUD Service 구현체.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@Transactional
@Service
public class NormalUserManageServiceImpl implements NormalUserManageService {

    public static final String BIRTH_PATTERN = "yyyyMMdd";

    private final NormalUserManageRepository normalUserManageRepository;

    private final UserManageService userManageService;
    private final WantJobTypeReadService wantJobTypeReadService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerNormalUser(CreateNormalUserRequestDto requestDto) {
        Long createdUserNo = userManageService.registerUser(
                        new CreateUserRequestDto(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName())).getUserNo();
        WantJobType wantJobType =
                wantJobTypeReadService.findByWantJobTypeNo(requestDto.getWantJobTypeNo()).getWantJobType();

        NormalUser normalUser = NormalUser.builder()
                .userNo(createdUserNo)
                .wantJobType(wantJobType)
                .phoneNo(requestDto.getPhoneNo())
                .birth(LocalDate.parse(requestDto.getBirth(), DateTimeFormatter.ofPattern(BIRTH_PATTERN)))
                .build();
        normalUserManageRepository.save(normalUser);
    }
}
