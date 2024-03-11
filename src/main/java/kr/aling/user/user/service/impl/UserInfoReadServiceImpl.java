package kr.aling.user.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.common.feignclient.FileFeignClient;
import kr.aling.user.user.dto.response.ReadPostAuthorInfoResponseDto;
import kr.aling.user.user.dto.response.ReadUserInfoResponseDto;
import kr.aling.user.user.dto.resquest.ReadPostAuthorInfoRequestDto;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.service.UserInfoReadService;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : 이성준
 * @since : 1.0
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserInfoReadServiceImpl implements UserInfoReadService {

    private final BandUserReadRepository bandUserReadRepository;
    private final UserReadService userReadService;
    private final FileFeignClient fileFeignClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReadPostAuthorInfoResponseDto> requestPostAuthorInfo(Set<ReadPostAuthorInfoRequestDto> requests) {
        List<ReadPostAuthorInfoResponseDto> postAuthorInfoResponseDtos = new ArrayList<>();

        requests.forEach(
                request -> postAuthorInfoResponseDtos.add(getUserInfoResponse(request))
        );
        return postAuthorInfoResponseDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadUserInfoResponseDto readNormalUser(Long userNo) {
        AlingUser alingUser = userReadService.getAlingUserByUserNo(userNo);

        return ReadUserInfoResponseDto.builder()
                .userNo(alingUser.getUserNo())
                .username(alingUser.getName())
                .profilePath(Objects.nonNull(alingUser.getFileNo()) ? getFilePath(alingUser.getFileNo()) : null)
                .build();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReadUserInfoResponseDto> readUserInfos(List<Long> userNoList) {
        List<ReadUserInfoResponseDto> readUserInfoResponseDtos = new ArrayList<>();

        userNoList.forEach(
                userNo -> {
                    AlingUser alingUser = userReadService.getAlingUserByUserNo(userNo);

                    readUserInfoResponseDtos.add(ReadUserInfoResponseDto.builder()
                            .userNo(alingUser.getUserNo())
                            .username(alingUser.getName())
                            .profilePath(
                                    Objects.nonNull(alingUser.getFileNo()) ? getFilePath(alingUser.getFileNo()) : null)
                            .build());
                }
        );

        return readUserInfoResponseDtos;
    }

    /**
     * 게시물 작성자 정보 요청 객체를 매개변수로 하여 응답 객체를 반환하는 private 메서드입니다.
     *
     * @param request 조회할 게시물 작성자 정보 요청 객체
     * @return 조회된 게시물 작성자 정보 응답 객체
     * @author : 이성준
     * @since : 1.0
     */
    private ReadPostAuthorInfoResponseDto getUserInfoResponse(ReadPostAuthorInfoRequestDto request) {
        AlingUser alingUser = userReadService.getAlingUserByUserNo(request.getAuthorInfo().getUserNo());

        if (request.isBandPost()) {
            Optional<BandUser> optionalBandUser =
                    bandUserReadRepository.findById(request.getAuthorInfo().getUserNo());
            if (optionalBandUser.isPresent()) {
                alingUser = optionalBandUser.get().getAlingUser();
            }
        }

        ReadUserInfoResponseDto userInfo = ReadUserInfoResponseDto.builder()
                .userNo(alingUser.getUserNo())
                .username(alingUser.getName())
                .profilePath(Objects.nonNull(alingUser.getFileNo()) ? getFilePath(alingUser.getFileNo()) : null)
                .build();

        return new ReadPostAuthorInfoResponseDto(request.getPostNo(), userInfo);
    }

    private String getFilePath(Long fileNo) {
        return fileFeignClient.requestFileInfo(fileNo).getPath();
    }
}
