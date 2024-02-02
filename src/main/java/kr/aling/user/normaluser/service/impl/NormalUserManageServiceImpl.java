package kr.aling.user.normaluser.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.transaction.Transactional;
import kr.aling.user.normaluser.dto.request.CreateNormalUserRequestDto;
import kr.aling.user.normaluser.dto.response.CreateNormalUserResponseDto;
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

    private final NormalUserManageRepository normalUserManageRepository;

    private final UserManageService userManageService;
    private final WantJobTypeReadService wantJobTypeReadService;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateNormalUserResponseDto registerNormalUser(CreateNormalUserRequestDto requestDto) {
        Long userNo = userManageService.registerUser(
                new CreateUserRequestDto(requestDto.getId(), requestDto.getPassword(), requestDto.getName())).getUserNo();
        WantJobType wantJobType = wantJobTypeReadService.findByWantJobTypeNo(requestDto.getWantJobTypeNo()).getWantJobType();

        NormalUser normalUser = NormalUser.builder()
                .userNo(userNo)
                .wantJobType(wantJobType)
                .phoneNo(requestDto.getPhoneNo())
                .birth(LocalDate.parse(requestDto.getBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();
        normalUser = normalUserManageRepository.save(normalUser);

        return new CreateNormalUserResponseDto(normalUser.getUser().getId(), normalUser.getUser().getName());
    }
}
