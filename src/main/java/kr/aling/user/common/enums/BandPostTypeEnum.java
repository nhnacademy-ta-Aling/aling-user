package kr.aling.user.common.enums;

import lombok.Getter;

/**
 * 그룹 게시글 분류 Enum.
 *
 * @author 정유진
 * @since 1.0
 **/
@Getter
public enum BandPostTypeEnum {
    DEFAULT("default");

    private String typeName;

    BandPostTypeEnum(String typeName) {
        this.typeName = typeName;
    }
}
